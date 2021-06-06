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

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import de.codapro.niusb.api.ClockTiming;
import de.codapro.niusb.api.FillMode;
import de.codapro.niusb.api.NiDac;
import de.codapro.niusb.api.NiDacException;
import de.codapro.niusb.api.NiTask;
import de.codapro.niusb.api.SampleMode;
import de.codapro.niusb.api.TerminalConfig;

public class TestMain {
	private static boolean keepMeasuring = true;

	public static void main(final String... args) throws NiDacException {
		final NiDac dac = new NiDac();
		final NiTask task = dac.createTask("measurement");

		final int rate = 500_000;

		task.createAnalogInputVoltageChannel("Dev3/ai0:1", "Channel", TerminalConfig.ConfigurationDefault, -10.0, 10.0);
		task.setSampleClockTiming("", rate, ClockTiming.Rising, SampleMode.Continuous, rate / 6);

		task.start();
		while (keepMeasuring()) {
			read(task, rate);
		}

		task.stop();
		task.clear();
	}

	private static boolean keepMeasuring() {
		return keepMeasuring;
	}

	public static void setKeepMeasuring(final boolean newValue) {
		keepMeasuring = newValue;
	}

	@SuppressWarnings("squid:S106")
	private static void read(final NiTask task, final int rate) throws NiDacException {
		final int numberOfSamplesPerChannel = rate / 30; // one second of data for each channel
		final int inputBufferSize = numberOfSamplesPerChannel * 2; // in total we need space for two channelss
		final int read = 0;
		double[] buffer = new double[inputBufferSize];

		final DoubleBuffer inputBuffer = DoubleBuffer.wrap(buffer);
		final IntBuffer samplesPerChannelRead = IntBuffer.wrap(new int[] { read });

		task.readAnalogF64(numberOfSamplesPerChannel, inputBuffer, inputBufferSize, samplesPerChannelRead,
				FillMode.GroupByScanNumber);

		System.err.println("Read " + samplesPerChannelRead.get(0) + " of " + numberOfSamplesPerChannel + " values");
	}
}
