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

import com.sun.jna.NativeLong;
import java.nio.ByteBuffer;

public class StringBuffer {
	private final byte[] buffer;

	public StringBuffer() {
		buffer = new byte[1024];
	}

	public StringBuffer(final int size) {
		buffer = new byte[size];
	}

	public ByteBuffer asBuffer() {
		return ByteBuffer.wrap(buffer);
	}

	public NativeLong size() {
		return new NativeLong(buffer.length);
	}

	@Override
	public String toString() {
		int terminatorIndex = 0;
		for (; terminatorIndex < buffer.length && buffer[terminatorIndex] != 0; ++terminatorIndex)
			;

		return new String(buffer, 0, terminatorIndex);
	}
}
