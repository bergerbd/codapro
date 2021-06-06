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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PreScaledUnitsTest {
	@Test
	public void testValueOf() {
		assertEquals(PreScaledUnits.Amperes, PreScaledUnits.valueOf("Amperes"));
		assertEquals(PreScaledUnits.Bar, PreScaledUnits.valueOf("Bar"));
		assertEquals(PreScaledUnits.Degrees, PreScaledUnits.valueOf("Degrees"));
		assertEquals(PreScaledUnits.DegreesCelsius, PreScaledUnits.valueOf("DegreesCelsius"));
		assertEquals(PreScaledUnits.DegreesFahrenheit, PreScaledUnits.valueOf("DegreesFahrenheit"));
		assertEquals(PreScaledUnits.DegreesRankine, PreScaledUnits.valueOf("DegreesRankine"));
		assertEquals(PreScaledUnits.FootPounds, PreScaledUnits.valueOf("FootPounds"));
		assertEquals(PreScaledUnits.G, PreScaledUnits.valueOf("G"));
		assertEquals(PreScaledUnits.Hertz, PreScaledUnits.valueOf("Hertz"));
		assertEquals(PreScaledUnits.Inches, PreScaledUnits.valueOf("Inches"));
		assertEquals(PreScaledUnits.InchOunces, PreScaledUnits.valueOf("InchOunces"));
		assertEquals(PreScaledUnits.InchPounds, PreScaledUnits.valueOf("InchPounds"));
		assertEquals(PreScaledUnits.Kelvins, PreScaledUnits.valueOf("Kelvins"));
		assertEquals(PreScaledUnits.KilogramForce, PreScaledUnits.valueOf("KilogramForce"));
		assertEquals(PreScaledUnits.Meters, PreScaledUnits.valueOf("Meters"));
		assertEquals(PreScaledUnits.MetersPerSecondSquared, PreScaledUnits.valueOf("MetersPerSecondSquared"));
		assertEquals(PreScaledUnits.MillivoltsPerVolt, PreScaledUnits.valueOf("MillivoltsPerVolt"));
		assertEquals(PreScaledUnits.NewtonMeters, PreScaledUnits.valueOf("NewtonMeters"));
		assertEquals(PreScaledUnits.Newtons, PreScaledUnits.valueOf("Newtons"));
		assertEquals(PreScaledUnits.Ohms, PreScaledUnits.valueOf("Ohms"));
		assertEquals(PreScaledUnits.Pascals, PreScaledUnits.valueOf("Pascals"));
		assertEquals(PreScaledUnits.Pounds, PreScaledUnits.valueOf("Pounds"));
		assertEquals(PreScaledUnits.PoundsPerSquareInch, PreScaledUnits.valueOf("PoundsPerSquareInch"));
		assertEquals(PreScaledUnits.Radians, PreScaledUnits.valueOf("Radians"));
		assertEquals(PreScaledUnits.Seconds, PreScaledUnits.valueOf("Seconds"));
		assertEquals(PreScaledUnits.Strain, PreScaledUnits.valueOf("Strain"));
		assertEquals(PreScaledUnits.TEDS, PreScaledUnits.valueOf("TEDS"));
		assertEquals(PreScaledUnits.Volts, PreScaledUnits.valueOf("Volts"));
		assertEquals(PreScaledUnits.VoltsPerVolt, PreScaledUnits.valueOf("VoltsPerVolt"));
	}

	@Test
	public void testValues() {
		assertEquals(29, PreScaledUnits.values().length);
	}

	@Test
	public void testGetApiValue() {
		PreScaledUnits.Amperes.getApiValue(); // just to please code coverage
	}
}
