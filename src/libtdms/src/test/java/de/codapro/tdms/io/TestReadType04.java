package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType04 {

	@Test
	public void testReadOneInt64ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_04_int64_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_04_int64_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("int64_group").get();
		assertNotNull(group);
		assertEquals("int64_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("int64_channel").get();
		assertNotNull(channel);
		assertEquals("int64_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.I64, channel.getType());
		assertEquals(5, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals(-9223372036854775808l, iterator.next());
		assertEquals(-4611686018427387904l, iterator.next());
		assertEquals(0l, iterator.next());
		assertEquals(4611686018427387903l, iterator.next());
		assertEquals(9223372036854775807l, iterator.next());
		assertFalse(iterator.hasNext());

		reader.close();
	}

	@Test
	public void testReadTwoInt64ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_04_int64_two_channels_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_04_int64_two_channels_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("int64_group").get();
		assertNotNull(group);
		assertEquals("int64_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channelA = group.getChannelByName("int64_channel_a").get();
		assertNotNull(channelA);
		assertEquals("int64_channel_a", channelA.getName());
		assertEquals(1, channelA.getProperties().size());
		assertEquals(Type.I64, channelA.getType());
		assertEquals(5, channelA.getNumberOfDataSets().get().intValue());

		final TDMsChannel channelB = group.getChannelByName("int64_channel_b").get();
		assertNotNull(channelB);
		assertEquals("int64_channel_b", channelB.getName());
		assertEquals(1, channelB.getProperties().size());
		assertEquals(Type.I64, channelB.getType());
		assertEquals(5, channelB.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iteratorA = channelA.iterator();

		assertEquals(-9223372036854775808l, iteratorA.next());
		assertEquals(-4611686018427387904l, iteratorA.next());
		assertEquals(0l, iteratorA.next());
		assertEquals(4611686018427387903l, iteratorA.next());
		assertEquals(9223372036854775807l, iteratorA.next());
		assertFalse(iteratorA.hasNext());


		final Iterator<Object> iteratorB = channelB.iterator();

		assertEquals(9223372036854775807l, iteratorB.next());
		assertEquals(4611686018427387903l, iteratorB.next());
		assertEquals(0l, iteratorB.next());
		assertEquals(-4611686018427387904l, iteratorB.next());
		assertEquals(-9223372036854775808l, iteratorB.next());
		assertFalse(iteratorB.hasNext());


		reader.close();
	}

	@Test
	public void testReadOneInt64ChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_04_int64_three_segments.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_04_int64_three_segments", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("int64_group").get();
		assertNotNull(group);
		assertEquals("int64_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("int64_channel").get();
		assertNotNull(channel);
		assertEquals("int64_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.I64, channel.getType());
		assertEquals(15, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals(-9223372036854775808l, iterator.next());
		assertEquals(-4611686018427387904l, iterator.next());
		assertEquals(0l, iterator.next());
		assertEquals(4611686018427387903l, iterator.next());
		assertEquals(9223372036854775807l, iterator.next());
		assertEquals(-9223372036854775808l, iterator.next());
		assertEquals(-4611686018427387904l, iterator.next());
		assertEquals(0l, iterator.next());
		assertEquals(4611686018427387903l, iterator.next());
		assertEquals(9223372036854775807l, iterator.next());
		assertEquals(-9223372036854775808l, iterator.next());
		assertEquals(-4611686018427387904l, iterator.next());
		assertEquals(0l, iterator.next());
		assertEquals(4611686018427387903l, iterator.next());
		assertEquals(9223372036854775807l, iterator.next());

		assertFalse(iterator.hasNext());

		reader.close();
	}
}
