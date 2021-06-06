package de.codapro.tdms.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;

public class TestReadType20 {

	@Test
	public void testReadOneStringChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_20_string_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("strings_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("string_group").get();
		assertNotNull(group);
		assertEquals("string_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("string_channel").get();
		assertNotNull(channel);
		assertEquals("string_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.STRING, channel.getType());
		assertEquals(10, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals("zero", iterator.next());
		assertEquals("one", iterator.next());
		assertEquals("two", iterator.next());
		assertEquals("three", iterator.next());
		assertEquals("four", iterator.next());
		assertEquals("five", iterator.next());
		assertEquals("six", iterator.next());
		assertEquals("seven", iterator.next());
		assertEquals("eight", iterator.next());
		assertEquals("nine", iterator.next());

		assertFalse(iterator.hasNext());

		reader.close();
	}

	@Test
	public void testReadTwoStringChannelInOneSegment() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_20_string_two_channels_one_segment.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("strings_two_channels_one_segment", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("string_group").get();
		assertNotNull(group);
		assertEquals("string_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channelA = group.getChannelByName("string_channel_a").get();
		assertNotNull(channelA);
		assertEquals("string_channel_a", channelA.getName());
		assertEquals(1l, channelA.getProperties().size());
		assertEquals(Type.STRING, channelA.getType());
		assertEquals(10, channelA.getNumberOfDataSets().get().intValue());

		final TDMsChannel channelB = group.getChannelByName("string_channel_b").get();
		assertNotNull(channelB);
		assertEquals("string_channel_b", channelB.getName());
		assertEquals(1l, channelB.getProperties().size());
		assertEquals(Type.STRING, channelB.getType());
		assertEquals(10, channelB.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iteratorA = channelA.iterator();

		assertEquals("a-zero", iteratorA.next());
		assertEquals("a-one", iteratorA.next());
		assertEquals("a-two", iteratorA.next());
		assertEquals("a-three", iteratorA.next());
		assertEquals("a-four", iteratorA.next());
		assertEquals("a-five", iteratorA.next());
		assertEquals("a-six", iteratorA.next());
		assertEquals("a-seven", iteratorA.next());
		assertEquals("a-eight", iteratorA.next());
		assertEquals("a-nine", iteratorA.next());

		assertFalse(iteratorA.hasNext());


		final Iterator<Object> iteratorB = channelB.iterator();

		assertEquals("b-zero", iteratorB.next());
		assertEquals("b-one", iteratorB.next());
		assertEquals("b-two", iteratorB.next());
		assertEquals("b-three", iteratorB.next());
		assertEquals("b-four", iteratorB.next());
		assertEquals("b-five", iteratorB.next());
		assertEquals("b-six", iteratorB.next());
		assertEquals("b-seven", iteratorB.next());
		assertEquals("b-eight", iteratorB.next());
		assertEquals("b-nine", iteratorB.next());

		assertFalse(iteratorB.hasNext());

		reader.close();
	}

	@Test
	public void testReadOneStringChannelAcrossThreeSegments() throws Exception {
		final TDMsReader reader = new TDMsReader(new File("src/test/resources/type_20_string_three_segments.tdms"));
		final TDMsFile file = reader.read();

		assertEquals(1l, file.getProperties().size());
		assertEquals("/", file.getName());
		assertEquals("type_20_string_three_segments", file.getProperties().get("name"));

		final TDMsGroup group = file.getGroupByName("string_group").get();
		assertNotNull(group);
		assertEquals("string_group", group.getName());
		assertTrue(group.getProperties().isEmpty());

		final TDMsChannel channel = group.getChannelByName("string_channel").get();
		assertNotNull(channel);
		assertEquals("string_channel", channel.getName());
		assertTrue(channel.getProperties().isEmpty());
		assertEquals(Type.STRING, channel.getType());
		assertEquals(30, channel.getNumberOfDataSets().get().intValue());

		final Iterator<Object> iterator = channel.iterator();

		assertEquals("zero", iterator.next());
		assertEquals("one", iterator.next());
		assertEquals("two", iterator.next());
		assertEquals("three", iterator.next());
		assertEquals("four", iterator.next());
		assertEquals("five", iterator.next());
		assertEquals("six", iterator.next());
		assertEquals("seven", iterator.next());
		assertEquals("eight", iterator.next());
		assertEquals("nine", iterator.next());
		assertEquals("ten", iterator.next());
		assertEquals("eleven", iterator.next());
		assertEquals("twelve", iterator.next());
		assertEquals("thirteen", iterator.next());
		assertEquals("fourteen", iterator.next());
		assertEquals("fifteen", iterator.next());
		assertEquals("sixteen", iterator.next());
		assertEquals("seventeen", iterator.next());
		assertEquals("eighteen", iterator.next());
		assertEquals("nineteen", iterator.next());
		assertEquals("twenty", iterator.next());
		assertEquals("twenty-one", iterator.next());
		assertEquals("twenty-two", iterator.next());
		assertEquals("twenty-three", iterator.next());
		assertEquals("twenty-four", iterator.next());
		assertEquals("twenty-five", iterator.next());
		assertEquals("twenty-six", iterator.next());
		assertEquals("twenty-seven", iterator.next());
		assertEquals("twenty-eight", iterator.next());
		assertEquals("twenty-nine", iterator.next());



		assertFalse(iterator.hasNext());

		reader.close();
	}
}
