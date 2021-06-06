/*
 * Copyright 2021 CoDaPro project. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package de.codapro.tdms.model;

import static org.junit.Assert.*;

import java.math.BigInteger;

import de.codapro.tdms.io.impl.TDMsVoid;
import org.junit.Test;

public class TypeFactoryTest {

	@Test
	public void testTypedValue() {
		assertEquals(Type.I16, TypeFactory.typeOf(new TypedValue(Type.I16, 0)));
	}

	@Test
	public void testNull() {
		assertEquals(Type.VOID, TypeFactory.typeOf(null));
	}

	@Test
	public void testVoid() {
		assertEquals(Type.VOID, TypeFactory.typeOf(TDMsVoid.INSTANCE));
	}

	@Test
	public void testString() {
		assertEquals(Type.STRING, TypeFactory.typeOf("Foobar"));
	}

	@Test
	public void testI64ofBigInteger() {
		assertEquals(Type.I64, TypeFactory.typeOf(BigInteger.valueOf(-10)));
	}

	@Test
	public void testU64() {
		assertEquals(Type.U64, TypeFactory.typeOf(BigInteger.valueOf(10)));
	}

	@Test
	public void testFloat() {
		assertEquals(Type.SINGLE_FLOAT, TypeFactory.typeOf(0.0f));
	}

	@Test
	public void testDouble() {
		assertEquals(Type.DOUBLE_FLOAT, TypeFactory.typeOf(0.0));
	}

	@Test
	public void testI64ofLong() {
		assertEquals(Type.I64, TypeFactory.typeOf(-1l));
	}

	@Test
	public void testI32() {
		assertEquals(Type.I32, TypeFactory.typeOf(-1));
	}

	@Test
	public void testI16() {
		assertEquals(Type.I16, TypeFactory.typeOf((short)-1));
	}

	@Test
	public void testI8() {
		assertEquals(Type.I8, TypeFactory.typeOf((byte)-1));
	}

	@Test
	public void testU32() {
		assertEquals(Type.U32, TypeFactory.typeOf(1l));
	}

	@Test
	public void testU16() {
		assertEquals(Type.U16, TypeFactory.typeOf(1));
	}

	@Test
	public void testU8() {
		assertEquals(Type.U8, TypeFactory.typeOf((short)1));
	}}
