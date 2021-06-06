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
import de.codapro.tdms.model.TDMsDataSegment;

/**
 * A {@code TDMsProviderSegment} is used for creating a data segment while writing.
 *   It can either provide data for a single or multiple channels.
 * 
 */
public abstract class ProviderSegment<T> implements TDMsDataSegment {

	/**
	 * If this segment was already persisted.
	 */
	private boolean modified = true;

	/**
	 * @return The associated data provider.
	 */
	public abstract DataProvider<T> getProvider();

	@Override
	public Optional<BigInteger> size() {
		return getProvider().size();
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	public void setClean() {
		// TODO Should we convert the data segment?
		this.modified  = false;
	}
}
