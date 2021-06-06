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
package de.codapro.tdms.io.impl;

import java.math.BigInteger;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;

import de.codapro.tdms.model.impl.FileSegment;

public class TDMsIteratorTest {

	@Test
	public void testToString() {
		final FileSegment mock = Mockito.mock(FileSegment.class);
		Mockito.when(mock.size()).thenReturn(Optional.of(BigInteger.ZERO));

		final TDMsIterator testee = new TDMsIterator(Collections.singletonList(mock));

		testee.toString();
	}

	@Test(expected = NoSuchElementException.class)
	public void testNextOnEmptyIterator() {
		final FileSegment mock = Mockito.mock(FileSegment.class);
		Mockito.when(mock.size()).thenReturn(Optional.of(BigInteger.ZERO));

		final TDMsIterator testee = new TDMsIterator(Collections.singletonList(mock));

		testee.next();
		testee.next();
		testee.next();
	}
}
