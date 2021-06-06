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
package de.codapro.components.core.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convert strings into other objects.
 *
 * @author Bernhard J. Berger
 */
public final class StringConverter {
	/**
	 * Logger instance
	 */
	private static final Logger log = LoggerFactory.getLogger(StringConverter.class);

	private static Function<String, Object> parseDoubleFunction(final Locale locale) {
		final NumberFormat nf = NumberFormat.getInstance(locale);

		return str -> {
			try {
				return nf.parse(str).doubleValue();
			} catch(final ParseException nfe) {
				log.error("Failed to parse floating point number '{}'. Therefore, we will return NaN.", str);
				return Double.NaN;
			}
		};
	}

	private static Function<String, Object> parseFloatFunction(final Locale locale) {
		final NumberFormat nf = NumberFormat.getInstance(locale);

		return str -> {
			try {
				return nf.parse(str).floatValue();
			} catch(final ParseException nfe) {
				log.error("Failed to parse floating point number '{}'. Therefore, we will return NaN.", str);
				return Double.NaN;
			}
		};
	}

	/**
	 * Returns a data conversion method for the given type.
	 */
	public static Function<String, Object> getFunction(final String conversionKind, final Locale locale) {
		switch(conversionKind) {
		case "%b":
			log.info("Using boolean conversion.");
			return val -> "1".equals(val) || "true".equalsIgnoreCase(val);

		case "%f":
			log.info("Using float conversion.");
			return parseFloatFunction(locale);

		case "%d":
			log.info("Using double conversion.");
			return parseDoubleFunction(locale);

		case "%i":
			log.info("Using int conversion.");
			return Integer::parseInt;

		default:
			log.info("Using identity conversion.");
			return t -> t;
		}
	}

	private StringConverter() {
	}
}
