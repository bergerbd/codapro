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

import org.junit.Test;

public class StreamHeaderTest {

	@Test
	public void testParameterlessConstructor() {
		final StreamHeader testee = new StreamHeader();

		assertEquals(0, testee.size());
	}

	@Test
	public void testCopyConstructor() {
		final StreamHeader original = new StreamHeader();
		original.add("a");
		original.add("b");
		original.add("c");

		final StreamHeader testee = new StreamHeader(original);

		assertEquals(3, testee.size());

		assertEquals(0, testee.indexOf("a"));
		assertEquals(1, testee.indexOf("b"));
		assertEquals(2, testee.indexOf("c"));
	}

	@Test
	public void testAddOne() {
		final StreamHeader testee = new StreamHeader();

		assertEquals(0, testee.size());
		testee.add("a");
		assertEquals(1, testee.size());
	}

	@Test
	public void testAddMultiple() {
		final StreamHeader testee = new StreamHeader();

		assertEquals(0, testee.size());
		testee.add(new String [] {"a", "b", "c"});
		assertEquals(3, testee.size());
	}


	@Test
	public void testIndexOfExisting() {
		final StreamHeader testee = new StreamHeader();

		testee.add("a");
		testee.add("b");
		testee.add("c");

		assertEquals(2, testee.indexOf("c"));
		assertEquals(1, testee.indexOf("b"));
		assertEquals(0, testee.indexOf("a"));
	}

	@Test
	public void testIndexOfNonExisting() {
		final StreamHeader testee = new StreamHeader();

		testee.add("a");
		testee.add("b");
		testee.add("c");

		assertEquals(-1, testee.indexOf("f"));
	}

	@Test
	public void testNameOfExisting() {
		final StreamHeader testee = new StreamHeader();

		testee.add("a");
		testee.add("b");
		testee.add("c");

		assertEquals("b", testee.nameOf(1));
		assertEquals("c", testee.nameOf(2));
		assertEquals("a", testee.nameOf(0));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testNameOfNonExisting() {
		final StreamHeader testee = new StreamHeader();

		testee.add("a");
		testee.add("b");
		testee.add("c");

		testee.nameOf(4);
	}

	@Test
	public void testRemoveAll() {
		final StreamHeader testee = new StreamHeader();

		assertEquals(0, testee.size());
		testee.add("a");
		testee.add("b");
		testee.add("c");
		assertEquals(3, testee.size());
		testee.remove(0);
		testee.remove(0);
		testee.remove(0);
		assertEquals(0, testee.size());
	}

	@Test
	public void testSize() {
		final StreamHeader testee = new StreamHeader();

		assertEquals(0, testee.size());
		testee.add("a");
		assertEquals(1, testee.size());
		testee.add("b");
		assertEquals(2, testee.size());
		testee.add("c");
		assertEquals(3, testee.size());
		assertEquals(3, testee.size());
	}

}
