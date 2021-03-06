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

import static org.junit.Assert.*;

import org.junit.Test;

public class TerminalConfigTest {

	@Test
	public void testValues() {
		assertEquals(5, TerminalConfig.values().length);
	}

	@Test
	public void testValueOf() {
		assertEquals(TerminalConfig.ConfigurationDefault, TerminalConfig.valueOf("ConfigurationDefault"));
		assertEquals(TerminalConfig.Differential, TerminalConfig.valueOf("Differential"));
		assertEquals(TerminalConfig.NonReferencedSingleEnded, TerminalConfig.valueOf("NonReferencedSingleEnded"));
		assertEquals(TerminalConfig.PseudoDifferential, TerminalConfig.valueOf("PseudoDifferential"));
		assertEquals(TerminalConfig.ReferencedSingleEnded, TerminalConfig.valueOf("ReferencedSingleEnded"));
	}

	@Test
	public void testGetApiValue() {
		TerminalConfig.ConfigurationDefault.getApiValue(); // just to please code coverage
	}
}
