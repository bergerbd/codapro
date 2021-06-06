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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang3.SerializationUtils;

/**
 * A simple container for a single data column. It consists of a list of data
 *   elements.
 */
public class DataVector implements Iterable<Object>, Serializable {
	/**
	 *  Serial version UID
	 */
	private static final long serialVersionUID = 8230788166525617022L;

	/**
	 * An artificial group end vector
	 */
	public static final DataVector GROUP_END = new DataVector();

	/**
	 * An artificial group start vector
	 */
	public static final DataVector GROUP_START = new DataVector();

	/**
	 * The actual data.
	 */
	private ArrayList<Object> data;

	public DataVector() {
		data = new ArrayList<>(10);
	}

	public DataVector(int size) {
		data = new ArrayList<>(Math.max(size, 10));
		ensureSize(size);
	}

	/**
	 * Appends an data element to the vector.
	 *
	 * @param next The data element to append.
	 * @return The data vector itself.
	 */
	public DataVector append(final Object next) {
		data.add(next);

		return this;
	}

	/**
	 * Appends multiple data elements.
	 *
	 * @param elements The elements to append in the correct order.
	 */
	public void appendAll(final Collection<Object> elements) {
		data.addAll(elements);
	}

	/**
	 * Puts null values until the backing array list has the correct size.
	 */
	private void ensureSize(int size) {
		for(int index = data.size(); index < size; ++index){
			data.add(null);
		}
	}

	/**
	 * Returns the element at position {@code index}.
	 * @param index The index of the element to return.
	 * @return The object stored at {@code index}.
	 * 
	 * @throws IndexOutOfBoundsException iff the index is not valid.
	 */
	public Object get(int index) {
		return data.get(index);
	}

	/**
	 * @return A read-only copy of the underlying data vector.
	 */
	public Collection<Object> getAll() {
		return Collections.unmodifiableCollection(data);
	}

	/**
	 * @return Number of date elements in this vector.
	 */
	public int size(){
		return data.size();
	}

	/**
	 * @return Iff this vector is empty.
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public Iterator<Object> iterator() {
		return data.iterator();
	}

	/**
	 * Removes data at {@code index}
	 */
	public void remove(int index) {
		data.remove(index);
	}

	/**
	 * Replaces the stored data by the passed data.
	 */
	public void replace(final Iterator<?> iterator) {
		data.clear();
		while(iterator.hasNext()) {
			data.add(iterator.next());
		}
	}

	/**
	 * Sets the {@code value} at index {@code i} and increases the buffer
	 *   vector size if necessary.
	 */
	public void set(int i, final Object value) {
		ensureSize(i + 1);
		data.set(i, value);
	}

	@Override
	public String toString() {
		return "DataVector<length = " + data.size() + ">";
	}

	/**
	 * Appends all elements to the vector.
	 */
	public void appendAll(final double[] elements) {
		for (final double element : elements) {
			data.add(element);
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return SerializationUtils.<DataVector>clone(this);
	}
}
