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

public class TestWriteReadType07 {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testOneUInt32ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_07_uint32_one_segment.tdms");

		{
			try (final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("uint32_group");
				final TDMsChannel channel = group.create("uint32_channel");

				file.addProperty("name", "type_07_uint32_one_segment");
				channel.setType(Type.U32);
				channel.createDataSegment(new SingleChannelArrayProvider<Long>(Type.U32, new Long[] { 0L, 1L, 1_073_741_823L, 2_147_483_647L, 4_294_967_295L }));

				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1l, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_07_uint32_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("uint32_group").get();
			assertNotNull(group);
			assertEquals("uint32_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("uint32_channel").get();
			assertNotNull(channel);
			assertEquals("uint32_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.U32, channel.getType());
			assertEquals(5l, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(0l, iterator.next());
			assertEquals(1l, iterator.next());
			assertEquals(1073741823l, iterator.next());
			assertEquals(2147483647l, iterator.next());
			assertEquals(4294967295l, iterator.next());
			assertFalse(iterator.hasNext());

			reader.close();
		}
	}

	@Test
	public void testTwoUInt32ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_07_uint32_two_channels_one_segment.tdms");

		{
			try (final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("uint32_group");
				final TDMsChannel channelA = group.create("uint32_channel_a");
				final TDMsChannel channelB = group.create("uint32_channel_b");

				channelA.setType(Type.U32);
				channelA.createDataSegment(new SingleChannelArrayProvider<Long>(Type.U32, new Long[] { 0L, 1L, 1_073_741_823L, 2_147_483_647L, 4_294_967_295L }));

				channelB.setType(Type.U32);
				channelB.createDataSegment(new SingleChannelArrayProvider<Long>(Type.U32, new Long[] { 4_294_967_295L, 2_147_483_647L, 1_073_741_823L, 1L, 0L }));

				file.addProperty("name", "type_07_uint32_two_channels_one_segment");
				channelA.addProperty("NI_ArrayColumn", new TypedValue(Type.I32, 0));
				channelB.addProperty("NI_ArrayColumn", new TypedValue(Type.I32, 1));

				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1l, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_07_uint32_two_channels_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("uint32_group").get();
			assertNotNull(group);
			assertEquals("uint32_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channelA = group.getChannelByName("uint32_channel_a").get();
			assertNotNull(channelA);
			assertEquals("uint32_channel_a", channelA.getName());
			assertEquals(1l, channelA.getProperties().size());
			assertEquals(Type.U32, channelA.getType());
			assertEquals(5l, channelA.getNumberOfDataSets().get().intValue());

			final TDMsChannel channelB = group.getChannelByName("uint32_channel_b").get();
			assertNotNull(channelB);
			assertEquals("uint32_channel_b", channelB.getName());
			assertEquals(1l, channelB.getProperties().size());
			assertEquals(Type.U32, channelB.getType());
			assertEquals(5l, channelB.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iteratorA = channelA.iterator();

			assertEquals(0l, iteratorA.next());
			assertEquals(1l, iteratorA.next());
			assertEquals(1073741823l, iteratorA.next());
			assertEquals(2147483647l, iteratorA.next());
			assertEquals(4294967295l, iteratorA.next());
			assertFalse(iteratorA.hasNext());

			final Iterator<Object> iteratorB = channelB.iterator();

			assertEquals(4294967295l, iteratorB.next());
			assertEquals(2147483647l, iteratorB.next());
			assertEquals(1073741823l, iteratorB.next());
			assertEquals(1l, iteratorB.next());
			assertEquals(0l, iteratorB.next());
			assertFalse(iteratorB.hasNext());

			reader.close();
		}
	}

	@Test
	public void testOneUInt32ChannelAcrossThreeSegments() throws Exception {
		final File tdmsFile = testFolder.newFile("type_07_uint32_three_segments.tdms");

		{
			try (final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("uint32_group");
				final TDMsChannel channel = group.create("uint32_channel");

				file.addProperty("name", "type_07_uint32_three_segments");
				channel.setType(Type.U32);

				channel.createDataSegment(new SingleChannelArrayProvider<Long>(Type.U32, new Long[] { 0L, 1L, 1_073_741_823L, 2_147_483_647L, 4_294_967_295L }));
				channel.createDataSegment(new SingleChannelArrayProvider<Long>(Type.U32, new Long[] { 0L, 1L, 1_073_741_823L, 2_147_483_647L, 4_294_967_295L }));
				channel.createDataSegment(new SingleChannelArrayProvider<Long>(Type.U32, new Long[] { 0L, 1L, 1_073_741_823L, 2_147_483_647L, 4_294_967_295L }));
				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1l, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_07_uint32_three_segments", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("uint32_group").get();
			assertNotNull(group);
			assertEquals("uint32_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("uint32_channel").get();
			assertNotNull(channel);
			assertEquals("uint32_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.U32, channel.getType());
			assertEquals(15l, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(0l, iterator.next());
			assertEquals(1l, iterator.next());
			assertEquals(1073741823l, iterator.next());
			assertEquals(2147483647l, iterator.next());
			assertEquals(4294967295l, iterator.next());
			assertEquals(0l, iterator.next());
			assertEquals(1l, iterator.next());
			assertEquals(1073741823l, iterator.next());
			assertEquals(2147483647l, iterator.next());
			assertEquals(4294967295l, iterator.next());
			assertEquals(0l, iterator.next());
			assertEquals(1l, iterator.next());
			assertEquals(1073741823l, iterator.next());
			assertEquals(2147483647l, iterator.next());
			assertEquals(4294967295l, iterator.next());

			assertFalse(iterator.hasNext());

			reader.close();
		}
	}
}
