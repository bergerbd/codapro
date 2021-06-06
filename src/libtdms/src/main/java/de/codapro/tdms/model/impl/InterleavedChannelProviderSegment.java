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
package de.codapro.tdms.model.impl;

import java.math.BigInteger;
import java.util.Optional;

import de.codapro.tdms.model.DataProvider;
import de.codapro.tdms.model.InterleavedChannelDataProvider;
import de.codapro.tdms.model.Type;

public class InterleavedChannelProviderSegment<T> extends ProviderSegment<T> {

	private final DataProvider<T> provider;
	private final int channelIndex;

	public InterleavedChannelProviderSegment(final DataProvider<T> provider, final int channelIndex) {
		this.provider = provider;
		this.channelIndex = channelIndex;
	}

	public DataProvider<T> getProvider() {
		return provider;
	}

	@Override
	public Type getType() {
		throw new UnsupportedOperationException();
	}

	public int getChannelIndex() {
		return channelIndex;
	}

	@Override
	public Optional<BigInteger> size() {
		return getProvider().size();
	}

	public void setSize(long count) {
		((InterleavedChannelDataProvider)getProvider()).setSize(count);
	}

	@Override
	public boolean isModified() {
		return ((InterleavedChannelDataProvider)getProvider()).isModified();
	}

	@Override
	public void setClean() {
		((InterleavedChannelDataProvider)getProvider()).setClean();
	}
}
