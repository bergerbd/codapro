package de.codapro.niusb.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClockTimingTest {
	@Test
	public void testValueOf() {
		assertEquals(ClockTiming.Falling, ClockTiming.valueOf("Falling"));
		assertEquals(ClockTiming.Rising, ClockTiming.valueOf("Rising"));
	}

	@Test
	public void testValues() {
		assertEquals(2, ClockTiming.values().length);
	}

	@Test
	public void testGetApiValue() {
		ClockTiming.Falling.getApiValue(); // just to please code coverage
	}
}
