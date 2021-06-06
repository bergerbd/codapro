package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType05 {

	@Test
	public void testReadOneUInt8ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_05_uint8_one_segment.tdms"));
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

	@Test
	public void testReadTwoUInt8ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_05_uint8_two_channels_one_segment.tdms"));
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

	@Test
	public void testReadOneUInt8ChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_05_uint8_three_segments.tdms"));
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
