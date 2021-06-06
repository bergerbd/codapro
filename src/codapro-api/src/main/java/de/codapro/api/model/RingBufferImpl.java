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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.NoSuchElementException;

import de.codapro.api.ConversionException;

/**
 * A ring buffer.
 */
public class RingBufferImpl<T> implements RingBuffer<T> {
	/**
	 * Buffer capacity.
	 */
	private final int capacity;

	/**
	 * The data ring buffer
	 */
	private T [] data;

	/**
	 * Current ring buffer index.
	 */
	private int index;

	/**
	 * Current ring buffer size
	 */
	private int size;

	@SuppressWarnings("unchecked")
	public RingBufferImpl(final Class<T> type, final int capacity) {
		this.capacity = capacity;
		this.size = 0;
		this.index = 0;

		data = (T[])Array.newInstance(type, capacity);
	}

	@Override
	public void append(final T element) throws ConversionException {
		if(size != capacity) {
			size += 1;
		}

		data[index] = element;
		index = (index + 1) % capacity;
	}

	@Override
	public T get(int elementIndex) {
		if(isEmpty()) {
			throw new NoSuchElementException("Ring buffer is empty.");
		}

		if(elementIndex > size) {
			throw new NoSuchElementException("Not enough elements in ring buffer.");
		}

		return data[(capacity + index - size + elementIndex) % capacity];
	}

	@Override
	public T [] toArray() {
		return data;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean isFull() {
		return size == capacity;
	}

	/**
	 * Removes the object at the zeroth position.
	 */
	@Override
	public T remove() {
		final T element = data[(capacity + index - size) % capacity];

		size = size - 1;

		return element;
	}

	public int size() {
		return size;
	}

	/**
	 * Turns the ring buffer into a stream. CAUTION
	 */
	@Override
	public java.util.stream.Stream<T> stream() {
		return Arrays.stream(data);
	}
}
