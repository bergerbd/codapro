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
import java.util.NoSuchElementException;
import javax.inject.Named;
import org.slf4j.Logger;

/**
 *
 * @author cplump
 */

@Component(name = "WindowDetectorBySlopeChange", doc = "computes piecewise slopes and detects changes that are higher than expected.")
public class WindowDetectorBySlopeChange {
	@Log
	private static Logger log;

	private MarkingStream bufferingStream;

	@ColumnId(doc = "The column for peak detection.", name = "column", stream = "input-stream")
	private int dataIndex;

	@Named("input-stream")
	private Stream inputStream;

	private boolean oldHighState = false;

	@Input(doc = "Name for new column that contains boolean value as for whether this a peak window or not.", name = "name")
	private String peakColumn;

	@ColumnId(doc = "The column for phase destinction.", name = "phase-column", stream = "input-stream")
	private int phaseIndex;

	String previousPhase;

	Number previousValue;

	@Input(doc = "The threshold for slope value", name = "threshold", type = Double.class)
	private Value<Double> threshold;
	@Input(doc = "Number of data points that will be transfered before and after an peak.", name = "offset-size")
	private int windowSize = 5000;

	@OnExecute
	public void execute(final @Named("output-stream") Stream outputStream) throws ConversionException {
		bufferingStream = new MarkingStream(outputStream, windowSize);

		processColumns();

		try {
			bufferingStream.close();
		} catch (final Exception e) {
			throw new ConversionException("Failed to close buffered stream.", e);
		}
	}

	@OnInit
	public void getColumnId(final @Named("output-stream") Stream outputStream) {
		outputStream.setHeader(inputStream.getHeader());
		outputStream.getHeader().add(peakColumn);
	}

	/**
	 * Process a single data vector
	 */
	private void processColumn(final DataVector vector) throws ConversionException {
		boolean currentHighState;
		final Number currentValue = (Number) vector.get(dataIndex);
		final String currentPhase = (String) vector.get(phaseIndex);

		if (previousValue == null || currentPhase != previousPhase) {
			currentHighState = false;
		} else {
			currentHighState = (Math.abs(currentValue.floatValue() - previousValue.floatValue()) > threshold.get());
		}

		if (!oldHighState && currentHighState) {
			bufferingStream.markPast();
		} else if (oldHighState && !currentHighState) {
			bufferingStream.markFuture();
		}

		bufferingStream.append(vector, currentHighState);
		oldHighState = currentHighState;
		previousValue = currentValue;
		previousPhase = currentPhase;
	}

	/**
	 * Process all input data.
	 */
	private void processColumns() throws ConversionException {
		boolean keepProcessing = true;

		while (keepProcessing) {
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
		return "WindowDetectorBySlopeChange [noise-delta=" + threshold + "]";
	}
}
