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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import de.codapro.tdms.io.impl.TDMsVoid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for turning magic type values into instances.
 */
public final class TypeFactory {
	/**
	 * Logger instance.
	 */
	private static final Logger log = LoggerFactory.getLogger(Type.class);

	/**
	 * Fast map for default types.
	 */
	private static Type [] fastTypeMap = new Type[0x100];

	/**
	 * Slow map for other types
	 */
	private static Map<Long, Type> slowTypeMap = new HashMap<>();

	static {
		for(final Type type : Type.values()) {
			if(type.magicValue < (long)fastTypeMap.length) {
				fastTypeMap[(int)type.magicValue] = type;
			} else {
				slowTypeMap.put(type.magicValue, type);
			}
		}
	}

	/**
	 * @return The {@code Type} for the magic value.
	 */
	public static Type fromLong(long typeValue) {
		Type result = null;

		if(typeValue < fastTypeMap.length) {
			result = fastTypeMap[(int)typeValue];
		} else {
			result = slowTypeMap.get(typeValue);
		}

		if(result == null) {
			log.error("Unknown TDMs type magic '{}'.", typeValue);
			return Type.VOID;
		}

		return result;
	}

	private TypeFactory() {
	}

	public static Type typeOf(final Object value) {
		if(value instanceof TypedValue) {
			return ((TypedValue)value).getType();
		} else if(value == null || value instanceof TDMsVoid) {
			return Type.VOID;
		} else if(value instanceof String) {
			return Type.STRING;
		} else if(value instanceof BigInteger) {
			final BigInteger val = (BigInteger) value;
			if(val.signum() == -1) {
				return Type.I64;
			} else {
				return Type.U64;
			}
		} else if(value instanceof Double) {
			return Type.DOUBLE_FLOAT;
		} else if(value instanceof Float) {
			return Type.SINGLE_FLOAT;
		} else if(value instanceof Number) {
			return typeOfInteger(value);
		}

		return Type.VOID;
	}

	private static Type typeOfInteger(Object value) {
		final Number val = (Number)value;
		if(val.doubleValue() < 0.0) {
			if(value instanceof Long) {
				return Type.I64;
			} else if(value instanceof Integer) {
				return Type.I32;
			} else if(value instanceof Short) {
				return Type.I16;
			} else if(value instanceof Byte) {
				return Type.I8;
			}
		} else {
			if(value instanceof Long) {
				return Type.U32;
			} else if(value instanceof Integer) {
				return Type.U16;
			} else if(value instanceof Short) {
				return Type.U8;
			}
		}

		return Type.VOID;
	}
}
