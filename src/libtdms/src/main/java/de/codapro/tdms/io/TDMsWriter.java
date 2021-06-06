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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.codapro.tdms.io.impl.DataWriter;
import de.codapro.tdms.io.impl.DataWriterFactory;
import de.codapro.tdms.io.impl.ToC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsDataSegment;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;
import de.codapro.tdms.model.TypeFactory;
import de.codapro.tdms.model.TypedValue;
import de.codapro.tdms.model.impl.ChannelImpl;
import de.codapro.tdms.model.impl.FileImpl;
import de.codapro.tdms.model.impl.GroupImpl;
import de.codapro.tdms.model.impl.InterleavedChannelProviderSegment;
import de.codapro.tdms.model.impl.ObjectImpl;

/**
 * Class for writing TDMs files.
 */
public class TDMsWriter implements AutoCloseable {
	/**
	 * Logger instance.
	 */
	private static final Logger log = LoggerFactory.getLogger(TDMsWriter.class);

	/**
	 * Current TDMs version number
	 */
	private static final long VERSION_NUMBER = 4713l;

	/**
	 * File handle for writing.
	 */
	private RandomAccessFile raf = null;

	/**
	 * File to write to.
	 */
	private final File targetFile;

	public TDMsWriter(final File file) {
		log.info("Creating TDMs writer for {}.", file);
		this.targetFile = file;
	}

	@Override
	public void close() throws Exception {
		log.info("Closing TDMs writer for {}.", targetFile);
		if(raf != null) {
			raf.close();
			raf = null;
		}
	}

	/**
	 * @return A newly created file object.
	 */
	public TDMsFile create() {
		return new FileImpl();
	}

	/**
	 * Writes the given {@code file} to disk.
	 *
	 * @param file The TDMs object to write
	 *
	 * @throws IOException When an IO error occurs.
	 */
	public void write(final TDMsFile file) throws IOException {
		if(raf == null) {
			raf = new RandomAccessFile(targetFile, "rw");
		}

		final FileImpl internalFileObject = (FileImpl)file;

		while(!internalFileObject.isCompletelyUnmodified()) {
			writeSegments(internalFileObject);
		}
	}

	private Optional<DataWriter> writeChannel(final TDMsChannel channel, Optional<DataWriter> result) throws IOException {
		final Optional<TDMsDataSegment> optSegment = channel.getDataSegments().stream().filter(TDMsDataSegment::isModified).findFirst();

		if(optSegment.isPresent()) {
			final List<TDMsDataSegment> processedSegments = channel.getDataSegments().stream().filter(seg -> !seg.isModified()).collect(Collectors.toList());
			final TDMsDataSegment segment = optSegment.get();

			final TDMsDataSegment lastProcessedSegment = processedSegments.isEmpty() ? null : processedSegments.get(processedSegments.size() - 1);
			final Optional<BigInteger> lastSegmentSize = lastProcessedSegment == null ? Optional.empty() : lastProcessedSegment.size();
			final Optional<BigInteger> segmentSize = segment.size().isPresent() ? segment.size() : Optional.empty();

			if(lastProcessedSegment != null
					&& lastSegmentSize.isPresent()
					&& segmentSize.isPresent()
					&& lastSegmentSize.get().equals(segmentSize.get())) {
				Type.U32.write(0l, raf);
				result = DataWriterFactory.create(segment, raf);
			} else {
				Type.U32.write(0x14l, raf);
				Type.U32.write(channel.getType().getMagicValue(), raf);
				Type.U32.write(1l, raf); // dimension
				final long dataCountPosition = raf.getFilePointer();
				Type.U64.write(BigInteger.ZERO, raf);
				result = DataWriterFactory.create(segment, raf, dataCountPosition);
			}
		} else {
			Type.U32.write(0xFFFFFFFFl, raf); // raw data index
		}
		return result;
	}

	private Optional<DataWriter> writeObject(final ObjectImpl object) throws IOException {
		log.info("Writing TDMsObject {}.", object.getObjectPath());
		Optional<DataWriter> result = Optional.empty();

		Type.STRING.write(object.getObjectPath(), raf); // object name

		if(object instanceof TDMsChannel) {
			result = writeChannel((TDMsChannel)object, result);
		} else {
			Type.U32.write(0xFFFFFFFFl, raf); // raw data index
		}

		writeProperties(object);

		object.setClean();

		return result;
	}

