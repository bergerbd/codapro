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

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * A data channel within a TDSm file.
 */
public interface TDMsChannel extends TDMsObject, Iterable<Object> {
	/**
	 * Creates a new data segment for interleaved data.
	 */
	public void createDataSegment(final InterleavedChannelDataProvider provider, final int channelIndex);

	/**
	 * Creates a new data segment for the given data {@code provider}.
	 */
	public void createDataSegment(final SingleChannelDataProvider<?> provider);

	/**
	 * @return A read-only list of all data segments
	 */
	public List<TDMsDataSegment> getDataSegments();

	/**
	 * To calculate the number of data sets is only possible if all data segments
	 *   were loaded from disk or the number of data sets is known before writing
	 *   the file. If the result cannot be calculated the result is not present.
	 *
	 * @return The number of data sets within this channel.
	 */
	public Optional<BigInteger> getNumberOfDataSets();

	@Override
	public default String getObjectPath() {
		final Optional<TDMsObject> parent = getParent();

		assert parent.isPresent() : "Channels should have a parent.";

		return parent.get().getObjectPath() + "/'" + getName() + "'";
	}

	/**
	 * @return The data type of the channel.
	 */
	public Type getType();

	/**
	 * Sets the data type if possible.
	 */
	public void setType(final Type type);
}
