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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Java to C data conversions.
 */
public final class DataConverter {
	private DataConverter() {
	}

	public static byte[] toBytes(final String value) {
		if (value == null) {
			return new byte[0];
		}

		final CharsetEncoder enc = StandardCharsets.ISO_8859_1.newEncoder();

		int len = value.length();
		byte [] b = new byte[len + 1];
		final ByteBuffer bbuf = ByteBuffer.wrap(b);
		enc.encode(CharBuffer.wrap(value), bbuf, true);

		b[len] = 0;
		return b;
	}
}
