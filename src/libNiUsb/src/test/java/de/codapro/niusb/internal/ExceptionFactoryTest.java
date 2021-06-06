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
package de.codapro.niusb.internal;

import java.nio.ByteBuffer;

import de.codapro.niusb.internal.ExceptionFactory;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.codapro.niusb.api.NiDacException;
import de.codapro.niusb.driver.DriverLibrary;

public class ExceptionFactoryTest {

	@Test
	public void testCheckWithSuccessfullCall() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);

		final ExceptionFactory testee = new ExceptionFactory(mock);

		testee.check(0);
	}

	@Test(expected=NiDacException.class)
	public void testCheckWithNonSuccessfullCall() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);

		Mockito.when(mock.DAQmxGetErrorString(Mockito.eq(101), Mockito.any(), Mockito.any()))
				.thenAnswer(new Answer<Integer>() {
					@Override
					public Integer answer(final InvocationOnMock invocation) throws Throwable {
						final ByteBuffer buffer = (ByteBuffer)invocation.getArgument(1);
						buffer.array()[0] = 's';
						buffer.array()[1] = 'h';
						buffer.array()[2] = 'o';
						buffer.array()[3] = 'r';
						buffer.array()[4] = 't';
						buffer.array()[5] = ' ';
						buffer.array()[6] = 'm';
						buffer.array()[7] = 'e';
						buffer.array()[8] = 's';
						buffer.array()[9] = 's';
						buffer.array()[10] = 'a';
						buffer.array()[11] = 'g';
						buffer.array()[12] = 'e';
						buffer.array()[13] = '\0';

						return 0;
					}
				});

		Mockito.when(mock.DAQmxGetExtendedErrorInfo(Mockito.any(), Mockito.any()))
		.thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final ByteBuffer buffer = (ByteBuffer)invocation.getArgument(0);
				buffer.array()[0] = 'l';
				buffer.array()[1] = 'o';
				buffer.array()[2] = 'n';
				buffer.array()[3] = 'g';
				buffer.array()[4] = ' ';
				buffer.array()[5] = 'm';
				buffer.array()[6] = 'e';
				buffer.array()[7] = 's';
				buffer.array()[8] = 's';
				buffer.array()[9] = 'a';
				buffer.array()[10] = 'g';
				buffer.array()[11] = 'e';
				buffer.array()[12] = '\0';

				return 0;
			}
		});

		final ExceptionFactory testee = new ExceptionFactory(mock);

		testee.check(101);
	}
}
