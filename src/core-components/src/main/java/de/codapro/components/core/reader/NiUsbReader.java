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
package de.codapro.components.core.reader;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import javax.inject.Named;

import org.slf4j.Logger;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnExecute;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;
import de.codapro.api.model.StreamHeader;
import de.codapro.components.core.utils.CustomScaleConfiguration;
import de.codapro.niusb.api.ClockTiming;
import de.codapro.niusb.api.FillMode;
import de.codapro.niusb.api.NiDac;
import de.codapro.niusb.api.NiDacException;
import de.codapro.niusb.api.NiTask;
import de.codapro.niusb.api.SampleMode;
import de.codapro.niusb.api.TerminalConfig;

@Component(name = "NiUsbReader", doc = "Component for reading a data streams from a National Instruments USB DAC.", sinks = {})
public class NiUsbReader {
	@Log
	private static Logger log;

	@Input(doc = "Channel specification for reading, e.g. Dev3/ai0:1", name = "channels")
	private String channels;

	@Input(doc = "Name of the columns. The first one will be 'Time'", name = "column-names")
	private String[] columns;

	@Input(doc = "The expected input max value", name = "max-value")
	private double maxValue;

	@Input(doc = "The expected input min value", name = "min-value")
	private double minValue;

	@Input(doc = "The number of analog input channels", name = "number-of-channels")
	private int numberOfChannels;

	@Input(doc = "The input sample rate", name = "sample-rate")
	private int sampleRate;

	@Input(doc = "The terminal configuration mode (ConfigurationDefault, Differential, NonReferencedSingleEnded, PseudoDifferential, ReferencedSingleEnded).", name = "terminal-configuration")
	private TerminalConfig terminalConfiguration = TerminalConfig.ConfigurationDefault;

	@Input(doc = "Custom scale configuration (only set when necessary).", name = "custom-scale")
	private CustomScaleConfiguration customScale = null;

	private boolean hasToTerminate = false;

	@OnInit
	public void init(final @Named("output-stream") Stream stream) {
		final StreamHeader header = stream.getHeader();

		header.add("Time");
		for (final String column : columns) {
			log.info("Adding column {}.", column);
			header.add(column);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				NiUsbReader.this.hasToTerminate = true;
			}
		});
	}

	@OnExecute
	public void read(final @Named("output-stream") Stream stream) throws ConversionException {
		log.info("Reading data from {} at {}. Expecting values between {} and {}.", channels, sampleRate, minValue, maxValue);
		try {
			final NiDac dac = new NiDac();
			final NiTask task = dac.createTask("measurement");

			if(customScale == null) {
				task.createAnalogInputVoltageChannel(channels, "channel", terminalConfiguration, minValue, maxValue);
			} else {
				dac.createScale(customScale.getName(), customScale.getSlope(), customScale.getYIntercept(), customScale.getPreScaledUnits(), customScale.getScaledUnits());
				task.createAnalogInputVoltageChannelWithCustomScale(channels, "channel", terminalConfiguration, minValue, maxValue, customScale.getName());
			}
			task.setSampleClockTiming("", sampleRate, ClockTiming.Rising, SampleMode.Continuous, sampleRate / 6);

			task.start();

			readData(task, stream);

			task.stop();
			task.clear();
		} catch (final NiDacException e) {
			log.error("Failed to read data.", e);
		}
	}

	private void readData(final NiTask task, final Stream stream) throws NiDacException, ConversionException {
		final int nos = numberOfChannels;
		final int numberOfSamplesPerChannel = sampleRate / 30; // one second of data for each channel
		final int inputBufferSize = numberOfSamplesPerChannel * nos; // in total we need space for nos channels
		final double timeInterval = 1.0 / sampleRate;
		double time = 0.0;

		double[] buffer = new double[inputBufferSize];

		final DoubleBuffer inputBuffer = DoubleBuffer.wrap(buffer);
		final IntBuffer samplesPerChannelRead = IntBuffer.wrap(new int[] { 0 });

		while(!hasToTerminate) {
			task.readAnalogF64(numberOfSamplesPerChannel, inputBuffer, inputBufferSize, samplesPerChannelRead, FillMode.GroupByScanNumber);

			int limit = samplesPerChannelRead.get(0) * nos;

			for (int i = 0; i < limit; i = i + 2) {
				final DataVector vector = new DataVector();

				vector.append(time).append(buffer[i]).append(buffer[i + 1]);

				stream.append(vector);
				time = time + timeInterval;
			}
		}
	}
}
