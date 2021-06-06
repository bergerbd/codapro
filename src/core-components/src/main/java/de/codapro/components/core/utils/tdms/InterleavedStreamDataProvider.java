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
package de.codapro.components.core.utils.tdms;

import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Optional;

import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;
import de.codapro.tdms.model.InterleavedChannelDataProvider;
import de.codapro.tdms.model.Type;

public class InterleavedStreamDataProvider implements InterleavedChannelDataProvider {
	private boolean modified = true;

	private Optional<BigInteger> size = Optional.empty();

	private final Stream stream;

	private final Type[] types;

	private DataVector data;

	private final int[] ids;


	public InterleavedStreamDataProvider(final Stream stream, final int[] columnIds, final Type[] columnTypes) {
		this.ids = columnIds;
		this.stream = stream;
		this.types = columnTypes;
	}

	@Override
	public Optional<BigInteger> size() {
		return size;
	}

	@Override
	public boolean hasNext() {
		if(data != null) {
			return true;
		}

		try {
			data = stream.get();
		} catch(final NoSuchElementException e) {
			return false;
		}

		return true;
	}

	@Override
	public Object[] next() {
		try {
			final Object [] result = new Object[types.length];

			for(int i = 0; i < ids.length; ++i) {
				result[i] = data.get(ids[i]);
			}

			return result;
		} catch(final IndexOutOfBoundsException e) {
			throw new NoSuchElementException("No more elements available.");
		}
	}

	@Override
	public Type getType(int index) {
		return types[index];
	}

	@Override
	public int getChannelCount() {
		return types.length;
	}

	@Override
	public void setSize(long count) {
		size = Optional.of(BigInteger.valueOf(count));
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setClean() {
		modified = false;
	}
}
