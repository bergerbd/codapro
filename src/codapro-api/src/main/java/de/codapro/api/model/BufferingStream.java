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
 * A stream that buffers a specific number of elements. Sometimes, it is necessary
 *   to have a specific amount of data vectors in memory to do some specific
 *   calculation. Therefore, you can use a buffering stream. It's purpose is to
 *   copy all data to the {@code dataStream} given in the constructor
 *   ({@link BufferingStream#BufferingStream(Stream, int)}). The second parameter
 *   specifies the number of buffered elements.
 *
 * Data can be appended to this stream by calling ({@link BufferingStream#append(DataVector)}).
 *   The method pushes the oldest buffered element to the {@code dataStream} if it already
 *   buffers {@code capacity} elements.
 *
 *  If the stream is closed it is going to push the remaining buffered elements to the
 *   {@code dataStream}.
 */
public class BufferingStream extends RingBufferImpl<DataVector> implements AutoCloseable {
	/**
	 * The stream for putting data into.
	 */
	private final Stream dataStream;

	public BufferingStream(final Stream dataStream, final int capacity) {
		super(DataVector.class, capacity);

		this.dataStream = dataStream;
	}

	@Override
	public void append(final DataVector element) throws ConversionException {
		if(isFull()) {
			// we are going to overwrite an element, let's push it to the output stream.
			dataStream.append(super.get(0));
		}

		super.append(element);
	}

	@Override
	public void close() throws ConversionException {
		while(!isEmpty()) {
			dataStream.append(super.remove());
		}

		dataStream.markClosed();
	}
}
