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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import de.codapro.niusb.api.ClockTiming;
import de.codapro.niusb.api.FileMode;
import de.codapro.niusb.api.LoggingMode;
import de.codapro.niusb.api.NiDac;
import de.codapro.niusb.api.NiDacException;
import de.codapro.niusb.api.NiTask;
import de.codapro.niusb.api.PreScaledUnits;
import de.codapro.niusb.api.SampleMode;
import de.codapro.niusb.api.TerminalConfig;
import de.codapro.niusb.driver.DriverLibrary;
import de.codapro.niusb.driver.NativeLibrary;

public class ReadDataToTDMs {
	private String channels;

	private final BufferedReader console;

	private File filename;
	private Double maxValue;
	private Double minValue;
	private String name;
	private final PrintStream out;
	private PreScaledUnits preScaledUnits;
	private Integer sampleRate;
	private String scaledUnits;
	private Double slope;
	private TerminalConfig terminalConfiguration;
	private boolean terminate = false;

	private Double yIntercept;

	private final DriverLibrary library;

	public ReadDataToTDMs(final InputStream input, final PrintStream output) {
		this(input, output, NativeLibrary.INSTANCE);
	}

	public ReadDataToTDMs(final InputStream input, final PrintStream output, final DriverLibrary library) {
		this.console = new BufferedReader(new InputStreamReader(input));
		this.out = output;
		this.library = library;
	}

	@SuppressWarnings("squid:S106")
	public static void main(final String ... args) throws NiDacException, IOException, InterruptedException {
		new ReadDataToTDMs(System.in, System.out).run();
	}

	public void run() throws IOException, NiDacException, InterruptedException {
		readParameters();

		final NiDac dac = new NiDac(library);
		final NiTask task = dac.createTask("Data Acquisition");

		if(name == null) {
			task.createAnalogInputVoltageChannel(channels, "channel", terminalConfiguration, minValue, maxValue);
		} else {
			dac.createScale(name, slope, yIntercept, preScaledUnits, scaledUnits);
			task.createAnalogInputVoltageChannelWithCustomScale(channels, "channel", terminalConfiguration, minValue, maxValue, name);
		}
		task.setSampleClockTiming("", sampleRate, ClockTiming.Rising, SampleMode.Continuous, sampleRate / 6);
		task.saveToTDMs(filename, FileMode.CreateOrReplace, LoggingMode.Log, "Measurement Group");

		final Thread shutdownHook = new Thread() {
			@Override
			public void run() {
				setTerminate(true);
			}
		};

		Runtime.getRuntime().addShutdownHook(shutdownHook);

		task.start();
		while(!terminate) {
			Thread.sleep(100);
		}

		Runtime.getRuntime().removeShutdownHook(shutdownHook);
		task.stop();
	}

	private void readChannelSpecification() throws IOException {
		out.println("Please specify the channel to read (e.g. Dev1/ai0:1):");

		channels = readOrDefault("Dev1/ai0:1");
	}

	private void readMaximumDataValue() throws IOException {
		out.println("Please specify the expected maximum data value (e.g. 10.0):");

		maxValue = Double.valueOf(readOrDefault("10.0"));
	}

	private void readMinimumDataValue() throws IOException {
		out.println("Please specify the expected minimum data value (e.g. -10.0):");

		minValue = Double.valueOf(readOrDefault("-10.0"));
	}

	private String readOrDefault(final String defaultValue) throws IOException {
		final String input = console.readLine();

		if(input.isEmpty()) {
			return defaultValue;
		}

		return input;
	}

	private void readOutput() throws IOException {
		out.println("Please specify the output file name to use (e.g. measurement.tdms):");
		filename = new File(readOrDefault("measurement.tdms"));
	}

	private void readParameters() throws IOException {
		readChannelSpecification();
		readTerminalConfiguration();
		readMinimumDataValue();
		readMaximumDataValue();
		readSampleRate();
		readScale();
		readOutput();
	}

	private void readSampleRate() throws IOException {
		out.println("Please specify the sample rate to use (e.g. 10000):");

		sampleRate = Integer.valueOf(readOrDefault("100000"));
	}

	private void readScale() throws IOException {
		out.println("Do you want to use a custom scale (y/n):");
		if(readOrDefault("y").charAt(0) != 'y') {
			return;
		}

		out.println("Please specify the scale name to use (e.g. CustomScale):");
		name = readOrDefault("CustomScale");

		out.println("Please specify the slope to use (e.g. -40.0):");
		slope = Double.valueOf(readOrDefault("-40.0"));

		out.println("Please specify the y intercept to use (e.g. 0.0):");
		yIntercept = Double.valueOf(readOrDefault("0.0"));

		out.println("Please specify the prescaled units to use (e.g. Volts):");
		preScaledUnits = PreScaledUnits.valueOf(readOrDefault("Volts"));

		out.println("Please specify the to scaled units to use (e.g. Newton):");
		scaledUnits = readOrDefault("Newton");
	}

	private void readTerminalConfiguration() throws IOException {
		out.println("Please specify the terminal configuration (e.g. Differential):");

		terminalConfiguration = TerminalConfig.valueOf(readOrDefault("Differential"));
	}

	public void setTerminate(final boolean terminate) {
		this.terminate = terminate;
	}
}
