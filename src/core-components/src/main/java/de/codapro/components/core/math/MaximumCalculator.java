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

@Component(name= "MaximumCalculator",
           doc="Determines the maximal value within a given time frame for given channels.",
           sinks = {
               @Input(doc="The component's input stream.", name="input-stream", required=true),
               @Input(doc="Input Stream that contains already computed data", name="descriptor-stream-input")

           },
           sources = {
               @Output(doc="The component's output stream", name="output-stream"),
               @Output(doc="Output Stream that contains Input Stream plus newly computed descriptor", name="descriptor-stream-output")

           }
)
public class MaximumCalculator {
	@ColumnId(doc = "Columns that should be used for maximum calculation.", name = "columns-for-evaluation", stream = "input-stream")
	private int[] colNamesToBeEvaluated;

	private DataVector currentDescriptorRow;

	@ColumnId(doc = "Time column.", name = "det-col", stream = "input-stream")
	private int detCol;

	private double[] end;

	@Log
	private Logger log;

	private double[] maximum;
	private double[] start;

	@ColumnId(doc = "Columns containing start and end of times.", name = "start-end-cols", stream = "descriptor-stream-input")
	private int[] startEndCols;

	@OnEnterGroup
	public void enter(final @Named("descriptor-stream-input") Stream input) {
		currentDescriptorRow = input.get();

		maximum = new double[colNamesToBeEvaluated.length];
		for(int i = 0; i < colNamesToBeEvaluated.length; ++i) {
			maximum[i] = Double.MAX_VALUE * -1;
		}

		start = new double[colNamesToBeEvaluated.length];
		end = new double[colNamesToBeEvaluated.length];
		for (int i = 0; i < colNamesToBeEvaluated.length; i++) {
			start[i] = ((Number) currentDescriptorRow.get(startEndCols[i * 2])).doubleValue();
			end[i] = ((Number) currentDescriptorRow.get(startEndCols[i * 2 + 1])).doubleValue();
		}
	}

	@OnInit
	public void initialHeaderChange(final @Named("descriptor-stream-input") Stream input,
									final @Named("input-stream") Stream realInput,
									final @Named("descriptor-stream-output") Stream output) {
		final StreamHeader header = output.setHeader(input.getHeader());
		final StreamHeader realHeader = realInput.getHeader();

		Arrays.stream(colNamesToBeEvaluated)
				.mapToObj(realHeader::nameOf) // convert column ids to column names
				.map(name -> "Maximum " + name) // calculate new column name
				.forEach(header::add); // add columns
	}

	private boolean isInBetween(double time, double start, double end) {
		return ((time >= start) && (time <= end));
	}

	@OnLeaveGroup
	public void leave(final @Named("descriptor-stream-output") Stream output) throws ConversionException {
		currentDescriptorRow.appendAll(maximum);
		output.append(currentDescriptorRow);
	}

	@OnProcess
	public void process(final DataVector vector) {
		// fetch current data from vector

		for (int i = 0; i < colNamesToBeEvaluated.length; i++) {
			if(!isInBetween(((Number) vector.get(detCol)).doubleValue(), start[i], end[i])) {
				continue; // data set is not of intereset since out of time frame
			}

			final double value = ((Number) vector.get(colNamesToBeEvaluated[i])).doubleValue();
			if(value > maximum[i]) {
				maximum[i] = value;
			}
		}
	}
}

