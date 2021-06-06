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

import javax.inject.Named;

import de.codapro.components.core.utils.stream.PhaseDescription;
import org.slf4j.Logger;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;


@Component(name= "SeparatorByFixedValue", doc = "Adds a column that identifies which type of phase this is")
public class MarkByFixedValue {
	
	
	@Input(doc="Name of column", name =  "column-name")
	private String nameOfColumn = "phase";
	
	@ColumnId(doc="Name of column of interest", name = "column-of-interest", required= true, stream = "input-stream" )
	private int colId;
	
	@Input(doc="Phases for destinction.", name = "phases-to-destinct", required = true)
	private PhaseDescription[] phaseDescriptors = {};
	
	@Log
	private Logger log;

	@Named("output-stream")
	private Stream outputStream;

	private int phaseIndex = 0;

	@OnInit
	public void copyHeaders(final @Named("input-stream") Stream stream) {
		outputStream.setHeader(stream.getHeader()).add(nameOfColumn);
	}

	@OnProcess
	public void execute(final DataVector vector) throws ConversionException {
		final double candidate = ((Number)vector.get(colId)).doubleValue();
		
		if(!phaseDescriptors[phaseIndex].matches(candidate)) {
			phaseIndex++;						
		}
		
		vector.append(phaseDescriptors[phaseIndex].getName());		
		
	}

	@Override
	public String toString() {
		return "SeparatorByFixedValue []";
	}
}
