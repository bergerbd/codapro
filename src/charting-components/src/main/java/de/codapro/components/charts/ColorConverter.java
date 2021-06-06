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
package de.codapro.components.charts;

import java.awt.Color;

import org.apache.commons.beanutils.Converter;

import de.codapro.api.annotations.ConfigConverter;

@ConfigConverter(type=Color.class)
public class ColorConverter implements Converter {

	@Override
	public <T> T convert(final Class<T> type, final Object value) {
		final String strValue = (String)value;

		switch(strValue) {
		case "white" : return type.cast(Color.white);
		case "light-gray" : return type.cast(Color.lightGray);
		case "dark-gray" : return type.cast(Color.darkGray);
		case "black" : return type.cast(Color.black);
		case "red" : return type.cast(Color.red);
		case "pink" : return type.cast(Color.pink);
		case "orange" : return type.cast(Color.orange);
		case "yellow" : return type.cast(Color.yellow);
		case "green" : return type.cast(Color.green);
		case "magenta" : return type.cast(Color.magenta);
		case "cyan" : return type.cast(Color.cyan);
		case "blue" : return type.cast(Color.BLUE);
		default : return type.cast(Color.decode(strValue));
		}
	}
}
