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

import static de.codapro.niusb.internal.DataConverter.toBytes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.codapro.niusb.driver.DriverLibrary;
import de.codapro.niusb.driver.NativeLibrary;
import de.codapro.niusb.internal.ExceptionFactory;

public class NiDac {
	/**
	 * Driver library.
	 */
	private final DriverLibrary driver;

	/**
	 * Logger instance.
	 */
	private static final Logger log = LoggerFactory.getLogger(NiDac.class);

	public NiDac() {
		this.driver = NativeLibrary.INSTANCE;
	}

	public NiDac(final DriverLibrary driver) {
		this.driver = driver;
	}

	public NiTask createTask(final String taskName) throws NiDacException {
		log.info("Creating task {}", taskName);
		return new NiTask(driver, taskName);
	}

	@Override
	public String toString() {
		return "NiDac []";
	}

	public void createScale(final String name, final double slope, final double yIntercept, final PreScaledUnits preScaledUnits, final String scaledUnits) throws NiDacException {
		log.info("Creating custom scale {}.", name);
		new ExceptionFactory(driver).check(driver.DAQmxCreateLinScale(toBytes(name), slope, yIntercept, preScaledUnits.getApiValue(), toBytes(scaledUnits)));
	}
}
