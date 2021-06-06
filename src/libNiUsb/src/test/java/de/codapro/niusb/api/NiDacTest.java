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
package de.codapro.niusb.api;

import java.nio.ByteBuffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.codapro.niusb.driver.DriverLibrary;

public class NiDacTest {

	@Test
	public void testToString() {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiDac testee = new NiDac(mock);

		testee.toString();
	}

	@Test
	public void testCreateTask() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiDac testee = new NiDac(mock);

		Mockito.when(mock.DAQmxGetTaskName(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenAnswer(new Answer<Integer>() {
					@Override
					public Integer answer(final InvocationOnMock invocation) throws Throwable {
						final ByteBuffer buffer = (ByteBuffer)invocation.getArgument(1);
						buffer.array()[0] = 'f';
						buffer.array()[1] = 'o';
						buffer.array()[2] = 'o';
						buffer.array()[3] = 'b';
						buffer.array()[4] = 'a';
						buffer.array()[5] = 'r';
						buffer.array()[6] = '\0';

						return 0;
					}
				});

		final NiTask task = testee.createTask("foobar");

		assertEquals("foobar", task.getName());
	}

	@Test
	public void testCreateScale() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiDac testee = new NiDac(mock);

		testee.createScale("scale", 1.0, 2.0, PreScaledUnits.Bar, "MyUnit");
	}
}
