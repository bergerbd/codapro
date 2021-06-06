package de.codapro.niusb.api;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileModeTest {

	@Test
	public void testValues() {
		assertEquals(4, FileMode.values().length);
	}

	@Test
	public void testValueOf() {
		assertEquals(FileMode.Open, FileMode.valueOf("Open"));
		assertEquals(FileMode.OpenOrCreate, FileMode.valueOf("OpenOrCreate"));
		assertEquals(FileMode.CreateOrReplace, FileMode.valueOf("CreateOrReplace"));
		assertEquals(FileMode.Create, FileMode.valueOf("Create"));
	}

	@Test
	public void testGetApiValue() {
		FileMode.Open.getApiValue(); // just to please code coverage
	}
}
