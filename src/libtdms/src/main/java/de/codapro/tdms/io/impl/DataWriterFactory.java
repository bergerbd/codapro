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

import java.io.RandomAccessFile;
import java.util.Optional;

import de.codapro.tdms.model.TDMsDataSegment;
import de.codapro.tdms.model.impl.InterleavedChannelProviderSegment;
import de.codapro.tdms.model.impl.ProviderSegment;
import de.codapro.tdms.model.impl.SingleChannelProviderSegment;

public final class DataWriterFactory {
	private DataWriterFactory() {
	}

	public static Optional<DataWriter> create(final TDMsDataSegment segment, final RandomAccessFile raf) {
		if(!(segment instanceof ProviderSegment)) {
			throw new IllegalArgumentException("Expected instance of TDMsProviderSegment, got " + segment.getClass().getName());
		}

		final ProviderSegment providerSegment = (ProviderSegment)segment;

		if(providerSegment instanceof SingleChannelProviderSegment) {
			return Optional.of(new SingleChannelDataWriter((SingleChannelProviderSegment)providerSegment, raf));
		}

		if(providerSegment instanceof InterleavedChannelProviderSegment) {
			return Optional.of(new InterleavedChannelDataWriter((InterleavedChannelProviderSegment)providerSegment, raf));
		}

		throw new IllegalArgumentException("Provider segment of type '" + segment.getClass().getName() + " is not yet supported.");
	}

	public static Optional<DataWriter> create(final TDMsDataSegment segment, final RandomAccessFile raf, final long dataCountPosition) {
		if(!(segment instanceof ProviderSegment)) {
			throw new IllegalArgumentException("Expected instance of TDMsProviderSegment, got " + segment.getClass().getName());
		}

		final Optional<DataWriter> writer = create(segment, raf);

		return Optional.of(new DataCountWriter(writer.get(), dataCountPosition));
	}
}
