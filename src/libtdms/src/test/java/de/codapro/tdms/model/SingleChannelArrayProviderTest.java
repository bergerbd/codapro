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
package de.codapro.tdms.model;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;

public class SingleChannelArrayProviderTest {

	@Test(expected = NoSuchElementException.class)
	public void testEmptyConstructor() {
		final SingleChannelArrayProvider<Integer> testee = new SingleChannelArrayProvider<Integer>(Type.I32);

		assertEquals(Type.I32, testee.getType());
		assertFalse(testee.hasNext());
		assertEquals(Optional.of(BigInteger.ZERO), testee.size());

		testee.next();
	}

	@Test
	public void testConstructor() {
		final SingleChannelArrayProvider<Integer> testee = new SingleChannelArrayProvider<Integer>(Type.I32, new Integer [] {1,2,3,4,5});

		assertEquals(Type.I32, testee.getType());
		assertEquals(Optional.of(BigInteger.valueOf(5)), testee.size());

		assertEquals(Integer.valueOf(1), testee.next());
		assertEquals(Integer.valueOf(2), testee.next());
		assertEquals(Integer.valueOf(3), testee.next());
		assertEquals(Integer.valueOf(4), testee.next());
		assertEquals(Integer.valueOf(5), testee.next());

		assertFalse(testee.hasNext());
	}
}
