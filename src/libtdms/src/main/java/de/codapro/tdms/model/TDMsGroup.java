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
package de.codapro.tdms.model;

import java.util.Collection;
import java.util.Optional;

public interface TDMsGroup extends TDMsObject {
	/**
	 * Creates and adds a new channel with the given name if there is no channel with the given
	 *   name. Otherwise the method will just return the existing channel.
	 * 
	 * @return A valid group.
	 */
	public TDMsChannel create(final String name);

	/**
	 * @return The channel with the specified {@code name}.
	 */
	public Optional<TDMsChannel> getChannelByName(final String name);

	/**
	 * @return A read-only collection of all channels.
	 */
	public Collection<TDMsChannel> getChannels();

	@Override
	public default String getObjectPath() {
		final Optional<TDMsObject> parent = getParent();

		assert parent.isPresent() : "Groups should have a parent.";

		return parent.get().getObjectPath() + "'" + getName() + "'";
	}
}
