package de.codapro.tdms.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

public class TestWriteReadType0a {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testOneDoubleChannelInOneSegment() throws Exception {
		final String name = "type_0a_double_one_segment";
		final File tdmsFile = testFolder.newFile(name + ".tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("double_group");
				final TDMsChannel channel = group.create("double_channel");

				file.addProperty("name", name);
				channel.setType(Type.DOUBLE_FLOAT);
				channel.createDataSegment(new SingleChannelArrayProvider<Double>(Type.DOUBLE_FLOAT, new Double [] {-2.02, -1.01, 0.0, 1.01, 2.02}));

				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals(name, file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("double_group").get();
			assertNotNull(group);
			assertEquals("double_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("double_channel").get();
			assertNotNull(channel);
			assertEquals("double_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.DOUBLE_FLOAT, channel.getType());
			assertEquals(5, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(-2.02, iterator.next());
			assertEquals(-1.01, iterator.next());
			assertEquals(0.0, iterator.next());
			assertEquals(1.01, iterator.next());
			assertEquals(2.02, iterator.next());

			reader.close();
		}
	}

	@Test
	public void testTwoDoubleChannelInOneSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_0a_double_two_channels_one_segment.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("double_group");
				final TDMsChannel channelA = group.create("double_channel_a");
				final TDMsChannel channelB = group.create("double_channel_b");

				channelA.setType(Type.DOUBLE_FLOAT);
				channelA.createDataSegment(new SingleChannelArrayProvider<Double>(Type.DOUBLE_FLOAT, new Double [] {-2.02, -1.01, 0.0,  1.01,  2.02}));

				channelB.setType(Type.DOUBLE_FLOAT);
				channelB.createDataSegment(new SingleChannelArrayProvider<Double>(Type.DOUBLE_FLOAT, new Double [] { 2.02,  1.01, 0.0, -1.01, -2.02}));

				file.addProperty("name", "type_0a_double_two_channels_one_segment");
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
			assertEquals("type_0a_double_two_channels_one_segment", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("double_group").get();
			assertNotNull(group);
			assertEquals("double_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channelA = group.getChannelByName("double_channel_a").get();
			assertNotNull(channelA);
			assertEquals("double_channel_a", channelA.getName());
			assertEquals(1, channelA.getProperties().size());
			assertEquals(Type.DOUBLE_FLOAT, channelA.getType());
			assertEquals(5, channelA.getNumberOfDataSets().get().intValue());

			final TDMsChannel channelB = group.getChannelByName("double_channel_b").get();
			assertNotNull(channelB);
			assertEquals("double_channel_b", channelB.getName());
			assertEquals(1, channelB.getProperties().size());
			assertEquals(Type.DOUBLE_FLOAT, channelB.getType());
			assertEquals(5, channelB.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iteratorA = channelA.iterator();

			assertEquals(-2.02, iteratorA.next());
			assertEquals(-1.01, iteratorA.next());
			assertEquals(0.0, iteratorA.next());
			assertEquals(1.01, iteratorA.next());
			assertEquals(2.02, iteratorA.next());

			final Iterator<Object> iteratorB = channelB.iterator();

			assertEquals(2.02, iteratorB.next());
			assertEquals(1.01, iteratorB.next());
			assertEquals(0.0, iteratorB.next());
			assertEquals(-1.01, iteratorB.next());
			assertEquals(-2.02, iteratorB.next());

			reader.close();
		}
	}

	@Test
	public void testOneDoubleChannelAcrossThreeSegment() throws Exception {
		final File tdmsFile = testFolder.newFile("type_0a_double_three_segments.tdms");

		{
			try(final TDMsWriter writer = new TDMsWriter(tdmsFile)) {
				final TDMsFile file = writer.create();
				final TDMsGroup group = file.create("double_group");
				final TDMsChannel channel = group.create("double_channel");

				file.addProperty("name", "type_0a_double_three_segments");
				channel.setType(Type.DOUBLE_FLOAT);

				channel.createDataSegment(new SingleChannelArrayProvider<Double>(Type.DOUBLE_FLOAT, new Double [] {-2.02, -1.01, 0.0, 1.01, 2.02}));
				channel.createDataSegment(new SingleChannelArrayProvider<Double>(Type.DOUBLE_FLOAT, new Double [] {-2.02, -1.01, 0.0, 1.01, 2.02}));
				channel.createDataSegment(new SingleChannelArrayProvider<Double>(Type.DOUBLE_FLOAT, new Double [] {-2.02, -1.01, 0.0, 1.01, 2.02}));
				writer.write(file);
			}
		}

		{
			final TDMsReader reader = new TDMsReader(tdmsFile);
			final TDMsFile file = reader.read();

			assertEquals(1, file.getProperties().size());
			assertEquals("/", file.getName());
			assertEquals("type_0a_double_three_segments", file.getProperties().get("name"));

			final TDMsGroup group = file.getGroupByName("double_group").get();
			assertNotNull(group);
			assertEquals("double_group", group.getName());
			assertTrue(group.getProperties().isEmpty());

			final TDMsChannel channel = group.getChannelByName("double_channel").get();
			assertNotNull(channel);
			assertEquals("double_channel", channel.getName());
			assertTrue(channel.getProperties().isEmpty());
			assertEquals(Type.DOUBLE_FLOAT, channel.getType());
			assertEquals(15, channel.getNumberOfDataSets().get().intValue());

			final Iterator<Object> iterator = channel.iterator();

			assertEquals(-2.02, iterator.next());
			assertEquals(-1.01, iterator.next());
			assertEquals(0.0, iterator.next());
			assertEquals(1.01, iterator.next());
			assertEquals(2.02, iterator.next());
			assertEquals(-2.02, iterator.next());
			assertEquals(-1.01, iterator.next());
			assertEquals(0.0, iterator.next());
			assertEquals(1.01, iterator.next());
			assertEquals(2.02, iterator.next());
			assertEquals(-2.02, iterator.next());
			assertEquals(-1.01, iterator.next());
			assertEquals(0.0, iterator.next());
			assertEquals(1.01, iterator.next());
			assertEquals(2.02, iterator.next());

			reader.close();
		}
	}
}
