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

import de.codapro.niusb.driver.DriverLibrary;

public enum TerminalConfig {
	ConfigurationDefault(DriverLibrary.DAQmx_Val_Cfg_Default),
	Differential(DriverLibrary.DAQmx_Val_Diff),
	NonReferencedSingleEnded(DriverLibrary.DAQmx_Val_NRSE),
	PseudoDifferential(DriverLibrary.DAQmx_Val_PseudoDiff),
	ReferencedSingleEnded(DriverLibrary.DAQmx_Val_RSE);

	private final int apiValue;

	private TerminalConfig(final int apiValue) {
		this.apiValue = apiValue;
	}

	int getApiValue() {
		return apiValue;
	}
}
