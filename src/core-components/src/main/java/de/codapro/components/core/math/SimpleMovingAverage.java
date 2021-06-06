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

import java.util.Arrays;
import java.util.NoSuchElementException;

import javax.inject.Named;

import org.slf4j.Logger;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnExecute;
import de.codapro.api.annotations.OnFinish;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.annotations.Output;
import de.codapro.api.model.BufferingStream;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "SimpleMovingAverage", doc = "computes the moving average of a column")
public class SimpleMovingAverage {
	
	private BufferingStream dataBuffer; 
	
	@Input(doc="The amount of data to be smoothed. Average of order 'filter size'.", name="filter-size")
	private int filterSize;

	@ColumnId(doc = "Name of the input columns to calculate the mean for.", name = "columns", required = true, stream = "input-stream")
	public int[]  inputColumns;

	@Input(doc="A stream of data vectors to smooth.", name="input-stream")
	private Stream inputStream;

	@Log
	private Logger log;

	@Input(doc = "Name of the output columns for the calculated mean.", name = "results", required = true)
	public String[]  outputColumns;

	//@Output(doc="The stream containing the original data with columns added for the smoothed values.", name="output-stream")
	//private Stream outputStream;

	@OnInit()
	public void initBuffers(@Named("output-stream") final Stream outputStream) throws ConversionException {
		copyStreamHeader(outputStream);
		dataBuffer = new BufferingStream(outputStream, filterSize);
	}

	@OnProcess(dest= {})
	public void process(final DataVector vector) throws ConversionException {
		dataBuffer.append(vector);

		final double [] means = computeMeanFromBuffer();
		vector.appendAll(means);
	}

	@OnFinish
	public void finish() {
		try {
			dataBuffer.close();
		} catch (final ConversionException c) {

		}
	}

	private double[] computeMeanFromBuffer() {
		double[] mean = new double[inputColumns.length];

		//for each column to be smoothed
		for(int j = 0; j < inputColumns.length; j++) {

			//compute sum of current buffer
			final int currentSize = dataBuffer.size();
			for(int i = 0; i < currentSize; i++) {
				mean[j] = mean[j] + (Double) dataBuffer.get(i).get(inputColumns[j]);
			}

			//divide every sum by filterSize to obtain mean
			mean[j] /= currentSize;
		}


		return mean; 
	}

	private void copyStreamHeader(final Stream outputStream) {
		outputStream.setHeader(inputStream.getHeader())
					.add(outputColumns);
	}

	@Override
	public String toString() {
		return "SimpleMovingAverage [inputColumns=" + inputColumns + ", outputColumns=" + outputColumns + "]";
	}
}
