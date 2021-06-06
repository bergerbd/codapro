package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.math.BigInteger;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType07 {

	@Test
	public void testReadOneUInt64ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_08_uint64_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_08_uint64_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("uint64_group").get();
		assertNotNull(group);
		assertEquals("uint64_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("uint64_channel").get();
		assertNotNull(channel);
		assertEquals("uint64_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.U64, channel.getType());
		assertEquals(5l, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals(BigInteger.valueOf(0), iterator.next());
		assertEquals(BigInteger.valueOf(1), iterator.next());
		assertEquals(BigInteger.valueOf(4294967295l), iterator.next());
		assertEquals(new BigInteger("9223372036854775807", 10), iterator.next());
		assertEquals(new BigInteger("18446744073709551615", 10), iterator.next());
		assertFalse(iterator.hasNext());

		reader.close();
	}

	@Test
	public void testReadTwoUInt64ChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_08_uint64_two_channels_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_08_uint64_two_channels_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("uint64_group").get();
		assertNotNull(group);
		assertEquals("uint64_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channelA = group.getChannelByName("uint64_channel_a").get();
		assertNotNull(channelA);
		assertEquals("uint64_channel_a", channelA.getName());
		assertEquals(1l, channelA.getProperties().size());
		assertEquals(Type.U64, channelA.getType());
		assertEquals(5l, channelA.getNumberOfDataSets().get().intValue());

		final TDMsChannel channelB = group.getChannelByName("uint64_channel_b").get();
		assertNotNull(channelB);
		assertEquals("uint64_channel_b", channelB.getName());
		assertEquals(1l, channelB.getProperties().size());
		assertEquals(Type.U64, channelB.getType());
		assertEquals(5l, channelB.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iteratorA = channelA.iterator();

		assertEquals(BigInteger.valueOf(0), iteratorA.next());
		assertEquals(BigInteger.valueOf(1), iteratorA.next());
		assertEquals(BigInteger.valueOf(4294967295l), iteratorA.next());
		assertEquals(new BigInteger("9223372036854775807", 10), iteratorA.next());
		assertEquals(new BigInteger("18446744073709551615", 10), iteratorA.next());
		assertFalse(iteratorA.hasNext());


		final Iterator<Object> iteratorB = channelB.iterator();

		assertEquals(new BigInteger("18446744073709551615", 10), iteratorB.next());
		assertEquals(new BigInteger("9223372036854775807", 10), iteratorB.next());
		assertEquals(BigInteger.valueOf(4294967295l), iteratorB.next());
		assertEquals(BigInteger.valueOf(1), iteratorB.next());
		assertEquals(BigInteger.valueOf(0), iteratorB.next());
		assertFalse(iteratorB.hasNext());


		reader.close();
	}

	@Test
	public void testReadOneUInt64ChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_08_uint64_three_segments.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_08_uint64_three_segments", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("uint64_group").get();
		assertNotNull(group);
		assertEquals("uint64_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("uint64_channel").get();
		assertNotNull(channel);
		assertEquals("uint64_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.U64, channel.getType());
		assertEquals(15l, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals(BigInteger.valueOf(0), iterator.next());
		assertEquals(BigInteger.valueOf(1), iterator.next());
		assertEquals(BigInteger.valueOf(4294967295l), iterator.next());
		assertEquals(new BigInteger("9223372036854775807", 10), iterator.next());
		assertEquals(new BigInteger("18446744073709551615", 10), iterator.next());
		assertEquals(BigInteger.valueOf(0), iterator.next());
		assertEquals(BigInteger.valueOf(1), iterator.next());
		assertEquals(BigInteger.valueOf(4294967295l), iterator.next());
		assertEquals(new BigInteger("9223372036854775807", 10), iterator.next());
		assertEquals(new BigInteger("18446744073709551615", 10), iterator.next());
		assertEquals(BigInteger.valueOf(0), iterator.next());
		assertEquals(BigInteger.valueOf(1), iterator.next());
		assertEquals(BigInteger.valueOf(4294967295l), iterator.next());
		assertEquals(new BigInteger("9223372036854775807", 10), iterator.next());
		assertEquals(new BigInteger("18446744073709551615", 10), iterator.next());

		assertFalse(iterator.hasNext());

		reader.close();
	}
}
