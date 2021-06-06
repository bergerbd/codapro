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

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class MarkingStreamTest {

	@Test
	public void testCloseOnNonFullStream() throws Exception {
		final Stream stream = Mockito.mock(Stream.class);
		final DataVector vector0 = Mockito.mock(DataVector.class);
		final DataVector vector1 = Mockito.mock(DataVector.class);
		final DataVector vector2 = Mockito.mock(DataVector.class);
		final DataVector vector3 = Mockito.mock(DataVector.class);
		final DataVector vector4 = Mockito.mock(DataVector.class);
		final DataVector vector5 = Mockito.mock(DataVector.class);

		final MarkingStream testee = new MarkingStream(stream, 10);
		testee.append(vector0, false);
		testee.append(vector1, false);
		testee.append(vector2, false);
		testee.append(vector3, false);
		testee.append(vector4, false);
		testee.append(vector5, false);

		testee.close();

		final InOrder orderConstraint = Mockito.inOrder(stream);
		orderConstraint.verify(stream).append(vector0);
		orderConstraint.verify(stream).append(vector1);
		orderConstraint.verify(stream).append(vector2);
		orderConstraint.verify(stream).append(vector3);
		orderConstraint.verify(stream).append(vector4);
		orderConstraint.verify(stream).append(vector5);
	}

	@Test
	public void testCloseOnFullStream() throws Exception {
		final Stream stream = Mockito.mock(Stream.class);
		final DataVector vector0 = Mockito.mock(DataVector.class);
		final DataVector vector1 = Mockito.mock(DataVector.class);
		final DataVector vector2 = Mockito.mock(DataVector.class);
		final DataVector vector3 = Mockito.mock(DataVector.class);
		final DataVector vector4 = Mockito.mock(DataVector.class);
		final DataVector vector5 = Mockito.mock(DataVector.class);

		final MarkingStream testee = new MarkingStream(stream, 5);
		testee.append(vector0, false);
		testee.append(vector1, false);
		testee.append(vector2, false);
		testee.append(vector3, false);
		testee.append(vector4, false);
		testee.append(vector5, false);

		testee.close();

		final InOrder orderConstraint = Mockito.inOrder(stream);
		orderConstraint.verify(stream).append(vector0);
		orderConstraint.verify(stream).append(vector1);
		orderConstraint.verify(stream).append(vector2);
		orderConstraint.verify(stream).append(vector3);
		orderConstraint.verify(stream).append(vector4);
		orderConstraint.verify(stream).append(vector5);
	}
}
