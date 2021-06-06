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
 * String data are handled in a different way then normal data. All strings are
 *   stored sequentially without length information. The starting positions of
 *   the different strings are stored within an index array before the strings
 *   itself.
 *
 * @author Bernhard J. Berger
 */
public class SingleChannelStringFileSegment extends FileSegment {

	/**
	 * Length of the data segment.
	 */
	private final BigInteger length;

	/**
	 * Position of the concatenated string within the stream.
	 */
	private long stringPosition;

	public SingleChannelStringFileSegment(final MappedByteBuffer buffer, final long position, final BigInteger numberOfDataSets, final BigInteger length) {
		super(buffer, position, numberOfDataSets, Type.STRING);

		this.stringPosition = position + numberOfDataSets.longValue() * Type.U32.size();
		this.length = length;
	}

	@Override
	public BigInteger endPosition() {
		return BigInteger.valueOf(getPosition()).add(length);
	}


	@Override
	public Object read(final long index) {
		// find start offset of the string using the string index table
		long startOffset = 0;
		if(index > 0) {
			buffer.position((int)(getPosition() + (index - 1) * Type.U32.size()));
			startOffset = Type.U32.read(buffer, Long.class);
		}

		// find end offset of the string using the string index table
		buffer.position((int)(getPosition() + index * Type.U32.size()));
		long endOffset = Type.U32.read(buffer, Long.class);

		// read actual string
		buffer.position((int)(stringPosition + startOffset));
		return Type.readString(buffer.asReadOnlyBuffer(), (int)(endOffset - startOffset));
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("SingleChannelStringFileSegment [numberOfDataSets=");
		sb.append(numberOfDataSets);
		sb.append(", position=");
		sb.append(getPosition());
		sb.append("]");

		return sb.toString();
	}
}
