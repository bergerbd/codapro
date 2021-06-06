package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType06 {

	@Test
	public void testReadOneUInt16ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_06_uint16_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_06_uint16_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("uint16_group").get();
		assertNotNull(group);
		assertEquals("uint16_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("uint16_channel").get();
		assertNotNull(channel);
		assertEquals("uint16_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.U16, channel.getType());
		assertEquals(5, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals(0, iterator.next());
		assertEquals(1, iterator.next());
		assertEquals(16383, iterator.next());
		assertEquals(32767, iterator.next());
		assertEquals(65535, iterator.next());
		assertFalse(iterator.hasNext());

		reader.close();
	}

	@Test
	public void testReadTwoUInt16ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_06_uint16_two_channels_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_06_uint16_two_channels_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("uint16_group").get();
		assertNotNull(group);
		assertEquals("uint16_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channelA = group.getChannelByName("uint16_channel_a").get();
		assertNotNull(channelA);
		assertEquals("uint16_channel_a", channelA.getName());
		assertEquals(1, channelA.getProperties().size());
		assertEquals(Type.U16, channelA.getType());
		assertEquals(5, channelA.getNumberOfDataSets().get().intValue());

		final TDMsChannel channelB = group.getChannelByName("uint16_channel_b").get();
		assertNotNull(channelB);
		assertEquals("uint16_channel_b", channelB.getName());
		assertEquals(1, channelB.getProperties().size());
		assertEquals(Type.U16, channelB.getType());
		assertEquals(5, channelB.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iteratorA = channelA.iterator();

		assertEquals(0x0000, iteratorA.next());
		assertEquals(0x0001, iteratorA.next());
		assertEquals(0x3FFF, iteratorA.next());
		assertEquals(0x7FFF, iteratorA.next());
		assertEquals(0xFFFF, iteratorA.next());
		assertFalse(iteratorA.hasNext());


		final Iterator<Object> iteratorB = channelB.iterator();

		assertEquals(0xFFFF, iteratorB.next());
		assertEquals(0x7FFF, iteratorB.next());
		assertEquals(0x3FFF, iteratorB.next());
		assertEquals(0x0001, iteratorB.next());
		assertEquals(0x0000, iteratorB.next());
		assertFalse(iteratorB.hasNext());


		reader.close();
	}

	@Test
	public void testReadOneUInt16ChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_06_uint16_three_segments.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_06_uint16_three_segments", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("uint16_group").get();
		assertNotNull(group);
		assertEquals("uint16_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("uint16_channel").get();
		assertNotNull(channel);
		assertEquals("uint16_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.U16, channel.getType());
		assertEquals(15, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals(0, iterator.next());
		assertEquals(1, iterator.next());
		assertEquals(16383, iterator.next());
		assertEquals(32767, iterator.next());
		assertEquals(65535, iterator.next());
		assertEquals(0, iterator.next());
		assertEquals(1, iterator.next());
		assertEquals(16383, iterator.next());
		assertEquals(32767, iterator.next());
		assertEquals(65535, iterator.next());
		assertEquals(0, iterator.next());
		assertEquals(1, iterator.next());
		assertEquals(16383, iterator.next());
		assertEquals(32767, iterator.next());
		assertEquals(65535, iterator.next());

		assertFalse(iterator.hasNext());

		reader.close();
	}
}
