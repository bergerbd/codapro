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
package de.codapro.components.core.stream;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Function;

import org.slf4j.Logger;

import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.components.core.utils.StringConverter;

@Component(name = "ValueConverter", doc = "Component for turning string data of data vectors into a parsed form such as float %f, double %d, integer %i, or string %s.")
public class ValueConverter {
	@Log
	private static Logger log;

	/**
	 * The conversions to execute
	 */
	private ArrayList<Function<String, Object>> conversionMatrix;

	@Input(doc="The transformation rule. For each column you can specify a transformation.", name="transformation")
	private String [] conversions;

	/**
	 * Number of conversions to execute
	 */
	private int conversionsCount;

	@Input(doc="Locale used for parsing numeric values.", name="locale", required = false)
	private Locale locale = Locale.getDefault();

	@OnInit
	public void init() {
		conversionsCount = conversions.length;
		conversionMatrix = new ArrayList<>(conversionsCount);

		for(final String conversionKind : conversions) {
			conversionMatrix.add(StringConverter.getFunction(conversionKind, locale));
		}
	}

	@OnProcess
	public void process(final DataVector vector) {
		for(int index = 0; index < conversionsCount; ++index) {
			final Function<String, Object> function = conversionMatrix.get(index);
			vector.set(index, function.apply((String)vector.get(index)));
		}
	}

	@Override
	public String toString() {
		return "ValueConverter [conversions=" + conversions + "]";
	}
}
