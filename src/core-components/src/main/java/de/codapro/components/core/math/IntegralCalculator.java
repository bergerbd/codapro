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

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnEnterGroup;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnLeaveGroup;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.annotations.Output;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;
import de.codapro.api.model.StreamHeader;
import java.util.Arrays;
import javax.inject.Named;
import org.slf4j.Logger;

/**
 *
 * @author cplump
 */

@Component(name = "IntegralCalculator", doc = "Calculates the integral under a curve for given channels.", sinks = {
		@Input(doc = "The component's input stream.", name = "input-stream", required = true),
		@Input(doc = "Input Stream that contains already computed data", name = "descriptor-stream-input")

}, sources = { @Output(doc = "The component's output stream", name = "output-stream"),
		@Output(doc = "Output Stream that contains Input Stream plus newly computed descriptor", name = "descriptor-stream-output")

})
public class IntegralCalculator {
	@ColumnId(doc = "Columns that should be used for evaluation", name = "columns-for-integration", stream = "input-stream")
	private int[] columnsToIntegrate;

	private DataVector currentDescriptorRow;

	private double[] currentValues;

	private double[] end;
	private double[] integral;

	@ColumnId(doc = "column to be integrated over", name = "column-of-integrand", stream = "input-stream")
	private int integrandColumn;

	@Log
	private Logger log;

	private double[] lowerSum;
	private double previousIntegrand;
	private double[] previousValues;

	private double[] start;
	@ColumnId(doc = "start and end of values to be evaluated over in order of columns-for-evaluation", name = "start-end-columns", stream = "descriptor-stream-input")
	private int[] startEndColumns;
	private double[] upperSum;

	@OnEnterGroup
	public void enter(final @Named("descriptor-stream-input") Stream input) {
		currentDescriptorRow = input.get();
		log.info("You can read this.");

		previousValues = null;
		currentValues = new double[columnsToIntegrate.length];
		lowerSum = new double[columnsToIntegrate.length];
		upperSum = new double[columnsToIntegrate.length];
		integral = new double[columnsToIntegrate.length];
		start = new double[columnsToIntegrate.length];
		end = new double[columnsToIntegrate.length];
		for (int i = 0; i < columnsToIntegrate.length; i++) {
			start[i] = ((Number) currentDescriptorRow.get(startEndColumns[i * 2])).doubleValue();
			end[i] = ((Number) currentDescriptorRow.get(startEndColumns[i * 2 + 1])).doubleValue();
		}
	}

	@OnInit
	public void initialHeaderChange(final @Named("descriptor-stream-input") Stream input,
			final @Named("input-stream") Stream realInput, final @Named("descriptor-stream-output") Stream output) {
		final StreamHeader header = output.setHeader(input.getHeader());
		final StreamHeader realHeader = realInput.getHeader();

		Arrays.stream(columnsToIntegrate)
			  .mapToObj(realHeader::nameOf) // convert column ids to column names
			  .map(name -> "Integral " + name) // calculate new column name
			  .forEach(header::add); // add columns
	}

	private boolean isInBetween(double time, double start, double end) {
		return ((time > start) && (time <= end));
	}

	@OnLeaveGroup
	public void leave(final @Named("descriptor-stream-output") Stream output) throws ConversionException {
		currentDescriptorRow.appendAll(integral);

		output.append(currentDescriptorRow);
	}

	@OnProcess
	public void process(final DataVector vector) {
		// fetch current data from vector

		final double currentIntegrand = ((Number) vector.get(integrandColumn)).doubleValue();
		final double[] localCurrentValues = this.currentValues;

		for (int i = 0; i < columnsToIntegrate.length; i++) {
			localCurrentValues[i] = ((Number) vector.get(columnsToIntegrate[i])).doubleValue();
		}

		if (previousValues == null) {
			previousValues = new double[columnsToIntegrate.length];
		} else {
			final double integrandDifference = currentIntegrand - previousIntegrand;
			for (int i = 0; i < columnsToIntegrate.length; i++) {
				if (isInBetween(((Number) vector.get(integrandColumn)).doubleValue(), start[i], end[i])) {
					lowerSum[i] = lowerSum[i] + integrandDifference * Math.min(previousValues[i], localCurrentValues[i]);
					upperSum[i] = upperSum[i] + integrandDifference * Math.max(previousValues[i], localCurrentValues[i]);
					integral[i] = (lowerSum[i] + upperSum[i]) / 2;
				}
			}
		}

		// copy current data to previous data for next iteration
		previousIntegrand = currentIntegrand;
		System.arraycopy(localCurrentValues, 0, previousValues, 0, previousValues.length);
	}
}
