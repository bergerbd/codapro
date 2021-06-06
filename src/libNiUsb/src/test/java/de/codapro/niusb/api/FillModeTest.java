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

public class FillModeTest {
	@Test
	public void testValueOf() {
		assertEquals(FillMode.GroupByChannel, FillMode.valueOf("GroupByChannel"));
		assertEquals(FillMode.GroupByScanNumber, FillMode.valueOf("GroupByScanNumber"));
	}

	@Test
	public void testValues() {
		assertEquals(2, FillMode.values().length);
	}

	@Test
	public void testGetApiValue() {
		FillMode.GroupByChannel.getApiValue(); // just to please code coverage
	}
}
