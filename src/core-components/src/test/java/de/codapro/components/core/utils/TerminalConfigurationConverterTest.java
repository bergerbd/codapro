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

import org.junit.Test;
import static org.junit.Assert.assertEquals;


import de.codapro.niusb.api.TerminalConfig;

public class TerminalConfigurationConverterTest {

	@Test
	public void testConfigurationDefault() {
		final TerminalConfigurationConverter testee = new TerminalConfigurationConverter();

		assertEquals(TerminalConfig.ConfigurationDefault, testee.convert(TerminalConfig.class, "ConfigurationDefault"));
	}

	@Test
	public void testDifferential() {
		final TerminalConfigurationConverter testee = new TerminalConfigurationConverter();

		assertEquals(TerminalConfig.Differential, testee.convert(TerminalConfig.class, "Differential"));
	}

	@Test
	public void testNonReferencedSingleEnded() {
		final TerminalConfigurationConverter testee = new TerminalConfigurationConverter();

		assertEquals(TerminalConfig.NonReferencedSingleEnded, testee.convert(TerminalConfig.class, "NonReferencedSingleEnded"));
	}

	@Test
	public void testPseudoDifferential() {
		final TerminalConfigurationConverter testee = new TerminalConfigurationConverter();

		assertEquals(TerminalConfig.PseudoDifferential, testee.convert(TerminalConfig.class, "PseudoDifferential"));
	}

	@Test
	public void testReferencedSingleEnded() {
		final TerminalConfigurationConverter testee = new TerminalConfigurationConverter();

		assertEquals(TerminalConfig.ReferencedSingleEnded, testee.convert(TerminalConfig.class, "ReferencedSingleEnded"));
	}

}
