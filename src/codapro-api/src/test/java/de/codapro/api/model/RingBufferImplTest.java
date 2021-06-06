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

import java.util.NoSuchElementException;

import de.codapro.api.ConversionException;
import org.junit.Test;

public class RingBufferImplTest {

	@Test
	public void testIsEmptyOnNewBuffer() {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		assertEquals(true, testee.isEmpty());
	}

	@Test
	public void testIsEmptyOnPartiallyFilledBuffer() throws ConversionException {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());

		assertEquals(false, testee.isEmpty());
	}

	@Test
	public void testIsEmptyOnFullBuffer() throws ConversionException {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());

		assertEquals(false, testee.isEmpty());
	}

	@Test
	public void testIsFullOnNewBuffer() {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		assertEquals(false, testee.isFull());
	}

	@Test
	public void testIsFullOnPartiallyFilledBuffer() throws ConversionException {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());

		assertEquals(false, testee.isFull());
	}

	@Test
	public void testIsFullOnFullBuffer() throws ConversionException {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());

		assertEquals(true, testee.isFull());
	}

	@Test
	public void testAppendWithEmptyBuffer() throws ConversionException {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		final Object element00 = new Object();

		assertEquals(true, testee.isEmpty());
		testee.append(element00);
		assertEquals(false, testee.isEmpty());
		assertEquals(element00, testee.get(0));
	}

	@Test
	public void testAppendWithFullBuffer() throws ConversionException {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		final Object element00 = new Object();
		final Object element01 = new Object();
		final Object element02 = new Object();
		final Object element03 = new Object();
		final Object element04 = new Object();
		final Object element05 = new Object();
		final Object element06 = new Object();
		final Object element07 = new Object();
		final Object element08 = new Object();
		final Object element09 = new Object();
		final Object element10 = new Object();

		assertEquals(true, testee.isEmpty());
		testee.append(element00);
		testee.append(element01);
		testee.append(element02);
		testee.append(element03);
		testee.append(element04);
		testee.append(element05);
		testee.append(element06);
		testee.append(element07);
		testee.append(element08);
		testee.append(element09);

		assertEquals(true, testee.isFull());
		assertEquals(element00, testee.get(0));
		assertEquals(element01, testee.get(1));
		assertEquals(element02, testee.get(2));
		assertEquals(element03, testee.get(3));
		assertEquals(element04, testee.get(4));
		assertEquals(element05, testee.get(5));
		assertEquals(element06, testee.get(6));
		assertEquals(element07, testee.get(7));
		assertEquals(element08, testee.get(8));
		assertEquals(element09, testee.get(9));

		testee.append(element10);

		assertEquals(true, testee.isFull());
		assertEquals(element01, testee.get(0));
		assertEquals(element02, testee.get(1));
		assertEquals(element03, testee.get(2));
		assertEquals(element04, testee.get(3));
		assertEquals(element05, testee.get(4));
		assertEquals(element06, testee.get(5));
		assertEquals(element07, testee.get(6));
		assertEquals(element08, testee.get(7));
		assertEquals(element09, testee.get(8));
		assertEquals(element10, testee.get(9));
	}

	@Test
	public void testGetDataOnNewBuffer() {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		assertNotNull(testee.toArray());
	}

	@Test
	public void testGetDataOnPartiallyFilledBuffer() throws ConversionException {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());

		assertNotNull(testee.stream());
	}

	@Test
	public void testStreamOnNewBuffer() {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		assertNotNull(testee.toArray());
	}

	@Test
	public void testStreamOnPartiallyFilledBuffer() throws ConversionException {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());
		testee.append(new Object());

		assertNotNull(testee.stream());
	}

	@Test
	public void testRemove() throws ConversionException {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		final Object element00 = new Object();
		final Object element01 = new Object();
		final Object element02 = new Object();
		final Object element03 = new Object();
		final Object element04 = new Object();
		final Object element05 = new Object();
		final Object element06 = new Object();
		final Object element07 = new Object();
		final Object element08 = new Object();
		final Object element09 = new Object();
		final Object element10 = new Object();

		assertEquals(true, testee.isEmpty());

		testee.append(element00);
		testee.append(element01);
		testee.append(element02);
		testee.append(element03);
		testee.append(element04);
		testee.append(element05);
		testee.append(element06);
		testee.append(element07);
		testee.append(element08);
		testee.append(element09);

		assertEquals(true, testee.isFull());

		assertEquals(element00, testee.remove());
		assertEquals(element01, testee.remove());
		assertEquals(element02, testee.remove());
		assertEquals(element03, testee.remove());
		assertEquals(element04, testee.remove());
		assertEquals(element05, testee.remove());

		testee.append(element10);

		assertEquals(element06, testee.remove());
		assertEquals(element07, testee.remove());
		assertEquals(element08, testee.remove());
		assertEquals(element09, testee.remove());
		assertEquals(element10, testee.remove());

		assertEquals(true, testee.isEmpty());
	}

	@Test(expected = NoSuchElementException.class)
	public void testGetOnEmptyBuffer() {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		testee.get(0);
	}

	@Test(expected = NoSuchElementException.class)
	public void testGetBeyondSize() throws ConversionException {
		final RingBufferImpl<Object> testee = new RingBufferImpl<Object>(Object.class, 10);

		final Object element00 = new Object();
		final Object element01 = new Object();
		final Object element02 = new Object();

		testee.append(element00);
		testee.append(element01);
		testee.append(element02);

		testee.get(4);
	}
}
