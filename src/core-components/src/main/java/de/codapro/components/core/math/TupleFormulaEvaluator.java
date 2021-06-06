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

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.RingBuffer;
import de.codapro.api.model.RingBufferImpl;
import de.codapro.api.model.Stream;
import de.codapro.components.core.utils.math.FormulaEvaluatorBase;
import de.codapro.components.core.utils.math.TupleValueFunctionExtension;

@Component(doc = "A component for calculating new values based on configurable formulas and value tuples.", name = "TupleFormulaEvaluator")
public class TupleFormulaEvaluator extends FormulaEvaluatorBase {
	@Input(doc = "List of 'not a number' values for the results, e.g 1.", name = "nans")
	private double [] nans;

	@ColumnId(doc = "List of column names to use for tuples.", name = "tuple-column-names", stream = "input-stream")
	private int [] tupleColumns;

	@Input(doc = "Size of tuples, default is 50.", name = "capacity", required = false)
	private int capacity = 50;

	/**
	 * Array of ring buffers to store the tuples
	 */
	private RingBuffer<Double> [] buffers;

	@OnInit
	public void init(final @Named("output-stream") Stream stream) {
		addColumnNamesToOutputStream(stream);
		resolveFunctions();

		buffers = new RingBuffer[tupleColumns.length];
		for(int i = 0; i < buffers.length; ++i) {
			buffers[i] = new RingBufferImpl<Double>(Double.class, capacity);
		}
	}

	@OnProcess
	public void evaluateFormulas(final DataVector vector) throws ConversionException {
		final int length = buffers.length;
		for(int i = 0; i < length; ++i) {
			buffers[i].append(((Number)vector.get(tupleColumns[i])).doubleValue());
		}

		// append all nans if buffers aren't filled yet
		if(!buffers[0].isFull()) {
			vector.appendAll(nans);
			return;
		}

		final Function[] valueArrays = new Function[length+1];
		valueArrays[0] = toValuesFunction();

		for(int i = 0; i < length; ++i) {
			valueArrays[i + 1] = toFunction(stream.getHeader().nameOf(tupleColumns[i]), i, valueArrays[0]);
		}

		for(final Expression formula : formulas) {
			formula.removeAllConstants();
			formula.removeAllFunctions();

			formula.addConstants(constants);
			formula.addFunctions(functions);

			formula.addFunctions(valueArrays);

			vector.append(formula.calculate());
		}
	}

	private Function toValuesFunction() {
		final Double [][] values = new Double[buffers.length][];
		int index = 0;
		for(final RingBuffer<Double> buffer : buffers) {
			values[index++] = buffer.toArray();
		}

		final TupleValueFunctionExtension ext = new TupleValueFunctionExtension(values);

		return new Function("values", ext);
	}

	private Function toFunction(final String name, final int index, final Function valuesFunction) {
		final StringBuilder function = new StringBuilder();
		function.append(mangleName(name));
		function.append("(i)");
		function.append("=");
		function.append("values(");
		function.append(index);
		function.append(",i)");

		final Function result = new Function(function.toString());
		result.addFunctions(valuesFunction);

		return result;
	}
}
