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
package de.codapro.tdms.model.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

import de.codapro.tdms.model.SingleChannelDataProvider;
import de.codapro.tdms.model.Type;

public class SingleChannelProviderSegmentTest {

	@Test
	public void testConstructor() {
		final SingleChannelDataProvider<Object> provider = Mockito.mock(SingleChannelDataProvider.class);

		final SingleChannelProviderSegment<Object> testee = new SingleChannelProviderSegment<>(provider);

		assertEquals(provider, testee.getProvider());
	}


	@Test
	public void testGetType() {
		final SingleChannelDataProvider<Object> provider = Mockito.mock(SingleChannelDataProvider.class);
		Mockito.when(provider.getType()).thenReturn(Type.DA_QMX_RAW_DATA);

		final SingleChannelProviderSegment<Object> testee = new SingleChannelProviderSegment<>(provider);

		assertEquals(Type.DA_QMX_RAW_DATA, testee.getType());

		Mockito.verify(provider).getType();
	}
}
