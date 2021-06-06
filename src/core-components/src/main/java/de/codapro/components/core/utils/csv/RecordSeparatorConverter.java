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

import org.apache.commons.beanutils.Converter;

import de.codapro.api.annotations.ConfigConverter;

/**
 * Converter to convert Strings into RecordSeparator instances.
 */
@ConfigConverter(type=RecordSeparator.class)
public final class RecordSeparatorConverter implements Converter {
	@Override
	public <T> T convert(final Class<T> type, final Object value) {
		return type.cast(RecordSeparator.valueOf(value.toString()));
	}
}