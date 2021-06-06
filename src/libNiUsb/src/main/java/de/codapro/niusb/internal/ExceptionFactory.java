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

import de.codapro.niusb.api.NiDacException;
import de.codapro.niusb.api.StringBuffer;
import de.codapro.niusb.driver.DriverLibrary;

/**
 * Factory for creating exceptions if there is an errno set.
 */
public final class ExceptionFactory {
	private final DriverLibrary driver;

	public ExceptionFactory(final DriverLibrary driver) {
		this.driver = driver;
	}

	public void check(final int errno) throws NiDacException {
		if (errno == 0) {
			return;
		}

		final StringBuffer errorMessage = new StringBuffer(65535);
		final StringBuffer extendedErrorMessage = new StringBuffer(65535);

		driver.DAQmxGetErrorString(errno, errorMessage.asBuffer(), errorMessage.size());

		driver.DAQmxGetExtendedErrorInfo(extendedErrorMessage.asBuffer(),  extendedErrorMessage.size());

		throw new NiDacException(errorMessage.toString() + System.lineSeparator() + System.lineSeparator() + "Extended message is:"  + System.lineSeparator() + extendedErrorMessage.toString());
	}
}
