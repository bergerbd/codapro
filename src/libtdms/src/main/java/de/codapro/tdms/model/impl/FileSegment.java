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
import java.util.Optional;

import de.codapro.tdms.model.TDMsDataSegment;
import de.codapro.tdms.model.Type;

/**
 * The data area within a segment.
 */
public abstract  class FileSegment implements TDMsDataSegment {

	protected final MappedByteBuffer buffer;

	/**
	 * The number of data sets within the channel.
	 */
	protected final BigInteger numberOfDataSets;

	/**
	 * Starting position within the buffer
	 */
	protected final long position;

	/**
	 * Type of the data.
	 */
	protected final Type type;

	public FileSegment(final MappedByteBuffer buffer, final long position, final BigInteger numberOfDataSets, final Type type) {
		this.buffer = buffer;
		this.position = position;
		this.numberOfDataSets = numberOfDataSets;
		this.type = type;
	}

	/**
	 * Returns the first position after the data segment.
	 */
	public abstract BigInteger endPosition();

	public long getPosition() {
		return position;
	}

	public Type getType() {
		return type;
	}

	@Override
	public boolean isModified() {
		return false;
	}

	public abstract Object read(final long index);

	@Override
	public Optional<BigInteger> size() {
		return Optional.of(numberOfDataSets);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("FileSegment [numberOfDataSets=");
		sb.append(numberOfDataSets);
		sb.append(", position=");
		sb.append(getPosition());
		sb.append("]");

		return sb.toString();
	}
}
