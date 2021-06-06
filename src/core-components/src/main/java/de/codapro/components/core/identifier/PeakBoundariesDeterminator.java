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

import javax.inject.Named;

import org.slf4j.Logger;

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
import de.codapro.api.model.Value;

@Component(name = "PeakBoundariesDeterminator", doc = "Determines data descriptors.")
public class PeakBoundariesDeterminator {
	/**
	 * All column names for the descriptor stream.
	 */
	private static final String[] COLUMN_NAMES = new String[] { "starting time", "ending time", "length", "lower sum",
			"upper sum", "max peak" };

	private static final int ENDING_TIME_INDEX = 1;
	private static final int LENGTH_INDEX = 2;
	private static final int LOWER_SUM_INDEX = 3;
	private static final int MAX_INDEX = 5;
	private static final int STARTING_TIME_INDEX = 0;
	private static final int UPPER_SUM_INDEX = 4;

	@Log
	private static Logger log;

	/**
	 * The current descriptor's data vector
	 */
	private DataVector descriptor;

	@Output(doc = "A stream containing the descriptor data.", name = "descriptor-stream")
	private Stream descriptorStream;

	/**
	 * Peak ending time
	 */
	private double end = 0.0;

	/**
	 * Length of peak,
	 */
	private int length = 0;

	/**
	 * Peak max value.
	 */
	private double maxValue = 0.0;

	/**
	 * Over sum
	 */
	private double over = 0.0;

	@ColumnId(doc = "The name of the column storing the data being analyzed.", name = "peak-data", stream = "input-stream")
	private int peakIndex;

	@Input(doc = "The value for peak starts.", name = "peak-start-value", type = Double.class)
	private Value<Double> peakThreshold;

	/**
	 * Previous value for peak detection
	 */
	private double previousValue;

	/**
	 * Peak starting time
	 */
	private double start = 0.0;

	@ColumnId(doc = "The name of the column storing time data.", name = "time-data", stream = "input-stream")
	private int timeIndex;

	/**
	 * Under sum
	 */
	private double under = 0.0;

	/**
	 * If the last value was over the peak threshold.
	 */
	private boolean wasLastValueInPeak = false;

	@OnInit
	public void addColumnNamesToOutputStream(final @Named("peak-data") String peakColumnName) {
		for (final String column : COLUMN_NAMES) {
			descriptorStream.getHeader().add(peakColumnName + " " + column);
		}
	}

	@OnProcess
	public void calculateDescriptors(final DataVector vector) {
		final boolean isCurrentValueInPeak = isValueOverThreshold(vector);

		if (wasLastValueInPeak && isCurrentValueInPeak) {
			recalculateDescriptorValues(vector);
		} else if (!wasLastValueInPeak && isCurrentValueInPeak) {
			setInitialValues(vector);
		} else if (wasLastValueInPeak && !isCurrentValueInPeak && isCurrentPeakHigherThanLast()) {
			replacePeakValues();
		}

		wasLastValueInPeak = isCurrentValueInPeak;
		previousValue = ((Number) vector.get(peakIndex)).doubleValue();
	}

	@OnLeaveGroup
	public void copyDescriptorToOutputStream() {
		try {
			descriptorStream.append(descriptor);
		} catch (ConversionException e) {
			log.error("Failed to write to the descriptor stream.", e);
		}
		wasLastValueInPeak = false;
	}

	private boolean isCurrentPeakHigherThanLast() {
		log.info("Checking if current peak {} is higher than last one {}.", this.maxValue,
				((Number) descriptor.get(MAX_INDEX)).doubleValue());
		return this.maxValue > ((Number) descriptor.get(MAX_INDEX)).doubleValue();
	}

	/**
	 * @return Iff the current value is over the threshold.
	 */
	private boolean isValueOverThreshold(final DataVector vector) {
		return ((Number) vector.get(peakIndex)).doubleValue() > peakThreshold.get();
	}

	private void recalculateDescriptorValues(DataVector vector) {
		double currentValue = ((Number) vector.get(peakIndex)).doubleValue();
		double timeData = ((Number) vector.get(timeIndex)).doubleValue();

		this.under = this.under + (timeData - this.end) * Math.min(previousValue, currentValue);
		this.over = this.over + (timeData - this.end) * Math.max(previousValue, currentValue);
		this.end = timeData;
		this.length++;
		this.maxValue = Math.max(this.maxValue, ((Number) vector.get(peakIndex)).doubleValue());

	}

	/**
	 * Replaces the peak attributes to the actual values.
	 */
	private void replacePeakValues() {
		log.info("Replacing peak values from {} to {}", descriptor.get(MAX_INDEX), maxValue);
		descriptor.set(STARTING_TIME_INDEX, start);
		descriptor.set(ENDING_TIME_INDEX, end);
		descriptor.set(LENGTH_INDEX, length);
		descriptor.set(LOWER_SUM_INDEX, under);
		descriptor.set(UPPER_SUM_INDEX, over);
		descriptor.set(MAX_INDEX, maxValue);
	}

	private void setInitialValues(final DataVector vector) {
		double currentValue = ((Number) vector.get(peakIndex)).doubleValue();
		double timeData = ((Number) vector.get(timeIndex)).doubleValue();

		this.start = timeData;
		this.end = timeData;
		this.under = 0.0;
		this.over = 0.0;
		this.length = 1;
		this.maxValue = currentValue;
	}

	@OnEnterGroup
	public void startGroup() {
		log.info("Starting new group");
		descriptor = new DataVector(COLUMN_NAMES.length);

		this.start = 0.0;
		this.end = 0.0;
		this.under = 0.0;
		this.over = 0.0;
		this.length = 1;
		this.maxValue = 0;

		replacePeakValues();
	}

	@Override
	public String toString() {
		return "DescriptorDetermination [peakStartValue=" + peakThreshold + "]";
	}
}
