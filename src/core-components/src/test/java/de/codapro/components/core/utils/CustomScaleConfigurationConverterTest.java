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
package de.codapro.components.core.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import de.codapro.niusb.api.PreScaledUnits;

public class CustomScaleConfigurationConverterTest {

	@Test
	public void test() {
		final CustomScaleConfigurationConverter testee = new CustomScaleConfigurationConverter();
		final CustomScaleConfiguration result = testee.convert(CustomScaleConfiguration.class, "test;1.0;2.0;Volts;my units");

		assertNotNull(result);
		assertEquals("test", result.getName());
		assertEquals(1.0, result.getSlope(), Double.MIN_VALUE);
		assertEquals(2.0, result.getYIntercept(), Double.MIN_VALUE);
		assertEquals(PreScaledUnits.Volts, result.getPreScaledUnits());
		assertEquals("my units", result.getScaledUnits());
	}
}
