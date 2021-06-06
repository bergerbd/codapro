package de.codapro.tdms.model.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.codapro.tdms.io.impl.TDMsIterator;
import de.codapro.tdms.model.InterleavedChannelDataProvider;
import de.codapro.tdms.model.SingleChannelDataProvider;
import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsDataSegment;
import de.codapro.tdms.model.Type;

/**
 * A data channel within a TDSm file.
 */
public class ChannelImpl extends ObjectImpl implements TDMsChannel {
	/**
	 * Logger instance
	 */
	private static final Logger log = LoggerFactory.getLogger(ChannelImpl.class);


	/**
	 * The different data areas for this channel
	 */
	private final List<TDMsDataSegment> dataAreas = new ArrayList<>();

	/**
	 * The data type of the data within the channel.
	 */
	private Type type = Type.VOID;

	public ChannelImpl(final GroupImpl parent, final String name) {
		super(parent, name);
	}

	public void addDataSegment(final FileSegment data) {
		log.info("Adding {} to {}.", data, this);

		dataAreas.add(data);
	}

	@Override
	public void createDataSegment(final InterleavedChannelDataProvider provider, final int channelIndex) {
		final TDMsDataSegment segment = new InterleavedChannelProviderSegment(provider, channelIndex);

		if(!provider.getType(channelIndex).equals(getType())) {
			throw new IllegalStateException("Type of provider (" + provider.getType(channelIndex) + ") and channel (" + getType() + ") does not match.");
		}

		dataAreas.add(segment);
	}

	@Override
	public void createDataSegment(final SingleChannelDataProvider<?> provider) {
		final TDMsDataSegment segment = new SingleChannelProviderSegment(provider);

		if(!provider.getType().equals(getType())) {
			throw new IllegalStateException("Type of provider (" + provider.getType() + ") and channel (" + getType() + ") does not match.");
		}

		dataAreas.add(segment);
	}

	@Override
	public List<TDMsDataSegment> getDataSegments() {
		return Collections.unmodifiableList(dataAreas);
	}

	@Override
	public Optional<BigInteger> getNumberOfDataSets() {
		if(dataAreas.stream().anyMatch(segment -> !segment.size().isPresent())) {
			return Optional.empty();
		}

		BigInteger sum = BigInteger.ZERO;
		for(final TDMsDataSegment segment : dataAreas) {
			final Optional<BigInteger> segmentSize = segment.size();

			assert segmentSize.isPresent() : "Impossible";

			sum = sum.add(segmentSize.get());
		}

		return Optional.of(sum);
	}

	@Override
	public Type getType() {
		return type;
	}

	public boolean isCompletelyUnmodified() {
		return !isModified() && dataAreas.stream().noneMatch(TDMsDataSegment::isModified);
	}

	@Override
	public Iterator<Object> iterator() {
		return new TDMsIterator((List<FileSegment>)(List<?>)dataAreas);
	}

	@Override
	public void setType(final Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		final StringBuilder sb = objectToString("TDMsChannel");

		sb.append(", numberOfDataSets=");
		sb.append(getNumberOfDataSets());
		sb.append(", dataType=");
		sb.append(type);
		sb.append("]");

		return sb.toString();
	}
}
