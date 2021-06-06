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


/**
 * The data area within a segment.
 */
public class SingleChannelFileSegment extends FileSegment {

	public SingleChannelFileSegment(final MappedByteBuffer buffer, final long position, final Type type, final BigInteger numberOfDataSets) {
		super(buffer, position, numberOfDataSets, type);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("SingleChannelFileSegment [numberOfDataSets=");
		sb.append(numberOfDataSets);
		sb.append(", position=");
		sb.append(getPosition());
		sb.append("]");

		return sb.toString();
	}

	/**
	 * Returns the first position after the data segment.
	 */
	public BigInteger endPosition() {
		return BigInteger.valueOf(position).add(numberOfDataSets.multiply(BigInteger.valueOf(type.size())));
	}

	public Object read(final long index) {
		long dataPosition = position + index * type.size();

		buffer.position((int)dataPosition);

		return type.read(buffer, Object.class);
	}
}
