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

import de.codapro.api.ConversionException;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class BufferingStreamTest {

	@Test
	public void testProtocolWithClose() throws ConversionException {
		final DataVector vector00 = new DataVector();
		final DataVector vector01 = new DataVector();
		final DataVector vector02 = new DataVector();
		final DataVector vector03 = new DataVector();
		final DataVector vector04 = new DataVector();
		final DataVector vector05 = new DataVector();
		final DataVector vector06 = new DataVector();

		final Stream stream = Mockito.mock(Stream.class);

		final BufferingStream testee = new BufferingStream(stream, 5);
		testee.append(vector00);
		testee.append(vector01);
		testee.append(vector02);
		testee.append(vector03);
		testee.append(vector04);
		testee.append(vector05);
		testee.append(vector06);
		testee.close();

		final InOrder order = Mockito.inOrder(stream);
		order.verify(stream).append(vector00);
		order.verify(stream).append(vector01);
		order.verify(stream).append(vector02);
		order.verify(stream).append(vector03);
		order.verify(stream).append(vector04);
		order.verify(stream).append(vector05);
		order.verify(stream).append(vector06);
	}


	@Test
	public void testProtocolWithoutClose() throws ConversionException {
		final DataVector vector00 = new DataVector();
		final DataVector vector01 = new DataVector();
		final DataVector vector02 = new DataVector();
		final DataVector vector03 = new DataVector();
		final DataVector vector04 = new DataVector();
		final DataVector vector05 = new DataVector();
		final DataVector vector06 = new DataVector();

		final Stream stream = Mockito.mock(Stream.class);

		final BufferingStream testee = new BufferingStream(stream, 5);
		testee.append(vector00);
		testee.append(vector01);
		testee.append(vector02);
		testee.append(vector03);
		testee.append(vector04);
		testee.append(vector05);
		testee.append(vector06);

		final InOrder order = Mockito.inOrder(stream);
		order.verify(stream).append(vector00);
		order.verify(stream).append(vector01);

	}
}
