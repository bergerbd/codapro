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
import java.nio.MappedByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import de.codapro.tdms.model.Type;

public class InterleavedSharedSegment {

	private final MappedByteBuffer buffer;

	private int columnSize = 0;

	private Type [] types = new Type[0];

	private Optional<Long> position = Optional.empty();

	public InterleavedSharedSegment(final MappedByteBuffer buffer) {
		this.buffer = buffer;
	}

	public FileSegment create(final long position, final Type type, final BigInteger numberOfDatasets) {
		if(!this.position.isPresent()) {
			this.position = Optional.of(position);
		}

		final int offsetSize = (int)Arrays.stream(types).collect(Collectors.summarizingInt(Type::size)).getSum();

		final Type [] newTypes = new Type[types.length + 1];
		System.arraycopy(types, 0, newTypes, 0, types.length);
		newTypes[types.length] = type;

		types = newTypes;

		columnSize = (int)Arrays.stream(types).collect(Collectors.summarizingInt(Type::size)).getSum();

		return new InterleavedChannelFileSegment(buffer, this.position.get(), type, numberOfDatasets, this, offsetSize);
	}

	public int getColumnSize() {
		return columnSize;
	}
}
