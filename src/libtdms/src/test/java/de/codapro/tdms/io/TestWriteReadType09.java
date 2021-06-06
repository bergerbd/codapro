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

public class TestWriteReadType09 {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();


	@Test
	public void testOneSingleChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_09_single_one_segment.tdms");

		{
			try (final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("single_group");
				final TDMsChannel channel = group.create("single_channel");

				file.addProperty("name", "type_09_single_one_segment");
				channel.setType(Type.SINGLE_FLOAT);
				channel.createDataSegment(new SingleChannelArrayProvider<Float>(Type.SINGLE_FLOAT, new Float[] { -2.02f, -1.01f, 0.0f, 1.01f, 2.02f }));

				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1l, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_09_single_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("single_group").get();
			assertNotNull(group);
			assertEquals("single_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("single_channel").get();
			assertNotNull(channel);
			assertEquals("single_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.SINGLE_FLOAT, channel.getType());
			assertEquals(5l, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(-2.02f, iterator.next());
			assertEquals(-1.01f, iterator.next());
			assertEquals(0.00f, iterator.next());
			assertEquals(1.01f, iterator.next());
			assertEquals(2.02f, iterator.next());
			assertFalse(iterator.hasNext());

			reader.close();
		}
	}

	@Test
	public void testTwoSingleChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_09_single_two_channels_one_segment.tdms");

		{
			try (final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("single_group");
				final TDMsChannel channelA = group.create("single_channel_a");
				final TDMsChannel channelB = group.create("single_channel_b");

				channelA.setType(Type.SINGLE_FLOAT);
				channelA.createDataSegment(new SingleChannelArrayProvider<Float>(Type.SINGLE_FLOAT, new Float[] { -2.02f, -1.01f, 0.0f, 1.01f, 2.02f }));

				channelB.setType(Type.SINGLE_FLOAT);
				channelB.createDataSegment(new SingleChannelArrayProvider<Float>(Type.SINGLE_FLOAT, new Float[] { 2.02f, 1.01f, 0.0f, -1.01f, -2.02f }));

				file.addProperty("name", "type_09_single_two_channels_one_segment");
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
			assertEquals("type_09_single_two_channels_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("single_group").get();
			assertNotNull(group);
			assertEquals("single_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channelA = group.getChannelByName("single_channel_a").get();
			assertNotNull(channelA);
			assertEquals("single_channel_a", channelA.getName());
			assertEquals(1l, channelA.getProperties().size());
			assertEquals(Type.SINGLE_FLOAT, channelA.getType());
			assertEquals(5l, channelA.getNumberOfDataSets().get().intValue());

			final TDMsChannel channelB = group.getChannelByName("single_channel_b").get();
			assertNotNull(channelB);
			assertEquals("single_channel_b", channelB.getName());
			assertEquals(1l, channelB.getProperties().size());
			assertEquals(Type.SINGLE_FLOAT, channelB.getType());
			assertEquals(5l, channelB.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iteratorA = channelA.iterator();
			assertEquals(-2.02f, iteratorA.next());
			assertEquals(-1.01f, iteratorA.next());
			assertEquals(0.00f, iteratorA.next());
			assertEquals(1.01f, iteratorA.next());
			assertEquals(2.02f, iteratorA.next());
			assertFalse(iteratorA.hasNext());

			final Iterator<Object> iteratorB = channelB.iterator();
			assertEquals(2.02f, iteratorB.next());
			assertEquals(1.01f, iteratorB.next());
			assertEquals(0.00f, iteratorB.next());
			assertEquals(-1.01f, iteratorB.next());
			assertEquals(-2.02f, iteratorB.next());
			assertFalse(iteratorB.hasNext());

			reader.close();
		}
	}

	@Test
	public void testOneSingleChannelAcrossThreeSegments() throws Exception {
		final File tdmsFile = testFolder.newFile("type_09_single_three_segments.tdms");

		{
			try (final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("single_group");
				final TDMsChannel channel = group.create("single_channel");

				file.addProperty("name", "type_09_single_three_segments");
				channel.setType(Type.SINGLE_FLOAT);

				channel.createDataSegment(new SingleChannelArrayProvider<Float>(Type.SINGLE_FLOAT, new Float[] { -2.02f, -1.01f, 0.0f, 1.01f, 2.02f }));
				channel.createDataSegment(new SingleChannelArrayProvider<Float>(Type.SINGLE_FLOAT, new Float[] { -2.02f, -1.01f, 0.0f, 1.01f, 2.02f }));
				channel.createDataSegment(new SingleChannelArrayProvider<Float>(Type.SINGLE_FLOAT, new Float[] { -2.02f, -1.01f, 0.0f, 1.01f, 2.02f }));
				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1l, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_09_single_three_segments", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("single_group").get();
			assertNotNull(group);
			assertEquals("single_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("single_channel").get();
			assertNotNull(channel);
			assertEquals("single_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.SINGLE_FLOAT, channel.getType());
			assertEquals(15l, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(-2.02f, iterator.next());
			assertEquals(-1.01f, iterator.next());
			assertEquals(0.00f, iterator.next());
			assertEquals(1.01f, iterator.next());
			assertEquals(2.02f, iterator.next());
			assertEquals(-2.02f, iterator.next());
			assertEquals(-1.01f, iterator.next());
			assertEquals(0.00f, iterator.next());
			assertEquals(1.01f, iterator.next());
			assertEquals(2.02f, iterator.next());
			assertEquals(-2.02f, iterator.next());
			assertEquals(-1.01f, iterator.next());
			assertEquals(0.00f, iterator.next());
			assertEquals(1.01f, iterator.next());
			assertEquals(2.02f, iterator.next());

			assertFalse(iterator.hasNext());

			reader.close();
		}
	}
}
