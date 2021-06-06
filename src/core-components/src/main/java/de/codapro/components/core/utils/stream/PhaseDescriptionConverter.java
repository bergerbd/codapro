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
package de.codapro.components.core.utils.stream;

import org.apache.commons.beanutils.Converter;

import de.codapro.api.annotations.ConfigConverter;

@ConfigConverter(type=PhaseDescription.class)
public class PhaseDescriptionConverter implements Converter {

	@Override
	public <T> T convert(final Class<T> type, final Object value) {
		final String str = value.toString();
		
		
		
		final String[] spStr = str.split(":");
		
		//Has to contain three elements, because of "name:start:end".
		if (spStr.length != 3) {
			throw new RuntimeException("PhaseDescriptor is incorrect. Expected 'name:start:end' - got "+ value);
		}
		
		final String name = spStr[0];
		final double start = Double.valueOf(spStr[1]);
		final double end = Double.valueOf(spStr[2]);
		
		PhaseDescription phD; 
		
		if (start < end) {
			phD = new PhaseDescriptionRising(name, start, end);
		}
		
		else if (start > end) {
			phD = new PhaseDescriptionFalling(name, start, end);
		}
		
		else  {
			phD = new PhaseDescriptionHolding(name, start, end);
		}
		
		
		
		
		
		
		
		
		
		
		return type.cast(phD);
	}

}
