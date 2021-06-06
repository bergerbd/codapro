package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType08 {

	@Test
	public void testReadOneUInt32ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_07_uint32_one_segment.tdms"));
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

	@Test
	public void testReadTwoUInt32ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_07_uint32_two_channels_one_segment.tdms"));
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

	@Test
	public void testReadOneUInt32ChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_07_uint32_three_segments.tdms"));
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
