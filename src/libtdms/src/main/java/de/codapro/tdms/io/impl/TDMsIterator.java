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
package de.codapro.tdms.io.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;

import de.codapro.tdms.model.impl.FileSegment;

/**
 * Iterator for a TDMsChannel. The data for a channel can be stored to different
 *   segments within the file.
 */
public class TDMsIterator implements Iterator<Object> {


	/**
	 * Number of entries in the current data segment.
	 */
	private BigInteger count;

	/**
	 * All data segments within this iterator.
	 */
	private final LinkedList<FileSegment> dataSegments;

	/**
	 * Data index within the current data segment.
	 */
	private long index = 0;

	/**
	 * Data segment
	 */
	private FileSegment data;

	public TDMsIterator(final Collection<FileSegment> areas) {
		this.dataSegments = new LinkedList<>(areas);

		activateNextSegment();
	}

	private void activateNextSegment() {
		this.data = dataSegments.removeFirst();

		final Optional<BigInteger> dataSize = data.size();

		assert dataSize.isPresent() : "Illegal state.";

		this.count = dataSize.get();
		this.index = 0;
	}

	@Override
	public boolean hasNext() {
		if(index < count.intValue()) {
			return true;
		}

		if(dataSegments.isEmpty()) {
			index = Long.MAX_VALUE;
			return false;
		}

		activateNextSegment();

		return true;
	}

	@Override
	public Object next() {
		if(!hasNext()) {
			throw new NoSuchElementException("There is no next element.");
		}

		return data.read(index++);
	}

	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append("TDMsIterator [");
		buffer.append(index);
		buffer.append("/");
		buffer.append(count);
		buffer.append("  in ");
		buffer.append(data);
		buffer.append(" segments left [");
		for(final FileSegment segment : dataSegments) {
			buffer.append(segment);
			buffer.append(", ");
		}
		buffer.append("]]");

		return buffer.toString();
	}
}
