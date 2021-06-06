package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.time.Instant;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType44 {

	@Test
	public void testReadOneStringChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_44_timestamp_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_44_timestamp_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("timestamp_group").get();
		assertNotNull(group);
		assertEquals("timestamp_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("timestamp_channel").get();
		assertNotNull(channel);
		assertEquals("timestamp_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.TIMESTAMP, channel.getType());
		assertEquals(3, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();
		assertEquals(Instant.parse("1904-01-01T00:00:00Z"), iterator.next());
		assertEquals(Instant.parse("2008-06-07T01:23:45Z"), iterator.next());
		assertEquals(Instant.parse("1894-03-15T13:23:45Z"), iterator.next());

		assertFalse(iterator.hasNext());

		reader.close();
	}

	@Test
	public void testReadTwoStringChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_44_timestamp_two_channels_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_44_timestamp_two_channels_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("timestamp_group").get();
		assertNotNull(group);
		assertEquals("timestamp_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channelA = group.getChannelByName("timestamp_channel_a").get();
		assertNotNull(channelA);
		assertEquals("timestamp_channel_a", channelA.getName());
		assertEquals(1l, channelA.getProperties().size());
		assertEquals(Type.TIMESTAMP, channelA.getType());
		assertEquals(3, channelA.getNumberOfDataSets().get().intValue());

		final TDMsChannel channelB = group.getChannelByName("timestamp_channel_b").get();
		assertNotNull(channelB);
		assertEquals("timestamp_channel_b", channelB.getName());
		assertEquals(1l, channelB.getProperties().size());
		assertEquals(Type.TIMESTAMP, channelB.getType());
		assertEquals(3, channelB.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iteratorA = channelA.iterator();
		assertEquals(Instant.parse("1904-01-01T00:00:00Z"), iteratorA.next());
		assertEquals(Instant.parse("2008-06-07T01:23:45Z"), iteratorA.next());
		assertEquals(Instant.parse("1894-03-15T13:23:45Z"), iteratorA.next());
		assertFalse(iteratorA.hasNext());


		final Iterator<Object> iteratorB = channelB.iterator();
		assertEquals(Instant.parse("1894-03-15T13:23:45Z"), iteratorB.next());
		assertEquals(Instant.parse("2008-06-07T01:23:45Z"), iteratorB.next());
		assertEquals(Instant.parse("1904-01-01T00:00:00Z"), iteratorB.next());
		assertFalse(iteratorB.hasNext());

		reader.close();
	}

	@Test
	public void testReadOneStringChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_44_timestamp_three_segments.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_44_timestamp_three_segments", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("timestamp_group").get();
		assertNotNull(group);
		assertEquals("timestamp_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("timestamp_channel").get();
		assertNotNull(channel);
		assertEquals("timestamp_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.TIMESTAMP, channel.getType());
		assertEquals(9, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();
		assertEquals(Instant.parse("1904-01-01T00:00:00Z"), iterator.next());
		assertEquals(Instant.parse("2008-06-07T01:23:45Z"), iterator.next());
		assertEquals(Instant.parse("1894-03-15T13:23:45Z"), iterator.next());

		assertEquals(Instant.parse("1904-01-01T00:00:00Z"), iterator.next());
		assertEquals(Instant.parse("2008-06-07T01:23:45Z"), iterator.next());
		assertEquals(Instant.parse("1894-03-15T13:23:45Z"), iterator.next());

		assertEquals(Instant.parse("1904-01-01T00:00:00Z"), iterator.next());
		assertEquals(Instant.parse("2008-06-07T01:23:45Z"), iterator.next());
		assertEquals(Instant.parse("1894-03-15T13:23:45Z"), iterator.next());

		assertFalse(iterator.hasNext());

		reader.close();
	}

}
