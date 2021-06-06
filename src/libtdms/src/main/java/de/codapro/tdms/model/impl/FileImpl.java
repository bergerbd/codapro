package de.codapro.tdms.model.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;

public final class FileImpl extends ObjectImpl implements TDMsFile {
	private final Map<String, GroupImpl> groups = new LinkedHashMap<>();

	public FileImpl() {
		super(null, "/");
	}

	@Override
	public GroupImpl create(final String name) {
		GroupImpl group = groups.get(name);

		if(group == null) {
			group = new GroupImpl(this, name);
			groups.put(group.getName(), group);
		}

		return group;
	}

	@Override
	public Optional<TDMsGroup> getGroupByName(final String name) {
		return Optional.ofNullable(groups.get(name));
	}

	@Override
	public Collection<TDMsGroup> getGroups() {
		return Collections.unmodifiableCollection(groups.values());
	}

	@Override
	public String toString() {
		final StringBuilder sb = objectToString("TDMsFile");

		sb.append(", groups=[");
		for(final TDMsGroup group : getGroups()) {
			sb.append(group.toString());
			sb.append(", ");
		}
		sb.append("]]");

		return sb.toString();
	}

	public boolean isCompletelyUnmodified() {
		return !isModified() && groups.values().stream().allMatch(GroupImpl::isCompletelyUnmodified);
	}
}
