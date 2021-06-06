package de.codapro.tdms.model.impl;

import java.math.BigInteger;
import java.nio.MappedByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.codapro.tdms.model.Type;

/**
 * Factory for TDMs file data instances.
 */
public final class FileSegmentFactory {
	/**
	 * Logger instance.
	 */
	private static final Logger log = LoggerFactory.getLogger(FileSegmentFactory.class);

	private final InterleavedSharedSegment interleavedSharedSegment;

	private final MappedByteBuffer buffer;

	private final boolean segmentIsInterleaved;

	public FileSegmentFactory(final MappedByteBuffer buffer, final boolean segmentIsInterleaved) {
		log.info("Creating {} file segment factory.", segmentIsInterleaved ? "interleaved" : "normal");
		this.buffer = buffer;
		this.interleavedSharedSegment = new InterleavedSharedSegment(buffer);
		this.segmentIsInterleaved = segmentIsInterleaved;
	}

	public FileSegment create(final BigInteger dataPosition, final BigInteger dataCount, final ChannelImpl channel) {
		FileSegment dataSegment;

		if(segmentIsInterleaved) {
			dataSegment = createInterleaved(dataPosition, dataCount, channel);
		} else {
			dataSegment = createNonInterleaved(dataPosition, dataCount, channel);
		}

		channel.addDataSegment(dataSegment);

		return dataSegment;
	}

	private FileSegment createNonInterleaved(final BigInteger dataPosition, final BigInteger dataCount, final ChannelImpl channel) {
		if(Type.STRING.equals(channel.getType())) {
			final BigInteger length = Type.U64.read(buffer, BigInteger.class);

			return new SingleChannelStringFileSegment(buffer, dataPosition.longValue(), dataCount, length);
		} else {
			return new SingleChannelFileSegment(buffer, dataPosition.longValue(), channel.getType(), dataCount);
		}
	}

	private FileSegment createInterleaved (final BigInteger dataPosition, final BigInteger dataCount, final ChannelImpl channel) {
		if(Type.STRING.equals(channel.getType())) {
			log.error("Strings are unsupported in interleave mode.");
			throw new IllegalArgumentException("TDMs files with strings in interleaved segments are not supported in interleave mode.");
		}

		log.info("Creating interleaved file segment.");
		return interleavedSharedSegment.create(dataPosition.longValue(), channel.getType(), dataCount);
	}
}
