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

import de.codapro.tdms.model.DataProvider;
import de.codapro.tdms.model.SingleChannelDataProvider;
import de.codapro.tdms.model.Type;

/**
 * Segment provider for single channels.
 */
public class SingleChannelProviderSegment<T> extends ProviderSegment<T> {

	private final SingleChannelDataProvider<T> provider;

	public SingleChannelProviderSegment(final SingleChannelDataProvider<T> provider) {
		this.provider = provider;
	}

	@Override
	public DataProvider<T> getProvider() {
		return provider;
	}

	@Override
	public Type getType() {
		return provider.getType();
	}
}