	private void writeProperties(final ObjectImpl object) throws IOException {
		Type.U32.write((long)object.getProperties().size(), raf);
		for(final Map.Entry<String, Object> entry : object.getProperties().entrySet()) {
			log.info("Writing property {}.", entry);
			Type.STRING.write(entry.getKey(), raf);

			final Type entryType = TypeFactory.typeOf(entry.getValue());
			Type.U32.write(entryType.getMagicValue(), raf);

			if(entry.getValue() instanceof TypedValue) {
				entryType.write(((TypedValue)entry.getValue()).getValue(), raf);
			} else {
				entryType.write(entry.getValue(), raf);
			}
		}
	}

	/**
	 * Writes the number of segments that will be written to {@code raf}.
	 */
	private void writeSegmentCount(final FileImpl file) throws IOException {
		int segmentCount = 0;

		if(file.isModified()) {
			segmentCount += 1;
		}

		for(final TDMsGroup group : file.getGroups()) {
			final GroupImpl groupImpl = (GroupImpl)group;

			if(groupImpl.isModified()) {
				segmentCount += 1;
			}

			for(final TDMsChannel channel : group.getChannels()) {
				final ChannelImpl channelImpl = (ChannelImpl)channel;

				if(channelImpl.isModified() || channel.getDataSegments().stream().anyMatch(TDMsDataSegment::isModified)) {
					segmentCount += 1;
				}
			}
		}

		Type.U32.write((long)segmentCount, raf);
	}

	private void writeSegments(final FileImpl file) throws IOException {
		log.info("Writing segments for {}.", file);
		writeTDMsTag();
		writeToC(file);
		writeVersionNumber();

		long segmentOffsetPosition = raf.getFilePointer();
		Type.U64.write(BigInteger.ZERO, raf);
		Type.U64.write(BigInteger.ZERO, raf);
		long offsetPosition = raf.getFilePointer();

		writeSegmentCount(file);

		if(file.isModified()) {
			writeObject(file);
		}

		final List<DataWriter> dataToWrite = new ArrayList<>();

		for(final TDMsGroup group : file.getGroups()) {
			final GroupImpl groupImpl = (GroupImpl)group;

			if(groupImpl.isModified()) {
				writeObject(groupImpl);
			}

			for(final TDMsChannel channel : group.getChannels()) {
				final ChannelImpl channelImpl = (ChannelImpl)channel;

				writeObject(channelImpl).ifPresent(dataToWrite::add);
			}
		}

		final long dataPosition = raf.getFilePointer();

		for(final DataWriter writer : dataToWrite) {
			final BigInteger numberOfDataSets = writer.execute();
			log.info("{} of data sets were written to file.", numberOfDataSets);
		}

		long nextSegmentPosition = raf.getFilePointer();

		raf.seek(segmentOffsetPosition);
		log.info("Segment offset is {}", nextSegmentPosition - offsetPosition);
		Type.U64.write(BigInteger.valueOf(nextSegmentPosition - offsetPosition), raf);
		log.info("Data offset is {}", dataPosition - offsetPosition);
		Type.U64.write(BigInteger.valueOf(dataPosition - offsetPosition), raf);
		raf.seek(nextSegmentPosition);
	}

	/**
	 * Writes the TDMs tag to {@code raf}.
	 */
	private void writeTDMsTag() throws IOException {
		raf.write("TDSm".getBytes());
	}

	/**
	 * Writes the table of contents entry. Therefore, it determines which entries have
	 * to be written.
	 */
	private void writeToC(final FileImpl file) throws IOException {
		boolean containsMetadata = file.isModified();
		boolean containsData = false;
		boolean containsInterleavedData = false;

		for(final TDMsGroup group : file.getGroups()) {
			containsMetadata |= ((ObjectImpl)group).isModified();

			for(final TDMsChannel channel : group.getChannels()) {
				containsMetadata |= ((ObjectImpl)channel).isModified();

				for(final TDMsDataSegment segment : ((ChannelImpl)channel).getDataSegments()) {
					containsData |= segment.isModified();
					containsInterleavedData |= (segment instanceof InterleavedChannelProviderSegment);
				}
			}
		}

		long toc = 0;
		toc |= (containsMetadata || containsData ? ToC.MetaData.getMagicNumber() : 0);
		toc |= (containsData ? ToC.RawData.getMagicNumber() : 0);
		toc |= (containsInterleavedData ? ToC.InterleavedData.getMagicNumber() : 0);
		toc |= ToC.NewObjectList.getMagicNumber();

		Type.U32.write(toc, raf);
	}

	/**
	 * Writes the TDMs version number
	 */
	private void writeVersionNumber() throws IOException {
		Type.U32.write(VERSION_NUMBER, raf);
	}
}
