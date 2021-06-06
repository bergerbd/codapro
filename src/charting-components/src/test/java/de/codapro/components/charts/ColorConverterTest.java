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
package de.codapro.components.charts;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class ColorConverterTest {

	@Test
	public void testWhite() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.white, testee.convert(Color.class, "white"));
	}

	@Test
	public void testLightGray() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.lightGray, testee.convert(Color.class, "light-gray"));
	}

	@Test
	public void testDarkGray() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.darkGray, testee.convert(Color.class, "dark-gray"));

	}

	@Test
	public void testBlack() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.black, testee.convert(Color.class, "black"));

	}

	@Test
	public void testRed() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.red, testee.convert(Color.class, "red"));

	}

	@Test
	public void testPink() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.pink, testee.convert(Color.class, "pink"));
	}

	@Test
	public void testOrange() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.orange, testee.convert(Color.class, "orange"));
	}

	@Test
	public void testYellow() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.yellow, testee.convert(Color.class, "yellow"));

	}

	@Test
	public void testGreen() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.green, testee.convert(Color.class, "green"));

	}

	@Test
	public void testMagenta() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.magenta, testee.convert(Color.class, "magenta"));

	}

	@Test
	public void testCyan() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.cyan, testee.convert(Color.class, "cyan"));

	}

	@Test
	public void testBlue() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.blue, testee.convert(Color.class, "blue"));

	}

	@Test
	public void testOthers() {
		final ColorConverter testee = new ColorConverter();

		assertEquals(Color.red, testee.convert(Color.class, "0xFF0000"));

	}
}
