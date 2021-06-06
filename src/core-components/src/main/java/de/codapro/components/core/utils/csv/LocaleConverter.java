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
package de.codapro.components.core.utils.csv;

import java.util.Locale;

import org.apache.commons.beanutils.Converter;

import de.codapro.api.annotations.ConfigConverter;

/**
 * Converter to convert a String into Locale instances.
 */
@ConfigConverter(type=Locale.class)
public final class LocaleConverter implements Converter {
	@Override
	public <T> T convert(final Class<T> type, final Object value) {
		switch((String)value) {
		case "english" : return type.cast(Locale.ENGLISH);
		case "french" : return type.cast(Locale.FRENCH);
		case "german" : return type.cast(Locale.GERMAN);
		case "italian" : return type.cast(Locale.ITALIAN);
		case "japanese" : return type.cast(Locale.JAPANESE);
		case "korean" : return type.cast(Locale.KOREAN);
		case "chinese" : return type.cast(Locale.CHINESE);
		case "american" : return type.cast(Locale.US);
		default : return type.cast(Locale.getDefault());
		}

	}
}

