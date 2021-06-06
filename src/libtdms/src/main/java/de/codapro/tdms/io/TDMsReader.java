/*
 * Copyright 2021 CoDaPro project. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package de.codapro.tdms.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;

import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import de.codapro.tdms.io.impl.ToC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.codapro.tdms.model.TDMsDataSegment;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.TDMsObject;
import de.codapro.tdms.model.Type;
import de.codapro.tdms.model.TypeFactory;
import de.codapro.tdms.model.impl.ChannelImpl;
import de.codapro.tdms.model.impl.FileSegment;
import de.codapro.tdms.model.impl.FileSegmentFactory;
import de.codapro.tdms.model.impl.FileImpl;
import de.codapro.tdms.model.impl.GroupImpl;
import de.codapro.tdms.model.impl.ObjectImpl;

public class TDMsReader implements AutoCloseable {
	/**
	 * Logger instance.
	 */
	private static final Logger log = LoggerFactory.getLogger(TDMsReader.class);

	/**
	 * The TDMs file header. Interestingly it is not TDMs but TDSm.
	 */
	private static final String TDSM_HEADER = "TDSm";

	/**
	 * Magic literal of the TDSm version
	 */
	private static final long VERSION_2_0 = 4713;

	private long dimension;

	/**
	 * The resulting TDSm file structure.
	 */
	private FileImpl file;

	/**
	 * The mapped input file.
	 */
	private MappedByteBuffer inputBuffer;

	/**
	 * Resulting input file.
	 */
	private File inputFile;

	/**
	 * Underlying random access file. Will be accessed using {@code inputBuffer}.
	 */
	private RandomAccessFile raf;

	public TDMsReader(final File inputFile) throws IOException {
		log.info("Creating TDMs reader for {}.", inputFile);

		this.inputFile = inputFile;
		raf = new RandomAccessFile(inputFile, "r");
		inputBuffer = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, raf.length());
	}

	@Override
	public void close() throws Exception {
		log.info("Closing TDMs file {}.", inputFile);

		raf.close();
	}

	private ChannelImpl getOrCreateChannel(final TDMsGroup group, final String name) {
		return (ChannelImpl)group.create(name);
	}

	private GroupImpl getOrCreateGroup(final String name) {
		return file.create(name);
	}

	private ObjectImpl getOrCreateObject(final String path) {
		if("/".equals(path)) {
			return file;
		}

		final String [] pathParts = path.split("/");
		for(int index = 1; index < pathParts.length; ++index) {
			String part = pathParts[index];
			if(part.startsWith("'") && part.endsWith("'")) {
				part = part.substring(1, part.length() - 1);
			}
			pathParts[index] = part;
		}

		if(pathParts.length == 2) {
			return getOrCreateGroup(pathParts[1]);
		} else if(pathParts.length == 3) {
			return getOrCreateChannel(getOrCreateGroup(pathParts[1]), pathParts[2]);
		} else {
			assert false : "Path " + path + " is too long.";
		}

		return null;
	}

	private boolean leadInPresent() {
		final int oldPosition = inputBuffer.position();

		try {
			final byte [] strData = new byte [4];
			inputBuffer.get(strData);

			final String headerTag = new String(strData, StandardCharsets.UTF_8);
			return TDSM_HEADER.equals(headerTag);
		} catch(final BufferUnderflowException e) {
			// ignore
			return false;
		} finally {
			inputBuffer.position(oldPosition);
		}
	}

	public TDMsFile read() {
		file = new FileImpl();
		while(leadInPresent()) {
			readSegment();
		}

		return file;
	}

	private void readObjects(long toc, BigInteger dataPosition) {
		long nmrOfObjects = Type.U32.read(inputBuffer, Long.class);
		log.info("Reading {} objects from segment", nmrOfObjects);

		final FileSegmentFactory factory = new FileSegmentFactory(inputBuffer, ToC.InterleavedData.isSetIn(toc));

		for(int index = 0; index < nmrOfObjects; ++index) {
			if(log.isInfoEnabled()) {
				log.info("Start reading section @{}.", Long.toHexString(inputBuffer.position()));
			}
			final String objectPath = Type.STRING.read(inputBuffer, String.class);
			log.info("Found object '{}'.", objectPath);
			final ObjectImpl object = getOrCreateObject(objectPath);
			long rawDataIndex = Type.U32.read(inputBuffer, Long.class);

			if(rawDataIndex == 0xFFFFFFFFl) {
				// no raw data available 
				log.info("No raw data present in this section.");
			} else if(ToC.DAQMxRawData.isSetIn(toc)) {
				log.error("Reading DAQMxRawData is not yet supported.");
			} else if(ToC.RawData.isSetIn(toc)) {
				final ChannelImpl channel = (ChannelImpl)object;
				final BigInteger count;

				if(rawDataIndex == 0) {
					// read data count from last segment
					final List<TDMsDataSegment> segments = channel.getDataSegments();
					final FileSegment lastSegment = (FileSegment)segments.get(segments.size() - 1);

					final Optional<BigInteger> lastSegmentSize = lastSegment.size();

					assert lastSegmentSize.isPresent() : "Last segment should have a size";

					count = lastSegmentSize.get();
				} else {
					// read data for new segment
					final Type type = TypeFactory.fromLong(Type.U32.read(inputBuffer, Long.class));
					dimension = Type.U32.read(inputBuffer, Long.class);
					count = Type.U64.read(inputBuffer, BigInteger.class);

					channel.setType(type);
				}

				if(dimension != 1) {
					log.warn("According to the specification 1 is the only valid value in TDMS 2.0.");
				}

				final FileSegment dataSection = factory.create(dataPosition, count, channel);

				// set position in stream for next segment
				dataPosition = dataSection.endPosition();
			}

			readProperties(object);

			object.setClean();
		}
	}

	private void readProperties(final TDMsObject object) {
		long nmrOfProperties = Type.U32.read(inputBuffer, Long.class);

		for(int i = 0; i < nmrOfProperties; ++i) {
			final String propertyName = Type.STRING.read(inputBuffer, String.class);
			log.info("Reading property '{}'.", propertyName);

			final Object propertyValue = TypeFactory.fromLong(Type.U32.read(inputBuffer, Long.class)).read(inputBuffer, Object.class);
			log.info("Property value is '{}'.", propertyValue);

			object.addProperty(propertyName, propertyValue);
		}
	}

	private void readSegment() {
		if(log.isDebugEnabled()) {
			log.debug("Reading TDMs segment at {}.", Integer.toHexString(inputBuffer.position()));
		}

		readTDMsTag();
		long toc = Type.U32.read(inputBuffer, Long.class);

		if(ToC.BigEndian.isSetIn(toc)) {
			log.error("This file is encoded in little endian format which is not yet supported.");
		}

		readVersionNumber();

		final BigInteger segmentOffset = Type.U64.read(inputBuffer, BigInteger.class);
		final BigInteger dataOffset = Type.U64.read(inputBuffer, BigInteger.class);

		final BigInteger segmentPosition = segmentOffset.add(BigInteger.valueOf(inputBuffer.position()));
		BigInteger dataPosition = dataOffset.add(BigInteger.valueOf(inputBuffer.position()));

		if(log.isInfoEnabled()) {
			log.info("Segment @{} and data @{}", Long.toHexString(segmentPosition.longValue()), Long.toHexString(dataPosition.longValue()));
		}

		readObjects(toc, dataPosition);
		inputBuffer.position(segmentPosition.intValue());
	}

	private void readTDMsTag() {
		final byte [] strData = new byte [4];
		inputBuffer.get(strData);

		final String headerTag = new String(strData, StandardCharsets.UTF_8);

		assert TDSM_HEADER.equals(headerTag) : "Segment header has to start with TDSm";
	}

	private void readVersionNumber() {
		final long versionNumber = Type.U32.read(inputBuffer, Long.class);

		if(versionNumber != VERSION_2_0) {
			log.warn("The file's version number ({}) is different than the expected value ({}). This might lead to errors.", versionNumber, VERSION_2_0); 
		}
	}
}
