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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.function.Function;

import de.codapro.tdms.model.impl.ThrowingBiConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

    /**
 * Enumeration listing all data types known in the TDMs file format.
 */
public enum Type {
	BOOLEAN(0x21, 1),
	COMPLEX_DOUBLE_FLOAT(0x08000d, -1),
	COMPLEX_SINGLE_FLOAT(0x08000c, -1),
	DA_QMX_RAW_DATA(0xFFFFFFFFL, -1),
	DOUBLE_FLOAT(0x0A, 8),
	DOUBLE_FLOAT_WITH_UNIT(0x1A, 8),
	EXTENDED_FLOAT(0x0B, -1),
	EXTENDED_FLOAT_WITH_UNIT(0x1B, -1),
	FIXED_POINT(0x4F, -1),
	I16(0x02, 2),
	I32(0x03, 4),
	I64(0x04, 8),
	I8(0x01, 1),
	SINGLE_FLOAT(0x09, 4),
	SINGLE_FLOAT_WITH_UNIT(0x19, -1),
	STRING(0x20, -1),
	TIMESTAMP(0x44, 16),
	U16(0x06, 2),
	U32(0x07, 4),
	U64(0x08, 8),
	U8(0x05, 1),
	VOID(0x00, 0);

	/**
	 * Logger instance.
	 */
	private static final Logger log = LoggerFactory.getLogger(Type.class);

	/**
	 * This magic number is the time epoch difference between Java's reference
	 *   time point (1970-01-01 00:00:00.0) and TDMs' reference time point
	 *   (1904-01-01 00:00:00.0).
	 */
	private static final long EPOCHE_DIFFERENCE_IN_SECONDS = 2_082_844_800l;

	private static boolean readBoolean(final ByteBuffer buffer) {
		return buffer.get() != 0;
	}

	private static double readDouble(final ByteBuffer buffer) {
		final byte [] bytes = readBytes(buffer, 8);

		return ByteBuffer.wrap(bytes).getDouble();
	}

	@SuppressWarnings("squid:S1172")
	private static Object readVoid(final ByteBuffer buffer) {
		return null;
	}

	private static byte[] readBytes(final ByteBuffer buffer, final int count) {
		final byte [] array = new byte[count];

		for(int i = count - 1; i >= 0; --i) {
			array[i] = buffer.get();
		}

		return array;
	}

	private static short readI16(final ByteBuffer buffer) {
		final byte [] array = readBytes(buffer, 2);

		return ByteBuffer.wrap(array).getShort();
	}

	private static int readI32(final ByteBuffer buffer) {
		final byte [] array = readBytes(buffer, 4);

		return ByteBuffer.wrap(array).getInt();
	}

	private static long readI64(final ByteBuffer buffer) {
		final byte [] array = readBytes(buffer, 8);

		return ByteBuffer.wrap(array).getLong();
	}

	private static byte readI8(final ByteBuffer buffer) {
		return buffer.get();
	}

	private static float readSingle(final ByteBuffer buffer) {
		final byte [] array = readBytes(buffer, 4);

		return ByteBuffer.wrap(array).getFloat();
	}

	public static String readString(final ByteBuffer buffer, int length) {
		final byte [] strData = new byte [length];
		buffer.get(strData);

		return new String(strData, StandardCharsets.UTF_8);
	}

	private static String readStringWithLength(final ByteBuffer buffer) {
		long stringLength = readU32(buffer);
		return readString(buffer, (int)stringLength);
	}

	private static Instant readTimestamp(final ByteBuffer buffer) {
		@SuppressWarnings({"unused", "squid:S1854", "squid:S1481"})
		final BigInteger fractions = readU64(buffer);
		final long timeSinceEpoche = readI64(buffer);

		return Instant.ofEpochSecond(timeSinceEpoche - EPOCHE_DIFFERENCE_IN_SECONDS);
	}

	private static int readU16(final ByteBuffer buffer) {
		return buffer.get() & 0xFF | (buffer.get() << 8) & 0xFFFF;
	}

	private static long readU32(final ByteBuffer buffer) {
		return (buffer.get() & 0xFFl) | ((buffer.get() << 8) & 0xFFFFl) | ((buffer.get() << 16) & 0xFFFFFFl) | ((buffer.get() << 24) & 0xFFFFFFFFl);
	}

	private static BigInteger readU64(final ByteBuffer buffer) {
		BigInteger result =  new BigInteger(readBytes(buffer, 8));

		if(result.compareTo(BigInteger.ZERO) < 0) {
			result = result.add(BigInteger.ONE.shiftLeft(64));
		}

		return result;
	}

	private static short readU8(final ByteBuffer buffer) {
		return (short)(buffer.get() & 0xFF);
	}

	final long magicValue;
	private final int size;
	private final Function<ByteBuffer, Object> readFunction;
	private final ThrowingBiConsumer<Object, RandomAccessFile, IOException> writeFunction;


