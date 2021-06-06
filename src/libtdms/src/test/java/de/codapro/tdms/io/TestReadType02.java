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
package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType02 {

	@Test
	public void testReadOneInt16ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_02_int16_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_02_int16_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("int16_group").get();
		assertNotNull(group);
		assertEquals("int16_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("int16_channel").get();
		assertNotNull(channel);
		assertEquals("int16_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.I16, channel.getType());
		assertEquals(5, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals((short)-32768, iterator.next());
		assertEquals((short)-16384, iterator.next());
		assertEquals((short)-0, iterator.next());
		assertEquals((short)16383, iterator.next());
		assertEquals((short)32767, iterator.next());
		assertFalse(iterator.hasNext());

		reader.close();
	}

	@Test
	public void testReadTwoInt16ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_02_int16_two_channels_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_02_int16_two_channels_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("int16_group").get();
		assertNotNull(group);
		assertEquals("int16_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channelA = group.getChannelByName("int16_channel_a").get();
		assertNotNull(channelA);
		assertEquals("int16_channel_a", channelA.getName());
		assertEquals(1, channelA.getProperties().size());
		assertEquals(Type.I16, channelA.getType());
		assertEquals(5, channelA.getNumberOfDataSets().get().intValue());

		final TDMsChannel channelB = group.getChannelByName("int16_channel_b").get();
		assertNotNull(channelB);
		assertEquals("int16_channel_b", channelB.getName());
		assertEquals(1, channelB.getProperties().size());
		assertEquals(Type.I16, channelB.getType());
		assertEquals(5, channelB.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iteratorA = channelA.iterator();

		assertEquals((short)-32768, iteratorA.next());
		assertEquals((short)-16384, iteratorA.next());
		assertEquals((short)0, iteratorA.next());
		assertEquals((short)16383, iteratorA.next());
		assertEquals((short)32767, iteratorA.next());
		assertFalse(iteratorA.hasNext());


		final Iterator<Object> iteratorB = channelB.iterator();

		assertEquals((short)32767, iteratorB.next());
		assertEquals((short)16383, iteratorB.next());
		assertEquals((short)0, iteratorB.next());
		assertEquals((short)-16384, iteratorB.next());
		assertEquals((short)-32768, iteratorB.next());
		assertFalse(iteratorB.hasNext());


		reader.close();
	}

	@Test
	public void testReadOneInt16ChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_02_int16_three_segments.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_02_int16_three_segments", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("int16_group").get();
		assertNotNull(group);
		assertEquals("int16_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("int16_channel").get();
		assertNotNull(channel);
		assertEquals("int16_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.I16, channel.getType());
		assertEquals(15, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals((short)-32768, iterator.next());
		assertEquals((short)-16384, iterator.next());
		assertEquals((short)0, iterator.next());
		assertEquals((short)16383, iterator.next());
		assertEquals((short)32767, iterator.next());
		assertEquals((short)-32768, iterator.next());
		assertEquals((short)-16384, iterator.next());
		assertEquals((short)0, iterator.next());
		assertEquals((short)16383, iterator.next());
		assertEquals((short)32767, iterator.next());
		assertEquals((short)-32768, iterator.next());
		assertEquals((short)-16384, iterator.next());
		assertEquals((short)0, iterator.next());
		assertEquals((short)16383, iterator.next());
		assertEquals((short)32767, iterator.next());

		assertFalse(iterator.hasNext());

		reader.close();
	}
}
