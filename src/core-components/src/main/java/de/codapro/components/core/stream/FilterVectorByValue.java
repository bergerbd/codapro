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

import java.util.Arrays;

import javax.inject.Named;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "FilterVectorByValue", doc = "Removes data vectors if a specific column matches a given value.")
public class FilterVectorByValue {
	@ColumnId(doc="Name of the columns to compare.", name="column", stream="input-stream")
	private int column;

	@Input(doc = "List of values that leads to removing a vector.", name = "values")
	private String [] values = {};

	@Named("output-stream")
	private Stream outputStream;

	@OnInit
	public void sortValues(@Named("input-stream") Stream inputStream) {
		outputStream.setHeader(inputStream.getHeader());

		Arrays.sort(values);
	}
	
	@OnProcess(dest = {})
	public void removeColumns(final DataVector vector) throws ConversionException {
		final String value = vector.get(column).toString();
		
		if(Arrays.binarySearch(values, value) >= 0) {
			return;
		}
		
		outputStream.append(vector);
	}

	@Override
	public String toString() {
		return "FilterVectorByValue [values=" + Arrays.toString(values) + "]";
	}
}
