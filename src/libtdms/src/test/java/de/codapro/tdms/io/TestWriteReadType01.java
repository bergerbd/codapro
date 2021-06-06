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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.codapro.tdms.model.SingleChannelArrayProvider;
import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;
import de.codapro.tdms.model.TypedValue;

public class TestWriteReadType01 {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testReadOneInt8ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_01_int8_one_segment.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("int8_group");
				final TDMsChannel channel = group.create("int8_channel");

				file.addProperty("name", "type_01_int8_one_segment");
				channel.setType(Type.I8);
				channel.createDataSegment(new SingleChannelArrayProvider<Byte>(Type.I8, new Byte [] {-128, -64, 0, 63, 127}));

				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_01_int8_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("int8_group").get();
			assertNotNull(group);
			assertEquals("int8_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("int8_channel").get();
			assertNotNull(channel);
			assertEquals("int8_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.I8, channel.getType());
			assertEquals(5, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals((byte)-128, iterator.next());
			assertEquals((byte)-64, iterator.next());
			assertEquals((byte)-0, iterator.next());
			assertEquals((byte)63, iterator.next());
			assertEquals((byte)127, iterator.next());
			assertFalse(iterator.hasNext());

			reader.close();
		}
	}

	@Test
	public void testTwoInt8ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_01_int8_two_channels_one_segment.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("int8_group");
				final TDMsChannel channelA = group.create("int8_channel_a");
				final TDMsChannel channelB = group.create("int8_channel_b");

				channelA.setType(Type.I8);
				channelA.createDataSegment(new SingleChannelArrayProvider<Byte>(Type.I8, new Byte [] {-128, -64, 0, 63, 127}));

				channelB.setType(Type.I8);
				channelB.createDataSegment(new SingleChannelArrayProvider<Byte>(Type.I8, new Byte [] {127, 63, 0, -64, -128}));

				file.addProperty("name", "type_01_int8_two_channels_one_segment");
				channelA.addProperty("NI_ArrayColumn", new TypedValue(Type.I32, 0));
				channelB.addProperty("NI_ArrayColumn", new TypedValue(Type.I32, 1));

				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_01_int8_two_channels_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("int8_group").get();
			assertNotNull(group);
			assertEquals("int8_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channelA = group.getChannelByName("int8_channel_a").get();
			assertNotNull(channelA);
			assertEquals("int8_channel_a", channelA.getName());
			assertEquals(1, channelA.getProperties().size());
			assertEquals(Type.I8, channelA.getType());
			assertEquals(5, channelA.getNumberOfDataSets().get().intValue());

			final TDMsChannel channelB = group.getChannelByName("int8_channel_b").get();
			assertNotNull(channelB);
			assertEquals("int8_channel_b", channelB.getName());
			assertEquals(1, channelB.getProperties().size());
			assertEquals(Type.I8, channelB.getType());
			assertEquals(5, channelB.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iteratorA = channelA.iterator();

			assertEquals((byte)-128, iteratorA.next());
			assertEquals((byte)-64, iteratorA.next());
			assertEquals((byte)0, iteratorA.next());
			assertEquals((byte)63, iteratorA.next());
			assertEquals((byte)127, iteratorA.next());
			assertFalse(iteratorA.hasNext());

			final Iterator<Object> iteratorB = channelB.iterator();

			assertEquals((byte)127, iteratorB.next());
			assertEquals((byte)63, iteratorB.next());
			assertEquals((byte)0, iteratorB.next());
			assertEquals((byte)-64, iteratorB.next());
			assertEquals((byte)-128, iteratorB.next());
			assertFalse(iteratorB.hasNext());


			reader.close();
		}
	}

	@Test
	public void testReadOneInt8ChannelAcrossThreeSegments() throws Exception {
		final File tdmsFile = testFolder.newFile("type_01_int8_three_segments.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("int8_group");
				final TDMsChannel channel = group.create("int8_channel");

				file.addProperty("name", "type_01_int8_three_segments");
				channel.setType(Type.I8);

				channel.createDataSegment(new SingleChannelArrayProvider<Byte>(Type.I8, new Byte [] {-128, -64, 0, 63, 127}));
				channel.createDataSegment(new SingleChannelArrayProvider<Byte>(Type.I8, new Byte [] {-128, -64, 0, 63, 127}));
				channel.createDataSegment(new SingleChannelArrayProvider<Byte>(Type.I8, new Byte [] {-128, -64, 0, 63, 127}));
				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_01_int8_three_segments", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("int8_group").get();
			assertNotNull(group);
			assertEquals("int8_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("int8_channel").get();
			assertNotNull(channel);
			assertEquals("int8_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.I8, channel.getType());
			assertEquals(15, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals((byte)-128, iterator.next());
			assertEquals((byte)-64, iterator.next());
			assertEquals((byte)0, iterator.next());
			assertEquals((byte)63, iterator.next());
			assertEquals((byte)127, iterator.next());
			assertEquals((byte)-128, iterator.next());
			assertEquals((byte)-64, iterator.next());
			assertEquals((byte)0, iterator.next());
			assertEquals((byte)63, iterator.next());
			assertEquals((byte)127, iterator.next());
			assertEquals((byte)-128, iterator.next());
			assertEquals((byte)-64, iterator.next());
			assertEquals((byte)0, iterator.next());
			assertEquals((byte)63, iterator.next());
			assertEquals((byte)127, iterator.next());

			assertFalse(iterator.hasNext());

			reader.close();
		}
	}
}
