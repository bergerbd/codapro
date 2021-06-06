package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType09 {

	@Test
	public void testReadOneSingleChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_09_single_one_segment.tdms"));
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

	@Test
	public void testReadTwoSingleChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_09_single_two_channels_one_segment.tdms"));
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

	@Test
	public void testReadOneSingleChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_09_single_three_segments.tdms"));
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
