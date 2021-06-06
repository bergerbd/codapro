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

public class TestWriteReadType04 {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testReadOneInt64ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_04_int64_one_segment.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("int64_group");
				final TDMsChannel channel = group.create("int64_channel");

				file.addProperty("name", "type_04_int64_one_segment");
				channel.setType(Type.I64);
				channel.createDataSegment(new SingleChannelArrayProvider<Long>(Type.I64, new Long [] {-9_223_372_036_854_775_808L, -4_611_686_018_427_387_904L, 0L, 4_611_686_018_427_387_903L, 9_223_372_036_854_775_807L}));

				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_04_int64_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("int64_group").get();
			assertNotNull(group);
			assertEquals("int64_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("int64_channel").get();
			assertNotNull(channel);
			assertEquals("int64_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.I64, channel.getType());
			assertEquals(5, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(-9223372036854775808l, iterator.next());
			assertEquals(-4611686018427387904l, iterator.next());
			assertEquals(0l, iterator.next());
			assertEquals(4611686018427387903l, iterator.next());
			assertEquals(9223372036854775807l, iterator.next());
			assertFalse(iterator.hasNext());

			reader.close();
		}
	}

	@Test
	public void testReadTwoInt64ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_04_int64_two_channels_one_segment.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("int64_group");
				final TDMsChannel channelA = group.create("int64_channel_a");
				final TDMsChannel channelB = group.create("int64_channel_b");

				channelA.setType(Type.I64);
				channelA.createDataSegment(new SingleChannelArrayProvider<Long>(Type.I64, new Long [] {-9_223_372_036_854_775_808L, -4_611_686_018_427_387_904L, 0L, 4_611_686_018_427_387_903L, 9_223_372_036_854_775_807L}));

				channelB.setType(Type.I64);
				channelB.createDataSegment(new SingleChannelArrayProvider<Long>(Type.I64, new Long [] {9_223_372_036_854_775_807L, 4_611_686_018_427_387_903L, 0L, -4_611_686_018_427_387_904L, -9_223_372_036_854_775_808L}));

				file.addProperty("name", "type_04_int64_two_channels_one_segment");
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
			assertEquals("type_04_int64_two_channels_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("int64_group").get();
			assertNotNull(group);
			assertEquals("int64_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channelA = group.getChannelByName("int64_channel_a").get();
			assertNotNull(channelA);
			assertEquals("int64_channel_a", channelA.getName());
			assertEquals(1, channelA.getProperties().size());
			assertEquals(Type.I64, channelA.getType());
			assertEquals(5, channelA.getNumberOfDataSets().get().intValue());

			final TDMsChannel channelB = group.getChannelByName("int64_channel_b").get();
			assertNotNull(channelB);
			assertEquals("int64_channel_b", channelB.getName());
			assertEquals(1, channelB.getProperties().size());
			assertEquals(Type.I64, channelB.getType());
			assertEquals(5, channelB.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iteratorA = channelA.iterator();

			assertEquals(-9223372036854775808l, iteratorA.next());
			assertEquals(-4611686018427387904l, iteratorA.next());
			assertEquals(0l, iteratorA.next());
			assertEquals(4611686018427387903l, iteratorA.next());
			assertEquals(9223372036854775807l, iteratorA.next());
			assertFalse(iteratorA.hasNext());

			final Iterator<Object> iteratorB = channelB.iterator();

			assertEquals(9223372036854775807l, iteratorB.next());
			assertEquals(4611686018427387903l, iteratorB.next());
			assertEquals(0l, iteratorB.next());
			assertEquals(-4611686018427387904l, iteratorB.next());
			assertEquals(-9223372036854775808l, iteratorB.next());
			assertFalse(iteratorB.hasNext());

			reader.close();
		}
	}

	@Test
	public void testReadOneInt64ChannelAcrossThreeSegments() throws Exception {
		final File tdmsFile = testFolder.newFile("type_04_int64_three_segments.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("int64_group");
				final TDMsChannel channel = group.create("int64_channel");

				file.addProperty("name", "type_04_int64_three_segments");
				channel.setType(Type.I64);

				channel.createDataSegment(new SingleChannelArrayProvider<Long>(Type.I64, new Long [] {-9_223_372_036_854_775_808L, -4_611_686_018_427_387_904L, 0L, 4_611_686_018_427_387_903L, 9_223_372_036_854_775_807L}));
				channel.createDataSegment(new SingleChannelArrayProvider<Long>(Type.I64, new Long [] {-9_223_372_036_854_775_808L, -4_611_686_018_427_387_904L, 0L, 4_611_686_018_427_387_903L, 9_223_372_036_854_775_807L}));
				channel.createDataSegment(new SingleChannelArrayProvider<Long>(Type.I64, new Long [] {-9_223_372_036_854_775_808L, -4_611_686_018_427_387_904L, 0L, 4_611_686_018_427_387_903L, 9_223_372_036_854_775_807L}));
				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_04_int64_three_segments", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("int64_group").get();
			assertNotNull(group);
			assertEquals("int64_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("int64_channel").get();
			assertNotNull(channel);
			assertEquals("int64_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.I64, channel.getType());
			assertEquals(15, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(-9223372036854775808l, iterator.next());
			assertEquals(-4611686018427387904l, iterator.next());
			assertEquals(0l, iterator.next());
			assertEquals(4611686018427387903l, iterator.next());
			assertEquals(9223372036854775807l, iterator.next());
			assertEquals(-9223372036854775808l, iterator.next());
			assertEquals(-4611686018427387904l, iterator.next());
			assertEquals(0l, iterator.next());
			assertEquals(4611686018427387903l, iterator.next());
			assertEquals(9223372036854775807l, iterator.next());
			assertEquals(-9223372036854775808l, iterator.next());
			assertEquals(-4611686018427387904l, iterator.next());
			assertEquals(0l, iterator.next());
			assertEquals(4611686018427387903l, iterator.next());
			assertEquals(9223372036854775807l, iterator.next());

			assertFalse(iterator.hasNext());

			reader.close();
		}
	}
}
