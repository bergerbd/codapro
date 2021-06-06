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

import java.io.IOException;
import java.math.BigInteger;

import de.codapro.tdms.model.Type;

/**
 * Decorator for writing the number of data sets written to the data segment infos.
 */
public final class DataCountWriter extends DataWriter {

	/**
	 * Position where to write the data count.
	 */
	private final long dataCountPosition;

	/**
	 * The decorated object
	 */
	private final DataWriter decoratee;

	public DataCountWriter(final DataWriter decoratee, final long dataCountPosition) {
		super(decoratee.raf);
		this.decoratee = decoratee;
		this.dataCountPosition = dataCountPosition;
	}

	@Override
	public BigInteger execute() throws IOException {
		final BigInteger count = decoratee.execute();

		final long position = raf.getFilePointer();
		raf.seek(dataCountPosition);
		Type.U64.write(count, raf);
		raf.seek(position);

		return count;
	}
}
