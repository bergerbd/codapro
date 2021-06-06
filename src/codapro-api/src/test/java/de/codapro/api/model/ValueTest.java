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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValueTest {

	@Test
	public void testIfInitiallyUnset() {
		final Value<Integer> value = new Value<Integer>();

		assertTrue(value.isUnset());
	}

	@Test
	public void testIsSetAfterSetValue() {
		final Value<Integer> value = new Value<Integer>();
		value.set(10);

		assertTrue(!value.isUnset());
	}

	@Test
	public void testGetvalueAfterSetValue() {
		final Value<Integer> value = new Value<Integer>();
		value.set(10);

		assertEquals((Integer)10, value.get());
	}

	@Test
	public void testToStringWithValue() {
		final Value<Integer> value = new Value<Integer>();
		value.set(10);

		assertEquals("Constant<10>", value.toString());
	}

	@Test
	public void testToStringWithoutValue() {
		final Value<Integer> value = new Value<Integer>();

		assertEquals("Constant<null>", value.toString());
	}
}
