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
package de.codapro.api.model;

import de.codapro.api.ConversionException;

/**
 * A marking stream marks data vectors with boolean flags. This function is handy
 *   if you try to detect specific datasets in the stream.
 *   
 * The marking stream gets the target data stream as the first constructor's
 *   parameter (see  {@link MarkingStream#MarkingStream(Stream, int)}). The second
 *   parameter gives the number of elements to buffer. Furthermore, the capacity
 *   parameter specifies the number of elements you can mark (in the past or in
 *   the future). By calling the {@link MarkingStream#append(DataVector, boolean)}
 *   method you add a new data vector with a given marking value. The may be
 *   overwritten but we will cover that in a second. If you want to mark the datasets
 *   buffered, you can call {@link MarkingStream#markPast()}. This method is going
 *   to mark {@code capacity} elements already appended. If you want to mark data sets
 *   not yet appended, you can call {@link MarkingStream#markFuture()}. This method
 *   will mark {@code capacity} elements that will be appended in the future. A future
 *   marking may override the marking value passed to {@link MarkingStream#append(DataVector, boolean)}.
 */
public class MarkingStream implements AutoCloseable {
	/**
	 * Buffer capacity.
	 */
	private final int capacity;

	/**
	 * The data ring buffer
	 */
	private final Object [] data;

	/**
	 * The data stream to copy the data to.
	 */
	private final Stream dataStream;

	/**
	 * The number of future data sets to mark.
	 */
	private int dataToMark;

	/**
	 * Current insertion index.
	 */
	private int index;

	/**
	 * The data ring buffer
	 */
	private final boolean [] markings;

	/**
	 * Current size.
	 */
	private int size;

	/**
	 * Create a new marking stream.
	 *
	 * @param dataStream Output stream for the data.
	 * @param capacity Number of elements to buffer.
	 */
	public MarkingStream(final Stream dataStream, final int capacity) {
		this.dataStream = dataStream;
		this.capacity = capacity;
		this.size = 0;
		this.index = 0;

		data = new Object[capacity];
		markings = new boolean[capacity];
	}

	/**
	 * Appends a new data vector.
	 *
	 * @param element The data vector to append.
	 * @param marking The initial marking value.
	 * @throws ConversionException If there is a communication error using the underlying stream.
	 */
	public void append(final DataVector element, final boolean marking) throws ConversionException {
		if(size == capacity) {
			copyDataToStream(index);

			data[index] = element;
			markings[index] = marking || (dataToMark > 0);
			index = (index + 1) % capacity;
			dataToMark = Math.max(dataToMark - 1, 0);
		} else {
			data[index] = element;
			markings[index] = marking;

			index = (index + 1) % capacity;
			size += 1;
		}
	}

	@Override
	public void close() throws Exception {
		while(size != 0) {
			final int eldestIndex = (index - size + capacity) % capacity;

			copyDataToStream(eldestIndex);
			size = size - 1;
		}

		dataStream.markClosed();
	}

	private void copyDataToStream(final int dataIndex) throws ConversionException {
		final DataVector vector = (DataVector)data[dataIndex];
		vector.append(markings[dataIndex]);
		dataStream.append(vector);
	}

	/**
	 * Mark {@link MarkingStream#capacity} elements in the future.
	 */
	public void markFuture() {
		dataToMark = capacity;
	}

	/**
	 * Mark {@link MarkingStream#capacity} elements in the past.
	 */
	public void markPast() {
		for(int i = 0; i < capacity; ++i) {
			markings[i] = true;
		}
	}
}
