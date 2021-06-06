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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnExecute;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.Output;
import de.codapro.api.model.BufferingStream;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;
import de.codapro.components.core.stream.impl.ConversionRule;

@Component(name = "MedianFilter", doc = "Median filter to remove noise.")
public class MedianFilter {
	@Log
	private static Logger log;

	/**
	 * Conversions.
	 */
	private List<ConversionRule> conversionRules = new ArrayList<>();

	@ColumnId(doc = "Name of the input columns to calculate the median for.", name = "columns", required = true, stream = "input-stream")
	public int [] inputColumns;

	@Input(doc = "Name of the output columns for the calculated median.", name = "results", required = true)
	public String [] outputColumns;

	private BufferingStream dataBuffer;

	@Input(doc="The filter size.", name="filter-size")
	private int filterSize;

	@Input(doc="A stream of data vectors to smooth.", name="input-stream")
	private Stream inputStream;

	@Output(doc="A stream containing the smoothed data vectors.", name="output-stream")
	private Stream outputStream;

	@OnExecute
	public void execute() throws ConversionException {
		try {
			while(true) {
				final DataVector vector = inputStream.get();

				for(final ConversionRule rule : conversionRules) {
					final List<Object> sortedElements = dataBuffer.stream().map(v -> v.get(rule.getSourceIndex())).sorted().collect(Collectors.toList());
					Object median = sortedElements.get(sortedElements.size() / 2);
					dataBuffer.get(filterSize / 2).set(rule.getTargetIndex(), median);
				}
				dataBuffer.append(vector);
			}
		} catch(final NoSuchElementException e) {
			// end of stream reached.
		}
	}

	private Number getNaN(final Number object) {
		if(object instanceof Float) {
			return Float.NaN;
		} else if(object instanceof Double) {
			return Double.NaN;
		}

		log.error("Unsuppoerted number format {}.", object.getClass());
		return 0;
	}

	@OnInit
	public void initBuffers() throws ConversionException {
		copyStreamHeader();

		createConversionRules();

		dataBuffer = new BufferingStream(outputStream, filterSize);

		while(!dataBuffer.isFull()) {
			final DataVector vector = inputStream.get();

			for(final ConversionRule rule : conversionRules) {
				final Number nan = getNaN((Number)vector.get(rule.getSourceIndex()));
				vector.set(rule.getTargetIndex(), nan);
				dataBuffer.append(vector);
			}
		}
	}

	private void copyStreamHeader() {
		outputStream.setHeader(inputStream.getHeader())
					.add(outputColumns);
	}

	private void createConversionRules() {
		if(inputColumns.length != outputColumns.length) {
			log.warn("The numbers of input ({}) and result ({}) columns does not match.", inputColumns.length, outputColumns.length);
		}

		conversionRules.clear();
		for(int i = 0; i < Math.min(inputColumns.length, outputColumns.length); ++i) {
			conversionRules.add(new ConversionRule(inputColumns[i], outputStream.getHeader().indexOf(outputColumns[i])));
		}
	}

	@Override
	public String toString() {
		return "MedianFilter [inputColumns=" + inputColumns + ", outputColumns=" + outputColumns + "]";
	}
}
