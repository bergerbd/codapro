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
package de.codapro.niusb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import de.codapro.niusb.ReadDataToTDMs;
import org.junit.Test;
import org.mockito.Mockito;

import de.codapro.niusb.api.NiDacException;
import de.codapro.niusb.driver.DriverLibrary;

public class ReadDataToTDMsTest {

	@Test
	public void test() throws IOException, NiDacException, InterruptedException {
		final String inputString = "\n\n\n\n\ny\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

		final InputStream input = new ByteArrayInputStream(inputString.getBytes());
		final PrintStream output = Mockito.mock(PrintStream.class);
		final DriverLibrary library = Mockito.mock(DriverLibrary.class);

		final ReadDataToTDMs testee = new ReadDataToTDMs(input, output, library);
		testee.setTerminate(true);
		testee.run();
	}
}
