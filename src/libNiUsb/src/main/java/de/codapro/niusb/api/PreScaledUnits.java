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

public enum PreScaledUnits {
	Volts(DriverLibrary.DAQmx_Val_Volts),
	Amperes(DriverLibrary.DAQmx_Val_Amps),
	DegreesFahrenheit(DriverLibrary.DAQmx_Val_DegF),
	DegreesCelsius(DriverLibrary.DAQmx_Val_DegC),
	DegreesRankine(DriverLibrary.DAQmx_Val_DegR),
	Kelvins(DriverLibrary.DAQmx_Val_Kelvins),
	Strain(DriverLibrary.DAQmx_Val_Strain),
	Ohms(DriverLibrary.DAQmx_Val_Ohms),
	Hertz(DriverLibrary.DAQmx_Val_Hz),
	Seconds(DriverLibrary.DAQmx_Val_Seconds),
	Meters(DriverLibrary.DAQmx_Val_Meters),
	Inches(DriverLibrary.DAQmx_Val_Inches),
	Degrees(DriverLibrary.DAQmx_Val_Degrees),
	Radians(DriverLibrary.DAQmx_Val_Radians),
	G(DriverLibrary.DAQmx_Val_g),
	MetersPerSecondSquared(DriverLibrary.DAQmx_Val_MetersPerSecondSquared),
	KilogramForce(DriverLibrary.DAQmx_Val_KilogramForce),
	Newtons(DriverLibrary.DAQmx_Val_Newtons),
	Pounds(DriverLibrary.DAQmx_Val_Pounds),
	PoundsPerSquareInch(DriverLibrary.DAQmx_Val_PoundsPerSquareInch),
	Bar(DriverLibrary.DAQmx_Val_Bar),
	Pascals(DriverLibrary.DAQmx_Val_Pascals),
	VoltsPerVolt(DriverLibrary.DAQmx_Val_VoltsPerVolt),
	MillivoltsPerVolt(DriverLibrary.DAQmx_Val_mVoltsPerVolt),
	NewtonMeters(DriverLibrary.DAQmx_Val_NewtonMeters),
	InchOunces(DriverLibrary.DAQmx_Val_InchOunces),
	InchPounds(DriverLibrary.DAQmx_Val_InchPounds),
	FootPounds(DriverLibrary.DAQmx_Val_FootPounds),
	TEDS(DriverLibrary.DAQmx_Val_FromTEDS);

	private final int apiValue;

	private PreScaledUnits(final int apiValue) {
		this.apiValue = apiValue;
	}

	public int getApiValue() {
		return apiValue;
	}
}
