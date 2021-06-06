package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType03 {

	@Test
	public void testReadOneInt32ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_03_int32_one_segment.tdms"));
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

	@Test
	public void testReadTwoInt32ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_03_int32_two_channels_one_segment.tdms"));
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

	@Test
	public void testReadOneInt32ChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_03_int32_three_segments.tdms"));
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
