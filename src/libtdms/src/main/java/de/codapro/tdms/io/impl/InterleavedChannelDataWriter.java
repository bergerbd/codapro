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
import java.util.Optional;

import de.codapro.tdms.model.InterleavedChannelDataProvider;
import de.codapro.tdms.model.Type;
import de.codapro.tdms.model.impl.InterleavedChannelProviderSegment;

public class InterleavedChannelDataWriter extends DataWriter {

	private final InterleavedChannelDataProvider provider;
	private final InterleavedChannelProviderSegment segment;

	public InterleavedChannelDataWriter(final InterleavedChannelProviderSegment segment, final RandomAccessFile raf) {
		super(raf);

		this.segment = segment;
		this.provider = (InterleavedChannelDataProvider)segment.getProvider();
	}

	@Override
	public BigInteger execute() throws IOException {
		if(segment.getChannelIndex() != 0) {
			final Optional<BigInteger> segmentSize = segment.size();

			assert segmentSize.isPresent() : "The segment has an illegal state.";

			return segmentSize.get();
		}

		final int channelCount = provider.getChannelCount();

		long count = 0;
		while(provider.hasNext()) {
			final Object [] values = provider.next();

			for(int i = 0; i < channelCount; ++i) {

				final Type type = provider.getType(i);
				type.write(values[i], raf);
			}
			count = count + 1;
		}

		segment.setSize(count);
		segment.setClean();

		return BigInteger.valueOf(count);
	}

}
