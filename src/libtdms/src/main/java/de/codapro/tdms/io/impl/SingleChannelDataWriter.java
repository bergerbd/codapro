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
import java.io.RandomAccessFile;
import java.math.BigInteger;

import de.codapro.tdms.model.SingleChannelDataProvider;
import de.codapro.tdms.model.Type;
import de.codapro.tdms.model.impl.SingleChannelProviderSegment;

public class SingleChannelDataWriter extends DataWriter {

	private final SingleChannelDataProvider<?> provider;
	private SingleChannelProviderSegment segment;

	public SingleChannelDataWriter(final SingleChannelProviderSegment segment, final RandomAccessFile raf) {
		super(raf);

		this.segment = segment;
		this.provider = (SingleChannelDataProvider<?>)segment.getProvider();
	}

	@Override
	public BigInteger execute() throws IOException {
		final Type type = provider.getType();

		long count = 0;
		while(provider.hasNext()) {
			type.write(provider.next(), raf);
			count = count + 1;
		}

		segment.setClean();

		return BigInteger.valueOf(count);
	}

}
