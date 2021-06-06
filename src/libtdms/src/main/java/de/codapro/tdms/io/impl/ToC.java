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
package de.codapro.tdms.io.impl;

public enum ToC {
	/**
	 * Segment contains meta data.
	 */
	MetaData(1 << 1),

	/**
	 * Segment contains new object list (e.g. channels in this segment are
	 *   not the same channels the previous segment contains)
	 */
	NewObjectList(1 << 2),
	
	/**
	 * Segment contains raw data.
	 */
	RawData(1 << 3),

	/**
	 * Raw data in the segment is interleaved (if flag is not set, data is
	 *   contiguous)
	 */
	InterleavedData(1 << 5),

	/**
	 * All numeric values in the segment, including the lead in, raw data, and
	 *   meta data, are big-endian formatted (if flag is not set, data is
	 *   little-endian). ToC is not affected by endianess; it is always
	 *   little-endian.
	 */
	BigEndian(1 << 6),

	/**
	 * Segment contains DAQmx raw data.
	 */
	DAQMxRawData(1 << 7);

	private final int magicNumber;

	private ToC(final int magicNumber) {
		this.magicNumber = magicNumber;
	}

	public int getMagicNumber() {
		return magicNumber;
	}

	public boolean isSetIn(long toc) {
		return (toc & getMagicNumber()) != 0;
	}
}