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
package de.codapro.components.core.stream.logic;

import java.util.Arrays;

import javax.inject.Named;

import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "And", doc = "Takes a set of boolean columns and calculates the and result. The result is append to the data vector.")
public class LogicalAndCalculator {

	@Input(doc="The name of the new column.", name="name")
	private String columnName;

	@ColumnId(doc="List of columns to 'and'.", name="columns", stream="input-stream")
	private int[] columnIndices;

	@OnInit
	public void addColumnName(final @Named("output-stream") Stream stream) {
		stream.getHeader().add(columnName);
	}

	@OnProcess
	public void calculate(final DataVector vector) {
		boolean result = true;

		for(final int index : columnIndices) {
			result &= (Boolean)vector.get(index);
		}

		vector.append(result);
	}

	@Override
	public String toString() {
		return "And [columns=" + Arrays.toString(columnIndices) + "]";
	}
}
