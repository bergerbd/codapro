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

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import de.codapro.niusb.driver.DriverLibrary;
import de.codapro.niusb.internal.DataConverter;
import de.codapro.niusb.internal.ExceptionFactory;

import static de.codapro.niusb.internal.DataConverter.toBytes;

import java.io.File;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NiTask {

	/**
	 * Logger instance.
	 */
	private static final Logger log = LoggerFactory.getLogger(NiTask.class);

	/**
	 * The driver backend.
	 */
	private final DriverLibrary driver;

	/**
	 * The C task struct.
	 */
	private final Pointer handle;

	/**
	 * The exception factory.
	 */
	private ExceptionFactory factory;

	public NiTask(final DriverLibrary driver, final String name) throws NiDacException {
		this.driver = driver;

		this.factory = new ExceptionFactory(driver);

		final byte[] taskName = DataConverter.toBytes(name);
		final PointerByReference taskHandle = new PointerByReference();

		factory.check(driver.DAQmxCreateTask(taskName, taskHandle));

		this.handle = taskHandle.getValue();
	}

	public void clear() throws NiDacException {
		factory.check(driver.DAQmxClearTask(handle));
	}

	public void createAnalogInputVoltageChannel(final String physicalChannel, final String name, final TerminalConfig config, final double minValue, final double maxValue) throws NiDacException {
		factory.check(driver.DAQmxCreateAIVoltageChan(handle, toBytes(physicalChannel), toBytes(name), config.getApiValue(), minValue, maxValue, Scale.Volts.getApiValue(), null));
	}

	public void createAnalogInputVoltageChannelWithCustomScale(final String physicalChannel, final String name, final TerminalConfig config, final double minValue, final double maxValue, final String scaleName) throws NiDacException {
		factory.check(driver.DAQmxCreateAIVoltageChan(handle, toBytes(physicalChannel), toBytes(name), config.getApiValue(), minValue, maxValue, Scale.CustomScale.getApiValue(), toBytes(scaleName)));
	}

	public void enableDMA(final String name) throws NiDacException {
		factory.check(driver.DAQmxSetAIDataXferMech(handle, toBytes(name), TransferMode.DMA.getApiValue()));
	}

	public String getName() throws NiDacException {
		final StringBuffer nameBuffer = new StringBuffer();

		factory.check(driver.DAQmxGetTaskName(handle, nameBuffer.asBuffer(), nameBuffer.size()));

		return nameBuffer.toString();
	}

	public void readAnalogF64(final int numberOfSamplesPerChannel, final DoubleBuffer result, final int bufferSize, final IntBuffer samplesPerChannelRead, final FillMode mode) throws NiDacException {
		factory.check(driver.DAQmxReadAnalogF64(handle, numberOfSamplesPerChannel, 100.0, new NativeLong(mode.getApiValue()), result, new NativeLong(bufferSize), samplesPerChannelRead, null));
	}

	public void saveToTDMs(final File fileToStoreTo, final FileMode fileMode, final LoggingMode loggingMode, final String groupName) throws NiDacException {
		factory.check(driver.DAQmxConfigureLogging(handle, toBytes(fileToStoreTo.toString()), loggingMode.getApiValue(), toBytes(groupName), fileMode.getApiValue()));
	}

	public void setSampleClockTiming(final String source, final double rate, final ClockTiming timing, final SampleMode mode, final int count) throws NiDacException {
		factory.check(driver.DAQmxCfgSampClkTiming(handle, toBytes(source), rate, timing.getApiValue(), mode.getApiValue(), count));
	}

	public void start() throws NiDacException {
		factory.check(driver.DAQmxStartTask(handle));
	}

	public void stop() throws NiDacException {
		factory.check(driver.DAQmxStopTask(handle));
	}

	@Override
	public String toString() {
		try {
			return "NiTask [name=" + getName() + "]";
		} catch (final NiDacException e) {
			log.error("Failed to get name of task.", e);
			return "NiTask [name=<error>]";
		}
	}
}
