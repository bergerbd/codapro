package de.codapro.tdms.model.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsGroup;

public final class GroupImpl extends ObjectImpl implements TDMsGroup {
	private final Map<String, ChannelImpl> channels = new LinkedHashMap<>();

	public GroupImpl(final FileImpl parent, String name) {
		super(parent, name);
	}

	@Override
	public ChannelImpl create(final String name) {
		ChannelImpl channel = channels.get(name);

		if(channel == null) {
			channel = new ChannelImpl(this, name);
			channels.put(channel.getName(), channel);
		}

		return channel;
	}

	@Override
	public Optional<TDMsChannel> getChannelByName(final String name) {
		return Optional.ofNullable(channels.get(name));
	}

	@Override
	public Collection<TDMsChannel> getChannels() {
		return Collections.unmodifiableCollection(channels.values());
	}

	@Override
	public String toString() {
		final StringBuilder sb = objectToString("TDMsGroup");

		sb.append(", channels=[");

		for(final TDMsChannel channel : getChannels()) {
			sb.append(channel.toString());
			sb.append(",");
		}
		sb.append("]]");

		return sb.toString();
	}

	public boolean isCompletelyUnmodified() {
		return !isModified() && channels.values().stream().allMatch(ChannelImpl::isCompletelyUnmodified);
	}
}