	private Type(final long magicValue, final int size) {
		this.magicValue = magicValue;
		this.size = size;

		switch((int)magicValue) {
		case 0x21:
			readFunction = Type::readBoolean;
			writeFunction = Type::writeUnsupportedType;
			break;

		case 0x0A:
			readFunction = Type::readDouble;
			writeFunction = Type::writeDouble;
			break;

		case 0x00:
			readFunction = Type::readVoid;
			writeFunction = Type::writeVoid;
			break;

		case 0x01:
			readFunction = Type::readI8;
			writeFunction = Type::writeI8;
			break;

		case 0x02:
			readFunction = Type::readI16;
			writeFunction = Type::writeI16;
			break;

		case 0x03:
			readFunction = Type::readI32;
			writeFunction = Type::writeI32;
			break;

		case 0x04:
			readFunction = Type::readI64;
			writeFunction = Type::writeI64;
			break;

		case 0x05:
			readFunction = Type::readU8;
			writeFunction = Type::writeU8;
			break;

		case 0x06:
			readFunction = Type::readU16;
			writeFunction = Type::writeU16;
			break;

		case 0x07:
			readFunction = Type::readU32;
			writeFunction = Type::writeU32;
			break;

		case 0x08:
			readFunction = Type::readU64;
			writeFunction = Type::writeU64;
			break;

		case 0x09:
			readFunction = Type::readSingle;
			writeFunction = Type::writeSingle;
			break;

		case 0x20:
			readFunction = Type::readStringWithLength;
			writeFunction = Type::writeStringWithLength;
			break;

		case 0x44:
			readFunction = Type::readTimestamp;
			writeFunction = Type::writeUnsupportedType;
			break;

		default:
			readFunction = Type::readUnsupportedType;
			writeFunction = Type::writeUnsupportedType;
			break;
		}
	}

	private static Object readUnsupportedType(final ByteBuffer buffer) {
		log.error("Reading unsupported type from {}.", buffer);

		return null;
	}

	/**
	 * @return The size in bytes
	 */
	public int size() {
		return size;
	}

	public boolean isType(final long value) {
		return value == magicValue;
	}

	public <T> T read(final ByteBuffer buffer, final Class<T> clazz) {
		return clazz.cast(readFunction.apply(buffer));
	}

	public void write(final Object object, final RandomAccessFile raf) throws IOException {
		writeFunction.accept(object, raf);
	}

	private static void writeStringWithLength(final Object string, final RandomAccessFile raf) throws IOException {
		final String value = (String)string;

		writeU32((long)value.length(), raf);
		raf.write(value.getBytes());
	}

	@SuppressWarnings("squid:S1172")
	private static void writeVoid(final Object v0id, final RandomAccessFile raf) {
		// void isn't written to the resulting file.
	}

	private static void writeU64(final Object u64, final RandomAccessFile raf) throws IOException {
		byte [] data = ((BigInteger)u64).toByteArray();
		byte bytesWritten = 0;

		for(int index = data.length - 1; index >= 0 && bytesWritten < 8; --index) {
			raf.write(data[index]);
			bytesWritten += 1;
		}

		for(; bytesWritten < 8; bytesWritten += 1) {
			raf.write(0x00);
		}
	}

	private static void writeI8(final Object i8, final RandomAccessFile raf) throws IOException {
		final Byte value = (Byte)i8;

		raf.write(value.intValue());
	}

	private static void writeI16(final Object i16, final RandomAccessFile raf) throws IOException {
		final int value = (Short)i16;

		raf.write((value      ) & 0xFF);
		raf.write((value >>  8) & 0xFF);
	}

	private static void writeI32(final Object i32, final RandomAccessFile raf) throws IOException {
		final int value = (Integer)i32;

		raf.write((value      ) & 0xFF);
		raf.write((value >>  8) & 0xFF);
		raf.write((value >> 16) & 0xFF);
		raf.write((value >> 24) & 0xFF);
	}

	private static void writeI64(final Object i64, final RandomAccessFile raf) throws IOException {
		final long value = (Long)i64;

		raf.write((int)(value      ) & 0xFF);
		raf.write((int)(value >>  8) & 0xFF);
		raf.write((int)(value >> 16) & 0xFF);
		raf.write((int)(value >> 24) & 0xFF);
		raf.write((int)(value >> 32) & 0xFF);
		raf.write((int)(value >> 40) & 0xFF);
		raf.write((int)(value >> 48) & 0xFF);
		raf.write((int)(value >> 56) & 0xFF);
	}

	private static void writeDouble(final Object obj, final RandomAccessFile raf) throws IOException {
		final Double value = (Double)obj;
		final long bits = Double.doubleToLongBits(value);

		final ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
		buf.putLong(bits);

		for(int index = Long.BYTES - 1; index >= 0; --index) {
			raf.writeByte(buf.array()[index]);
		}
	}

	private static void writeSingle(final Object obj, final RandomAccessFile raf) throws IOException {
		final Float value = (Float)obj;
		final int bits = Float.floatToIntBits(value);

		final ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES);
		buf.putInt(bits);

		for(int index = Integer.BYTES - 1; index >= 0; --index) {
			raf.writeByte(buf.array()[index]);
		}
	}

	private static void writeU8(final Object u8, final RandomAccessFile raf) throws IOException {
		final short value = (Short)u8;

		raf.write(value);
	}

	private static void writeU16(final Object u16, final RandomAccessFile raf) throws IOException {
		final int value = (Integer)u16;

		raf.write(value);
		raf.write(value >> 8);
	}

	private static void writeU32(final Object u32, final RandomAccessFile raf) throws IOException {
		final Long value = (Long)u32;

		ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
		bb.putLong(value);
		byte [] data = bb.array();

		for(int i = data.length - 1; i >= Long.BYTES / 2; --i) {
			raf.writeByte(data[i]);
		}
	}

	public static void writeUnsupportedType(final Object object, final RandomAccessFile raf) {
		log.error("Writing unsupported type {} to {}.", object, raf);
	}

	public long getMagicValue() {
		return magicValue;
	}
}
