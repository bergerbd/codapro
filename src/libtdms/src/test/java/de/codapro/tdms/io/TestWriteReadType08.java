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
import java.math.BigInteger;
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

public class TestWriteReadType08 {
	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testOneUInt64ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_08_uint64_one_segment.tdms");

		{
			try (final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("uint64_group");
				final TDMsChannel channel = group.create("uint64_channel");

				file.addProperty("name", "type_08_uint64_one_segment");
				channel.setType(Type.U64);
				channel.createDataSegment(new SingleChannelArrayProvider<BigInteger>(Type.U64, new BigInteger[] { BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(4294967295l), new BigInteger("9223372036854775807", 10), new BigInteger("18446744073709551615", 10) }));

				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1l, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_08_uint64_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("uint64_group").get();
			assertNotNull(group);
			assertEquals("uint64_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("uint64_channel").get();
			assertNotNull(channel);
			assertEquals("uint64_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.U64, channel.getType());
			assertEquals(5l, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(BigInteger.ZERO, iterator.next());
			assertEquals(BigInteger.ONE, iterator.next());
			assertEquals(BigInteger.valueOf(4294967295l), iterator.next());
			assertEquals(new BigInteger("9223372036854775807", 10), iterator.next());
			assertEquals(new BigInteger("18446744073709551615", 10), iterator.next());
			assertFalse(iterator.hasNext());

			reader.close();
		}
	}

	@Test
	public void testTwoUInt64ChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_08_uint64_two_channels_one_segment.tdms");

		{
			try (final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("uint64_group");
				final TDMsChannel channelA = group.create("uint64_channel_a");
				final TDMsChannel channelB = group.create("uint64_channel_b");

				channelA.setType(Type.U64);
				channelA.createDataSegment(new SingleChannelArrayProvider<BigInteger>(Type.U64, new BigInteger[] { BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(4294967295l), new BigInteger("9223372036854775807", 10), new BigInteger("18446744073709551615", 10) }));

				channelB.setType(Type.U64);
				channelB.createDataSegment(new SingleChannelArrayProvider<BigInteger>(Type.U64, new BigInteger[] { new BigInteger("18446744073709551615", 10), new BigInteger("9223372036854775807", 10), BigInteger.valueOf(4294967295l), BigInteger.ONE, BigInteger.ZERO }));

				file.addProperty("name", "type_08_uint64_two_channels_one_segment");
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
			assertEquals("type_08_uint64_two_channels_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("uint64_group").get();
			assertNotNull(group);
			assertEquals("uint64_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channelA = group.getChannelByName("uint64_channel_a").get();
			assertNotNull(channelA);
			assertEquals("uint64_channel_a", channelA.getName());
			assertEquals(1l, channelA.getProperties().size());
			assertEquals(Type.U64, channelA.getType());
			assertEquals(5l, channelA.getNumberOfDataSets().get().intValue());

			final TDMsChannel channelB = group.getChannelByName("uint64_channel_b").get();
			assertNotNull(channelB);
			assertEquals("uint64_channel_b", channelB.getName());
			assertEquals(1l, channelB.getProperties().size());
			assertEquals(Type.U64, channelB.getType());
			assertEquals(5l, channelB.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iteratorA = channelA.iterator();

			assertEquals(BigInteger.ZERO, iteratorA.next());
			assertEquals(BigInteger.ONE, iteratorA.next());
			assertEquals(BigInteger.valueOf(4294967295l), iteratorA.next());
			assertEquals(new BigInteger("9223372036854775807", 10), iteratorA.next());
			assertEquals(new BigInteger("18446744073709551615", 10), iteratorA.next());
			assertFalse(iteratorA.hasNext());

			final Iterator<Object> iteratorB = channelB.iterator();

			assertEquals(new BigInteger("18446744073709551615", 10), iteratorB.next());
			assertEquals(new BigInteger("9223372036854775807", 10), iteratorB.next());
			assertEquals(BigInteger.valueOf(4294967295l), iteratorB.next());
			assertEquals(BigInteger.ONE, iteratorB.next());
			assertEquals(BigInteger.ZERO, iteratorB.next());
			assertFalse(iteratorB.hasNext());

			reader.close();
		}
	}

	@Test
	public void testOneUInt64ChannelAcrossThreeSegments() throws Exception {
		final File tdmsFile = testFolder.newFile("type_08_uint64_three_segments.tdms");

		{
			try (final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("uint64_group");
				final TDMsChannel channel = group.create("uint64_channel");

				file.addProperty("name", "type_08_uint64_three_segments");
				channel.setType(Type.U64);

				channel.createDataSegment(new SingleChannelArrayProvider<BigInteger>(Type.U64, new BigInteger[] { BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(4294967295l), new BigInteger("9223372036854775807", 10), new BigInteger("18446744073709551615", 10) }));
				channel.createDataSegment(new SingleChannelArrayProvider<BigInteger>(Type.U64, new BigInteger[] { BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(4294967295l), new BigInteger("9223372036854775807", 10), new BigInteger("18446744073709551615", 10) }));
				channel.createDataSegment(new SingleChannelArrayProvider<BigInteger>(Type.U64, new BigInteger[] { BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(4294967295l), new BigInteger("9223372036854775807", 10), new BigInteger("18446744073709551615", 10) }));
				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1l, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_08_uint64_three_segments", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("uint64_group").get();
			assertNotNull(group);
			assertEquals("uint64_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("uint64_channel").get();
			assertNotNull(channel);
			assertEquals("uint64_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.U64, channel.getType());
			assertEquals(15l, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(BigInteger.ZERO, iterator.next());
			assertEquals(BigInteger.ONE, iterator.next());
			assertEquals(BigInteger.valueOf(4294967295l), iterator.next());
			assertEquals(new BigInteger("9223372036854775807", 10), iterator.next());
			assertEquals(new BigInteger("18446744073709551615", 10), iterator.next());
			assertEquals(BigInteger.ZERO, iterator.next());
			assertEquals(BigInteger.ONE, iterator.next());
			assertEquals(BigInteger.valueOf(4294967295l), iterator.next());
			assertEquals(new BigInteger("9223372036854775807", 10), iterator.next());
			assertEquals(new BigInteger("18446744073709551615", 10), iterator.next());
			assertEquals(BigInteger.ZERO, iterator.next());
			assertEquals(BigInteger.ONE, iterator.next());
			assertEquals(BigInteger.valueOf(4294967295l), iterator.next());
			assertEquals(new BigInteger("9223372036854775807", 10), iterator.next());
			assertEquals(new BigInteger("18446744073709551615", 10), iterator.next());

			assertFalse(iterator.hasNext());

			reader.close();
		}
	}
}
