package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType0a {

	@Test
	public void testReadOneDoubleChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_0a_double_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_0a_double_one_segment", file.getProperties().get("name"));

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

	@Test
	public void testReadOneDoubleChannelAcrossThreeSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_0a_double_three_segments.tdms"));
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

	@Test
	public void testReadTwoDoubleChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_0a_double_two_channels_one_segment.tdms"));
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
