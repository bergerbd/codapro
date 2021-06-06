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
package de.codapro.components.core.math;

import javax.inject.Named;

import org.slf4j.Logger;

import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

/**
 * @author cplump
 *
 */

@Component(name="PiecewiseDerivator", doc = "Computes the piecewise derivation of the given column. If given no other information, this is done by simple subtraction. "
		+ "If given a column to refer to, it is computed as a differential quotient")
public class PiecewiseDerivator {

	@Log
	private static Logger log;
	
	@ColumnId(doc = "This is the column, whose piecewise derivation will be computed.", name= "column-to-derivate", stream="input-stream")
	private int colToBeDerivated;
	
	@ColumnId(doc = "This is the column, which contains the variable to derivate after.", name= "column-to-derivate-after", stream = "input-stream", required = false)
	private int colToBeDerivatedAfter; 
	
	@Input(doc = "the name of the new column", name = "column-name")
	private String colName;
	
	@Named("input-stream")
	private Stream inputStream;
	
	private double differentialQuotient=0.0;
	
	
	private double previousDependentValue = 0.0;
	private double previousIndependentValue = 0.0; 
	
	private boolean isFirst;
	
	
	
	
	@OnInit
	public void addHeader(final @Named("output-stream") Stream outputStream) {
		outputStream.setHeader(inputStream.getHeader());
		outputStream.getHeader().add(colName);
		isFirst=true;
	} 
	
	@OnProcess
	public void process(final DataVector vector) {
		final Number dependentValue = (Number) vector.get(colToBeDerivated);
		final Number independentValue = (Number) vector.get(colToBeDerivatedAfter);
		
		if(!isFirst) {

			differentialQuotient = computeDifferentialQuotient(previousDependentValue, previousIndependentValue, dependentValue.doubleValue(), independentValue.doubleValue());

		}
		
		vector.append(differentialQuotient);
		previousDependentValue = dependentValue.doubleValue();
		previousIndependentValue = independentValue.doubleValue(); 
		isFirst = false;
		
		
	}
	
	private double computeDifferentialQuotient(final double previousDependentValue, final double previousIndependentValue, final double dependentValue, final double independentValue) {
		
		return (dependentValue - previousDependentValue)/(independentValue - previousIndependentValue);
	}
	
	
	


}
