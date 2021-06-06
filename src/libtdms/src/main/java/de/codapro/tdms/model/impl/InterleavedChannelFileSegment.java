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

import de.codapro.tdms.model.Type;

public class InterleavedChannelFileSegment extends FileSegment {

	private final InterleavedSharedSegment interleavedSharedSegment;
	private final int offsetSize;

	public InterleavedChannelFileSegment(final MappedByteBuffer buffer, final long position, final Type type, final BigInteger numberOfDatasets, final InterleavedSharedSegment sharedSegment, final int offsetSize) {
		super(buffer, position, numberOfDatasets, type);

		this.interleavedSharedSegment = sharedSegment;
		this.offsetSize = offsetSize;
	}

	@Override
	public BigInteger endPosition() {
		return BigInteger.valueOf(position).add(numberOfDataSets.multiply(BigInteger.valueOf(interleavedSharedSegment.getColumnSize())));
	}

	@Override
	public Object read(final long index) {
		long dataPosition = position + index * interleavedSharedSegment.getColumnSize() + offsetSize;

		buffer.position((int)dataPosition);

		return type.read(buffer, Object.class);
	}

}
