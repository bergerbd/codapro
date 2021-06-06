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

import java.util.NoSuchElementException;

import de.codapro.api.ConversionException;

/**
 * An interface for a ring buffer with all necessary methods.
 */
public interface RingBuffer<T> {

	/**
	 * Appends {@code element} to the buffer. The behaviour of append is 
	 *   implementation defined. It might block or overwrite.
	 *
	 * @param element The element to add.
	 * @throws ConversionException Iff there is an error while appending an element.
	 */
	public void append(final T element) throws ConversionException;

	/**
	 * Returns the {@code nth} element of the buffer.
     *
	 * @throws NoSuchElementException If the element cannot be returned.
	 */
	public T get(int elementIndex);

	/**
	 * @return Iff the buffer is empty.
	 */
	public boolean isEmpty();

	/**
	 * @return Iff the buffer is full.
	 */
	public boolean isFull();

	/**
	 * Removes the object at the zeroth position.
	 */
	public T remove();

	/**
	 * Turns the ring buffer into a stream.
	 */
	public java.util.stream.Stream<T> stream();

	/**
	 * Converts the ring buffer into an array.
	 * 
	 * @return The converted array.
	 */
	public T [] toArray();
}
