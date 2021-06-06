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
package de.codapro.components.core.utils.csv;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class LocaleConverterTest {

	@Test
	public void testEnglish() {
		final LocaleConverter testee = new LocaleConverter();

		assertEquals(Locale.ENGLISH, testee.convert(Locale.class, "english"));
	}

	@Test
	public void testFrench() {
		final LocaleConverter testee = new LocaleConverter();

		assertEquals(Locale.FRENCH, testee.convert(Locale.class, "french"));
	}

	@Test
	public void testGerman() {
		final LocaleConverter testee = new LocaleConverter();

		assertEquals(Locale.GERMAN, testee.convert(Locale.class, "german"));
	}

	@Test
	public void testItalian() {
		final LocaleConverter testee = new LocaleConverter();

		assertEquals(Locale.ITALIAN, testee.convert(Locale.class, "italian"));
	}

	@Test
	public void testJapanese() {
		final LocaleConverter testee = new LocaleConverter();

		assertEquals(Locale.JAPANESE, testee.convert(Locale.class, "japanese"));
	}

	@Test
	public void testKorean() {
		final LocaleConverter testee = new LocaleConverter();

		assertEquals(Locale.KOREAN, testee.convert(Locale.class, "korean"));
	}

	@Test
	public void testChinese() {
		final LocaleConverter testee = new LocaleConverter();

		assertEquals(Locale.CHINESE, testee.convert(Locale.class, "chinese"));
	}

	@Test
	public void testAmerican() {
		final LocaleConverter testee = new LocaleConverter();

		assertEquals(Locale.US, testee.convert(Locale.class, "american"));
	}

	@Test
	public void testDefault() {
		final LocaleConverter testee = new LocaleConverter();

		assertEquals(Locale.getDefault(), testee.convert(Locale.class, "foobar"));
	}

}
