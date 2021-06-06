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

import java.io.File;
import java.nio.ByteBuffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.codapro.niusb.driver.DriverLibrary;

public class NiTaskTest {

	@Test
	public void testConstructor() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiTask testee = new NiTask(mock, "foobar");

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

		assertEquals("NiTask [name=foobar]", testee.toString());
	}

	@Test
	public void testToStringWithError() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiTask testee = new NiTask(mock, "foobar");

		Mockito.when(mock.DAQmxGetTaskName(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(1);

		assertEquals("NiTask [name=<error>]", testee.toString());
	}

	@Test
	public void testClear() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiTask testee = new NiTask(mock, "task");

		testee.clear();

		Mockito.verify(mock).DAQmxClearTask(Mockito.any());
	}

	@Test
	public void testCreateAnalogInputVoltageChannel() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiTask testee = new NiTask(mock, "task");

		testee.createAnalogInputVoltageChannel("test", "foo", TerminalConfig.ConfigurationDefault, 2.0, 3.0);

		Mockito.verify(mock).DAQmxCreateAIVoltageChan(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.eq(TerminalConfig.ConfigurationDefault.getApiValue()), Mockito.eq(2.0), Mockito.eq(3.0), Mockito.eq(Scale.Volts.getApiValue()), Mockito.eq(null));
	}

	@Test
	public void testStart() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiTask testee = new NiTask(mock, "task");

		testee.start();

		Mockito.verify(mock).DAQmxStartTask(Mockito.any());
	}

	@Test
	public void testStop() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiTask testee = new NiTask(mock, "task");

		testee.stop();

		Mockito.verify(mock).DAQmxStopTask(Mockito.any());
	}

	@Test
	public void testCreateAnalogInputVoltageChannelWithCustomScale() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiTask testee = new NiTask(mock, "task");

		testee.createAnalogInputVoltageChannelWithCustomScale("channel", "name", TerminalConfig.ConfigurationDefault, 0.0, 20.0, "wom");

		Mockito.verify(mock).DAQmxCreateAIVoltageChan(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.eq(0.0), Mockito.eq(20.0), Mockito.anyInt(), Mockito.any());
	}

	@Test
	public void testEnableDMA() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiTask testee = new NiTask(mock, "task");

		testee.enableDMA("task");

		Mockito.verify(mock).DAQmxSetAIDataXferMech(Mockito.any(), Mockito.any(), Mockito.anyInt());
	}

	@Test
	public void testReadAnalogF64() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final NiTask testee = new NiTask(mock, "task");

		testee.readAnalogF64(1000, null, 1024, null, FillMode.GroupByChannel);

		Mockito.verify(mock).DAQmxReadAnalogF64(Mockito.any(), Mockito.eq(1000), Mockito.eq(100.0), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.eq(null));
	}

	@Test
	public void testSaveToTDMs() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final File fileToStoreTo = Mockito.mock(File.class);
		final NiTask testee = new NiTask(mock, "task");

		testee.saveToTDMs(fileToStoreTo, FileMode.Create, LoggingMode.LogAndRead, "group-name");

		Mockito.verify(mock).DAQmxConfigureLogging(Mockito.any(), Mockito.any(), Mockito.eq(LoggingMode.LogAndRead.getApiValue()), Mockito.any(), Mockito.eq(FileMode.Create.getApiValue()));
	}

	@Test
	public void testSetSampleClockTiming() throws NiDacException {
		final DriverLibrary mock = Mockito.mock(DriverLibrary.class);
		final File fileToStoreTo = Mockito.mock(File.class);
		final NiTask testee = new NiTask(mock, "task");

		testee.setSampleClockTiming("source", 101.0, ClockTiming.Rising, SampleMode.Finite, 1025);

		Mockito.verify(mock).DAQmxCfgSampClkTiming(Mockito.any(), Mockito.any(), Mockito.eq(101.0), Mockito.eq(ClockTiming.Rising.getApiValue()), Mockito.eq(SampleMode.Finite.getApiValue()), Mockito.eq(1025l));
	}
}
