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

import java.io.Closeable;
import java.util.NoSuchElementException;

import de.codapro.api.ConversionException;

/**
 * A stream is a data exchange mechanism between two components.
 *
 * A stream transports vectors of data. The stream itself has a header assigning
 *   a name to the different elements of the data vector.
 */
public interface Stream extends Closeable {

	/**
	 * Appends a new data vecor to the stream.
	 *
	 * @param data The data vector to append. Is not allowed to be {@code null}.
	 * @throws ConversionException Signals the caller there was an problem sending the data vector.
	 */
	public void append(final DataVector data) throws ConversionException;

	/**
	 * @return The next available data vector.
	 *
	 * @throws NoSuchElementException If there is no next element since the stream is closed.
	 */
	public DataVector get();

	/**
	 * @return The header information of the stream.
	 */
	public StreamHeader getHeader();

	/**
	 * @return If the stream is closed.
	 */
	public boolean isClosed() ;

	/**
	 * @return Iff the stream is empty.
	 */
	public boolean isEmpty();

	/**
	 * Marks the stream as closed.
	 */
 	public void markClosed();

 	/**
 	 * Sets the current stream header and returns it.
 	 */
	public StreamHeader setHeader(final StreamHeader header);
}
