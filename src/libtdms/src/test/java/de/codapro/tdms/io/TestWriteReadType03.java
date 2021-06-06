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

import de.codapro.tdms.model.InterleavedChannelArrayProvider;
import de.codapro.tdms.model.InterleavedChannelDataProvider;
import de.codapro.tdms.model.SingleChannelArrayProvider;
import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;
import de.codapro.tdms.model.TypedValue;

public class TestWriteReadType03 {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testOneInt32ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_03_int32_one_segment.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("int32_group");
				final TDMsChannel channel = group.create("int32_channel");

				file.addProperty("name", "type_03_int32_one_segment");
				channel.setType(Type.I32);
				channel.createDataSegment(new SingleChannelArrayProvider<Integer>(Type.I32, new Integer [] {-2147483648, -1073741824, 0, 1073741823, 2147483647}));

				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_03_int32_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("int32_group").get();
			assertNotNull(group);
			assertEquals("int32_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("int32_channel").get();
			assertNotNull(channel);
			assertEquals("int32_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.I32, channel.getType());
			assertEquals(5, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(-2147483648, iterator.next());
			assertEquals(-1073741824, iterator.next());
			assertEquals(0, iterator.next());
			assertEquals(1073741823, iterator.next());
			assertEquals(2147483647, iterator.next());
			assertFalse(iterator.hasNext());

			reader.close();
		}
	}

	@Test
	public void testTwoInt32ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_03_int32_two_channels_one_segment.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("int32_group");
				final TDMsChannel channelA = group.create("int32_channel_a");
				final TDMsChannel channelB = group.create("int32_channel_b");

				channelA.setType(Type.I32);
				channelA.createDataSegment(new SingleChannelArrayProvider<Integer>(Type.I32, new Integer [] {-2147483648, -1073741824, 0, 1073741823, 2147483647}));

				channelB.setType(Type.I32);
				channelB.createDataSegment(new SingleChannelArrayProvider<Integer>(Type.I32, new Integer [] {2147483647, 1073741823, 0, -1073741824, -2147483648}));

				file.addProperty("name", "type_03_int32_two_channels_one_segment");
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
			assertEquals("type_03_int32_two_channels_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("int32_group").get();
			assertNotNull(group);
			assertEquals("int32_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channelA = group.getChannelByName("int32_channel_a").get();
			assertNotNull(channelA);
			assertEquals("int32_channel_a", channelA.getName());
			assertEquals(1, channelA.getProperties().size());
			assertEquals(Type.I32, channelA.getType());
			assertEquals(5, channelA.getNumberOfDataSets().get().intValue());

			final TDMsChannel channelB = group.getChannelByName("int32_channel_b").get();
			assertNotNull(channelB);
			assertEquals("int32_channel_b", channelB.getName());
			assertEquals(1, channelB.getProperties().size());
			assertEquals(Type.I32, channelB.getType());
			assertEquals(5, channelB.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iteratorA = channelA.iterator();

			assertEquals(-2147483648, iteratorA.next());
			assertEquals(-1073741824, iteratorA.next());
			assertEquals(0, iteratorA.next());
			assertEquals(1073741823, iteratorA.next());
			assertEquals(2147483647, iteratorA.next());
			assertFalse(iteratorA.hasNext());

			final Iterator<Object> iteratorB = channelB.iterator();

			assertEquals(2147483647, iteratorB.next());
			assertEquals(1073741823, iteratorB.next());
			assertEquals(0, iteratorB.next());
			assertEquals(-1073741824, iteratorB.next());
			assertEquals(-2147483648, iteratorB.next());
			assertFalse(iteratorB.hasNext());

			reader.close();
		}
	}

	@Test
	public void testOneInt32ChannelAcrossThreeSegments() throws Exception {
		final File tdmsFile = testFolder.newFile("type_03_int32_three_segments.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("int32_group");
				final TDMsChannel channel = group.create("int32_channel");

				file.addProperty("name", "type_03_int32_three_segments");
				channel.setType(Type.I32);

				channel.createDataSegment(new SingleChannelArrayProvider<Integer>(Type.I32, new Integer [] {-2147483648, -1073741824, 0, 1073741823, 2147483647}));
				channel.createDataSegment(new SingleChannelArrayProvider<Integer>(Type.I32, new Integer [] {-2147483648, -1073741824, 0, 1073741823, 2147483647}));
				channel.createDataSegment(new SingleChannelArrayProvider<Integer>(Type.I32, new Integer [] {-2147483648, -1073741824, 0, 1073741823, 2147483647}));
				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_03_int32_three_segments", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("int32_group").get();
			assertNotNull(group);
			assertEquals("int32_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("int32_channel").get();
			assertNotNull(channel);
			assertEquals("int32_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.I32, channel.getType());
			assertEquals(15, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(-2147483648, iterator.next());
			assertEquals(-1073741824, iterator.next());
			assertEquals(0, iterator.next());
			assertEquals(1073741823, iterator.next());
			assertEquals(2147483647, iterator.next());
			assertEquals(-2147483648, iterator.next());
			assertEquals(-1073741824, iterator.next());
			assertEquals(0, iterator.next());
			assertEquals(1073741823, iterator.next());
			assertEquals(2147483647, iterator.next());
			assertEquals(-2147483648, iterator.next());
			assertEquals(-1073741824, iterator.next());
			assertEquals(0, iterator.next());
			assertEquals(1073741823, iterator.next());
			assertEquals(2147483647, iterator.next());

			assertFalse(iterator.hasNext());

			reader.close();
		}
	}

	@Test
	public void testTwoInt32ChannelInOneSegmentInterleaved() throws Exception {
		final File tdmsFile = testFolder.newFile("type_03_int32_two_channels_one_segment_interleaved.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("int32_group");
				final TDMsChannel channelA = group.create("int32_channel_a");
				final TDMsChannel channelB = group.create("int32_channel_b");

				channelA.setType(Type.I32);
				channelB.setType(Type.I32);

				file.addProperty("name", "type_03_int32_two_channels_one_segment_interleaved");
				channelA.addProperty("NI_ArrayColumn", new TypedValue(Type.I32, 0));
				channelB.addProperty("NI_ArrayColumn", new TypedValue(Type.I32, 1));

				final InterleavedChannelDataProvider provider = new InterleavedChannelArrayProvider<>(new Type [] {channelA.getType(), channelB.getType()}, new Integer [][] {new Integer [] {-2147483648, 2147483647}, new Integer [] {-1073741824, 1073741823}, new Integer [] {0, 0}, new Integer [] {1073741823, -1073741824}, new Integer [] {2147483647, -2147483648}});

				channelA.createDataSegment(provider, 0);
				channelB.createDataSegment(provider, 1);
				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_03_int32_two_channels_one_segment_interleaved", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("int32_group").get();
			assertNotNull(group);
			assertEquals("int32_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channelA = group.getChannelByName("int32_channel_a").get();
			assertNotNull(channelA);
			assertEquals("int32_channel_a", channelA.getName());
			assertEquals(1, channelA.getProperties().size());
			assertEquals(Type.I32, channelA.getType());
			assertEquals(5, channelA.getNumberOfDataSets().get().intValue());

			final TDMsChannel channelB = group.getChannelByName("int32_channel_b").get();
			assertNotNull(channelB);
			assertEquals("int32_channel_b", channelB.getName());
			assertEquals(1, channelB.getProperties().size());
			assertEquals(Type.I32, channelB.getType());
			assertEquals(5, channelB.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iteratorA = channelA.iterator();

			assertEquals(-2147483648, iteratorA.next());
			assertEquals(-1073741824, iteratorA.next());
			assertEquals(0, iteratorA.next());
			assertEquals(1073741823, iteratorA.next());
			assertEquals(2147483647, iteratorA.next());
			assertFalse(iteratorA.hasNext());

			final Iterator<Object> iteratorB = channelB.iterator();

			assertEquals(2147483647, iteratorB.next());
			assertEquals(1073741823, iteratorB.next());
			assertEquals(0, iteratorB.next());
			assertEquals(-1073741824, iteratorB.next());
			assertEquals(-2147483648, iteratorB.next());
			assertFalse(iteratorB.hasNext());

			reader.close();
		}
	}
}
