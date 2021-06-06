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
package de.codapro.components.core.utils.math;

import org.mariuszgromada.math.mxparser.FunctionExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function extension to give access to all values by index.
 * 
 * @author Bernhard J. Berger
 */
public class TupleValueFunctionExtension implements FunctionExtension {
	/**
	 * Logger instance‚
	 */
	private static final Logger log = LoggerFactory.getLogger(TupleValueFunctionExtension.class);

	/**
	 * Value index to read.
	 */
	private int valueIndex = -1;

	/**
	 * Tuple index to read.
	 */
	private int tupleIndex = -1;
	
	/**
	 * List of all values.
	 */
	private Double[][] values;

	public TupleValueFunctionExtension(final Double [][] values) {
		setValues(values);
	}

	@Override
	public double calculate() {
		if(valueIndex < 0 || valueIndex >= values.length) {
			log.error("Invalid value index {}.", valueIndex);
			throw new IllegalStateException("Value index " + valueIndex + " is not in range.");
		}

		if(tupleIndex < 0 || tupleIndex >= values[valueIndex].length) {
			log.error("Invalid tuple index {}.", tupleIndex);
			throw new IllegalStateException("Tuple index " + tupleIndex + " is not in range.");
		}

		return values[valueIndex][tupleIndex];
	}

	@Override
	public FunctionExtension clone() {
		final TupleValueFunctionExtension result = new TupleValueFunctionExtension(this.values);
		result.setParameterValue(0, valueIndex);
		result.setParameterValue(1, tupleIndex);

		return result;
	}

	@Override
	public String getParameterName(int parameterIndex) {
		switch(parameterIndex) {
		case 0:
			return "value-index";
		case 1:
			return "tuple-index";
		default:
			log.error("There is no parameter with index {}.", parameterIndex);
		}

		return "<unknown>";
	}

	@Override
	public int getParametersNumber() {
		return 2;
	}

	public Double[][] getValues() {
		return values;
	}

	@Override
	public void setParameterValue(int parameterIndex, double parameterValue) {
		if(parameterIndex == 0) {
			valueIndex = (int)parameterValue;
		} else if(parameterIndex == 1) {
				tupleIndex = (int)parameterValue;
		} else {
			log.error("There is no parameter with index {}.", parameterIndex);
		}
	}

	public void setValues(Double[][] values) {
		this.values = values;
	}

}
