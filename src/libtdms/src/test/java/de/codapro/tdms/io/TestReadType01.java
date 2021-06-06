package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType01 {

	@Test
	public void testReadOneInt8ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_01_int8_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_01_int8_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("int8_group").get();
		assertNotNull(group);
		assertEquals("int8_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("int8_channel").get();
		assertNotNull(channel);
		assertEquals("int8_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.I8, channel.getType());
		assertEquals(5, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals((byte)-128, iterator.next());
		assertEquals((byte)-64, iterator.next());
		assertEquals((byte)-0, iterator.next());
		assertEquals((byte)63, iterator.next());
		assertEquals((byte)127, iterator.next());
		assertFalse(iterator.hasNext());

		reader.close();
	}

	@Test
	public void testReadTwoInt8ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_01_int8_two_channels_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_01_int8_two_channels_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("int8_group").get();
		assertNotNull(group);
		assertEquals("int8_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channelA = group.getChannelByName("int8_channel_a").get();
		assertNotNull(channelA);
		assertEquals("int8_channel_a", channelA.getName());
		assertEquals(1, channelA.getProperties().size());
		assertEquals(Type.I8, channelA.getType());
		assertEquals(5, channelA.getNumberOfDataSets().get().intValue());

		final TDMsChannel channelB = group.getChannelByName("int8_channel_b").get();
		assertNotNull(channelB);
		assertEquals("int8_channel_b", channelB.getName());
		assertEquals(1, channelB.getProperties().size());
		assertEquals(Type.I8, channelB.getType());
		assertEquals(5, channelB.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iteratorA = channelA.iterator();

		assertEquals((byte)-128, iteratorA.next());
		assertEquals((byte)-64, iteratorA.next());
		assertEquals((byte)0, iteratorA.next());
		assertEquals((byte)63, iteratorA.next());
		assertEquals((byte)127, iteratorA.next());
		assertFalse(iteratorA.hasNext());


		final Iterator<Object> iteratorB = channelB.iterator();

		assertEquals((byte)127, iteratorB.next());
		assertEquals((byte)63, iteratorB.next());
		assertEquals((byte)0, iteratorB.next());
		assertEquals((byte)-64, iteratorB.next());
		assertEquals((byte)-128, iteratorB.next());
		assertFalse(iteratorB.hasNext());


		reader.close();
	}

	@Test
	public void testReadOneInt8ChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_01_int8_three_segments.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_01_int8_three_segments", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("int8_group").get();
		assertNotNull(group);
		assertEquals("int8_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("int8_channel").get();
		assertNotNull(channel);
		assertEquals("int8_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.I8, channel.getType());
		assertEquals(15, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals((byte)-128, iterator.next());
		assertEquals((byte)-64, iterator.next());
		assertEquals((byte)0, iterator.next());
		assertEquals((byte)63, iterator.next());
		assertEquals((byte)127, iterator.next());
		assertEquals((byte)-128, iterator.next());
		assertEquals((byte)-64, iterator.next());
		assertEquals((byte)0, iterator.next());
		assertEquals((byte)63, iterator.next());
		assertEquals((byte)127, iterator.next());
		assertEquals((byte)-128, iterator.next());
		assertEquals((byte)-64, iterator.next());
		assertEquals((byte)0, iterator.next());
		assertEquals((byte)63, iterator.next());
		assertEquals((byte)127, iterator.next());

		assertFalse(iterator.hasNext());

		reader.close();
	}
}
