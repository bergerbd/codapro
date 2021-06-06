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
package de.codapro.components.core.stream;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import de.codapro.api.ConversionException;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

public class ReadOnlyStreamCopyCreatorTest {

	@Test
	public void testEnterGroupSuccess() throws IllegalAccessException, ConversionException {
		final Logger log = Mockito.mock(Logger.class);
		final Stream stream = Mockito.mock(Stream.class);

		final ReadOnlyStreamCopyCreator testee = new ReadOnlyStreamCopyCreator();

		FieldUtils.writeDeclaredStaticField(ReadOnlyStreamCopyCreator.class, "log", log, true);
		FieldUtils.writeDeclaredField(testee, "outputStream2", stream, true);

		testee.enterGroup();

		Mockito.verify(stream).append(DataVector.GROUP_START);
	}

	@Test
	public void testEnterGroupFailure() throws IllegalAccessException, ConversionException {
		final Logger log = Mockito.mock(Logger.class);
		final Stream stream = Mockito.mock(Stream.class);

		Mockito.doThrow(ConversionException.class).when(stream).append(Mockito.any());

		final ReadOnlyStreamCopyCreator testee = new ReadOnlyStreamCopyCreator();

		FieldUtils.writeDeclaredStaticField(ReadOnlyStreamCopyCreator.class, "log", log, true);
		FieldUtils.writeDeclaredField(testee, "outputStream2", stream, true);

		testee.enterGroup();

		Mockito.verify(log).error(Mockito.anyString());
	}

	@Test
	public void testLeaveGroupSuccess() throws IllegalAccessException, ConversionException {
		final Logger log = Mockito.mock(Logger.class);
		final Stream stream = Mockito.mock(Stream.class);

		final ReadOnlyStreamCopyCreator testee = new ReadOnlyStreamCopyCreator();

		FieldUtils.writeDeclaredStaticField(ReadOnlyStreamCopyCreator.class, "log", log, true);
		FieldUtils.writeDeclaredField(testee, "outputStream2", stream, true);

		testee.leaveGroup();

		Mockito.verify(stream).append(DataVector.GROUP_END);
	}

	@Test
	public void testLeaveGroupFailure() throws IllegalAccessException, ConversionException {
		final Logger log = Mockito.mock(Logger.class);
		final Stream stream = Mockito.mock(Stream.class);

		Mockito.doThrow(ConversionException.class).when(stream).append(Mockito.any());

		final ReadOnlyStreamCopyCreator testee = new ReadOnlyStreamCopyCreator();

		FieldUtils.writeDeclaredStaticField(ReadOnlyStreamCopyCreator.class, "log", log, true);
		FieldUtils.writeDeclaredField(testee, "outputStream2", stream, true);

		testee.leaveGroup();

		Mockito.verify(log).error(Mockito.anyString());
	}

	@Test
	public void testProcessFailure() throws IllegalAccessException, ConversionException {
		final Logger log = Mockito.mock(Logger.class);
		final Stream stream = Mockito.mock(Stream.class);
		final DataVector vector = Mockito.mock(DataVector.class);

		Mockito.doThrow(ConversionException.class).when(stream).append(Mockito.any());

		final ReadOnlyStreamCopyCreator testee = new ReadOnlyStreamCopyCreator();

		FieldUtils.writeDeclaredStaticField(ReadOnlyStreamCopyCreator.class, "log", log, true);
		FieldUtils.writeDeclaredField(testee, "outputStream2", stream, true);

		testee.process(vector);

		Mockito.verify(log).error(Mockito.anyString(), Mockito.eq(vector));
	}

}
