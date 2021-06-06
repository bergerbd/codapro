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

public class TestWriteReadType05 {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testOneUInt8ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_05_uint8_one_segment.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("uint8_group");
				final TDMsChannel channel = group.create("uint8_channel");

				file.addProperty("name", "type_05_uint8_one_segment");
				channel.setType(Type.U8);
				channel.createDataSegment(new SingleChannelArrayProvider<Short>(Type.U8, new Short[] {0, 1, 62, 127, 255}));

				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_05_uint8_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("uint8_group").get();
			assertNotNull(group);
			assertEquals("uint8_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("uint8_channel").get();
			assertNotNull(channel);
			assertEquals("uint8_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.U8, channel.getType());
			assertEquals(5, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals((short)0, iterator.next());
			assertEquals((short)1, iterator.next());
			assertEquals((short)62, iterator.next());
			assertEquals((short)127, iterator.next());
			assertEquals((short)255, iterator.next());
			assertFalse(iterator.hasNext());

			reader.close();
		}
	}

	@Test
	public void testTwoUInt8ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_05_uint8_two_channels_one_segment.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("uint8_group");
				final TDMsChannel channelA = group.create("uint8_channel_a");
				final TDMsChannel channelB = group.create("uint8_channel_b");

				channelA.setType(Type.U8);
				channelA.createDataSegment(new SingleChannelArrayProvider<Short>(Type.U8, new Short[] {0, 1, 62, 127, 255}));

				channelB.setType(Type.U8);
				channelB.createDataSegment(new SingleChannelArrayProvider<Short>(Type.U8, new Short[] {255, 127, 62, 1, 0}));

				file.addProperty("name", "type_05_uint8_two_channels_one_segment");
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
			assertEquals("type_05_uint8_two_channels_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("uint8_group").get();
			assertNotNull(group);
			assertEquals("uint8_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channelA = group.getChannelByName("uint8_channel_a").get();
			assertNotNull(channelA);
			assertEquals("uint8_channel_a", channelA.getName());
			assertEquals(1, channelA.getProperties().size());
			assertEquals(Type.U8, channelA.getType());
			assertEquals(5, channelA.getNumberOfDataSets().get().intValue());

			final TDMsChannel channelB = group.getChannelByName("uint8_channel_b").get();
			assertNotNull(channelB);
			assertEquals("uint8_channel_b", channelB.getName());
			assertEquals(1, channelB.getProperties().size());
			assertEquals(Type.U8, channelB.getType());
			assertEquals(5, channelB.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iteratorA = channelA.iterator();

			assertEquals((short)0, iteratorA.next());
			assertEquals((short)1, iteratorA.next());
			assertEquals((short)62, iteratorA.next());
			assertEquals((short)127, iteratorA.next());
			assertEquals((short)255, iteratorA.next());
			assertFalse(iteratorA.hasNext());

			final Iterator<Object> iteratorB = channelB.iterator();

			assertEquals((short)255, iteratorB.next());
			assertEquals((short)127, iteratorB.next());
			assertEquals((short)62, iteratorB.next());
			assertEquals((short)1, iteratorB.next());
			assertEquals((short)0, iteratorB.next());
			assertFalse(iteratorB.hasNext());

			reader.close();
		}
	}

	@Test
	public void testOneUInt8ChannelAcrossThreeSegments() throws Exception {
		final File tdmsFile = testFolder.newFile("type_05_uint8_three_segments.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("uint8_group");
				final TDMsChannel channel = group.create("uint8_channel");

				file.addProperty("name", "type_05_uint8_three_segments");
				channel.setType(Type.U8);

				channel.createDataSegment(new SingleChannelArrayProvider<Short>(Type.U8, new Short[] {0, 1, 62, 127, 255}));
				channel.createDataSegment(new SingleChannelArrayProvider<Short>(Type.U8, new Short[] {0, 1, 62, 127, 255}));
				channel.createDataSegment(new SingleChannelArrayProvider<Short>(Type.U8, new Short[] {0, 1, 62, 127, 255}));
				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_05_uint8_three_segments", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("uint8_group").get();
			assertNotNull(group);
			assertEquals("uint8_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("uint8_channel").get();
			assertNotNull(channel);
			assertEquals("uint8_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.U8, channel.getType());
			assertEquals(15, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals((short)0, iterator.next());
			assertEquals((short)1, iterator.next());
			assertEquals((short)62, iterator.next());
			assertEquals((short)127, iterator.next());
			assertEquals((short)255, iterator.next());
			assertEquals((short)0, iterator.next());
			assertEquals((short)1, iterator.next());
			assertEquals((short)62, iterator.next());
			assertEquals((short)127, iterator.next());
			assertEquals((short)255, iterator.next());
			assertEquals((short)0, iterator.next());
			assertEquals((short)1, iterator.next());
			assertEquals((short)62, iterator.next());
			assertEquals((short)127, iterator.next());
			assertEquals((short)255, iterator.next());

			assertFalse(iterator.hasNext());

			reader.close();
		}
	}
}
