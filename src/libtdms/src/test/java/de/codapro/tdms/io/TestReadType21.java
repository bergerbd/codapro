package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType21 {

	@Test
	public void testReadOneStringChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_21_boolean_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_21_boolean_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("boolean_group").get();
		assertNotNull(group);
		assertEquals("boolean_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("boolean_channel").get();
		assertNotNull(channel);
		assertEquals("boolean_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.BOOLEAN, channel.getType());
		assertEquals(2, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals(Boolean.TRUE, iterator.next());
		assertEquals(Boolean.FALSE, iterator.next());

		assertFalse(iterator.hasNext());

		reader.close();
	}

	@Test
	public void testReadTwoStringChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_21_boolean_two_channels_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_21_boolean_two_channels_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("boolean_group").get();
		assertNotNull(group);
		assertEquals("boolean_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channelA = group.getChannelByName("boolean_channel_a").get();
		assertNotNull(channelA);
		assertEquals("boolean_channel_a", channelA.getName());
		assertEquals(1l, channelA.getProperties().size());
		assertEquals(Type.BOOLEAN, channelA.getType());
		assertEquals(2, channelA.getNumberOfDataSets().get().intValue());

		final TDMsChannel channelB = group.getChannelByName("boolean_channel_b").get();
		assertNotNull(channelB);
		assertEquals("boolean_channel_b", channelB.getName());
		assertEquals(1l, channelB.getProperties().size());
		assertEquals(Type.BOOLEAN, channelB.getType());
		assertEquals(2, channelB.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iteratorA = channelA.iterator();

		assertEquals(Boolean.TRUE, iteratorA.next());
		assertEquals(Boolean.FALSE, iteratorA.next());

		assertFalse(iteratorA.hasNext());


		final Iterator<Object> iteratorB = channelB.iterator();

		assertEquals(Boolean.FALSE, iteratorB.next());
		assertEquals(Boolean.TRUE, iteratorB.next());

		assertFalse(iteratorB.hasNext());

		reader.close();
	}

	@Test
	public void testReadOneStringChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_21_boolean_three_segments.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_21_boolean_three_segments", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("boolean_group").get();
		assertNotNull(group);
		assertEquals("boolean_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("boolean_channel").get();
		assertNotNull(channel);
		assertEquals("boolean_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.BOOLEAN, channel.getType());
		assertEquals(6, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals(Boolean.TRUE, iterator.next());
		assertEquals(Boolean.FALSE, iterator.next());
		assertEquals(Boolean.TRUE, iterator.next());
		assertEquals(Boolean.FALSE, iterator.next());
		assertEquals(Boolean.TRUE, iterator.next());
		assertEquals(Boolean.FALSE, iterator.next());

		assertFalse(iterator.hasNext());

		reader.close();
	}

}
