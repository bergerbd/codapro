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
package de.codapro.components.core.identifier;

import java.util.NoSuchElementException;

import javax.inject.Named;

import org.slf4j.Logger;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnExecute;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.MarkingStream;
import de.codapro.api.model.Stream;
import de.codapro.api.model.Value;

@Component(name = "WindowDetectorByThreshold", doc = "Find peaks in a data stream. The result is stored to a boolean column that is appended to each data vector.")
public class WindowDetectorByThreshold {
	@Log
	private static Logger log;

	private MarkingStream bufferingStream;

	@ColumnId(doc = "The column for peak detection.", name = "column", stream = "input-stream")
	private int dataIndex;

	@Named("input-stream")
	private Stream inputStream;

	@Input(doc = "The threshold value for identifying a peak .", name = "threshold", type = Double.class)
	private Value<Double> threshold;

	private boolean oldHighState = false;

	@Input(doc = "Number of data points that will be transfered before and after an peak.", name = "offset-size", required = false)
	private int windowSize = 5000;

	@Input(doc = "Name of the new column that stores the information if the vector belongs to a peak or not.", name = "name")
	private String peakColumn;

	@Input(doc = "Whether phases are considered or not (defaults to false).", name = "phases-present", required = false)
	private boolean phaseIsSet = false;

	@ColumnId(doc = "Column to identify phase.", name = "phase-column", stream = "input-stream", required = false)
	private int phaseColumnId;

	private String previousPhase;

	@OnInit
	public void getColumnId(final @Named("output-stream") Stream outputStream) {
		outputStream.setHeader(inputStream.getHeader());
		outputStream.getHeader().add(peakColumn);
	}

	@OnExecute
	public void execute(final @Named("output-stream") Stream outputStream) throws  ConversionException {
		bufferingStream = new MarkingStream(outputStream, windowSize);

		processColumns();

		try {
			bufferingStream.close();
		} catch (final Exception e) {
			throw new ConversionException("Failed to close buffered stream.", e);
		}
	}

	/**
	 * Process a single data vector
	 */
	private void processColumn(final DataVector vector) throws ConversionException {
		final Number currentValue = (Number) vector.get(dataIndex);
		
		boolean currentHighState = (Math.abs(currentValue.floatValue()) > threshold.get());
		
		if(phaseIsSet) {
			final String currentPhase = (String)vector.get(phaseColumnId);
			
			boolean phaseChange = ((previousPhase ==null)||(currentPhase != previousPhase));
			currentHighState = (currentHighState && !phaseChange);
			
			previousPhase = currentPhase;
		}
		

		if (!oldHighState && currentHighState) {
			bufferingStream.markPast();
		} else if (oldHighState && !currentHighState) {
			bufferingStream.markFuture();
		}

		bufferingStream.append(vector, currentHighState);
		oldHighState = currentHighState;
	}

	/**
	 * Process all input data.
	 */
	private void processColumns() throws ConversionException {
		boolean keepProcessing = true;

		while(keepProcessing) {
			try {
				final DataVector col = inputStream.get();

				processColumn(col);
			} catch (final NoSuchElementException e) {
				keepProcessing = false;
				log.debug("Processed all data entries and got a NoSuchElementException.", e);
			}
		}
	}

	@Override
	public String toString() {
		return "PeakDetectorByStartingNoise [noise-delta=" + threshold + "]";
	}
}
