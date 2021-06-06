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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A stream header associates a name to each existing column.
 */
public class StreamHeader implements Iterable<String> {
	/**
	 * The index order
	 */
	private final List<String> columnOrder = new ArrayList<>();

	public StreamHeader() {
	}

	public StreamHeader(final StreamHeader other) {
		this.columnOrder.addAll(other.columnOrder);
	}

	/**
	 * Adds a new column.
	 *
	 * @param name The name of the column to add.
	 * @return A instance to itself.
	 */
	public StreamHeader add(final String name) {
		columnOrder.add(name);
		return this;
	}

	/**
	 * Adds new columns.
	 *
	 * @param names The names of the columns to add.
	 * @return A instance to itself.
	 */
	public StreamHeader add(final String[] names) {
		for (final String name : names) {
			add(name);
		}

		return this;
	}

	/**
	 * Calculates the index of a column.
	 *
	 * @param columnName The name of the column to search.
	 * @return Index of the column or <code>-1</code> if there is no such column.
	 */
	public int indexOf(final String columnName) {
		return columnOrder.indexOf(columnName);
	}

	@Override
	public Iterator<String> iterator() {
		return columnOrder.iterator();
	}

	/**
	 * Returns the name of a specific column.
	 *
	 * @param i Index of the column.
	 * @return The name at the given column index.
	 */
	public String nameOf(final int i) {
		return columnOrder.get(i);
	}

	/**
	 * Removes a column of the header.
	 *
	 * @param index Index of the column to remove.
	 */
	public void remove(final int index) {
		columnOrder.remove(index);
	}

	/**
	 * @return The number of existing columns.
	 */
	public int size() {
		return columnOrder.size();
	}

	@Override
	public String toString() {
		return "StreamHeader [" + columnOrder.toString() + "]";
	}
}
