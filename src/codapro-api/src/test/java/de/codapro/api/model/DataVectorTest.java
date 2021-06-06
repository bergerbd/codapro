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

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class DataVectorTest {

	@Test
	public void testIsEmptyOnEmptyVector() {
		final DataVector testee = new DataVector();

		assertTrue(testee.isEmpty());
	}

	@Test
	public void testIsEmptyOnNonEmptyVector() {
		final DataVector testee = new DataVector();

		testee.append(new Object());

		assertFalse(testee.isEmpty());
	}

	@Test
	public void testIteratorOnEmptyVector() {
		final DataVector testee = new DataVector();

		final Iterator<Object> iterator = testee.iterator();

		assertNotNull(iterator);
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratorOnNonEmptyVector() {
		final DataVector testee = new DataVector();
		final Object obj = new Object();

		testee.append(obj);

		final Iterator<Object> iterator = testee.iterator();

		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertEquals(obj, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testToStrin() {
		final DataVector testee = new DataVector();

		assertNotNull(testee.toString());
	}

	@Test
	public void testClone() throws CloneNotSupportedException {
		final DataVector testee = new DataVector();
		testee.append(Integer.valueOf(0));
		testee.append(Integer.valueOf(1));
		testee.append(Integer.valueOf(2));
		testee.append(Integer.valueOf(3));

		final DataVector cloned = (DataVector)testee.clone();

		testee.append(Integer.valueOf(4));

		assertEquals(5, testee.size());
		assertEquals(4, cloned.size());
	}
}
