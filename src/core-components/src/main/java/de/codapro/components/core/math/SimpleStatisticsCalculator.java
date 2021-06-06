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

import org.slf4j.Logger;

import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnFinish;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.annotations.Output;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Value;

@Component(name = "SimpleStatisticsCalculator", doc = "Calculate simple statistics, such as mean, variance, max and min for a given data stream.", sources = {})
public class SimpleStatisticsCalculator {
	@Log
	private static Logger log;

	@ColumnId(doc = "The column number for peak detection.", name = "column", stream = "input-stream")
	private int dataIndex;

	/**
	 * Maximum value
	 */
	@Output(doc = "The maximum value.", name = "max")
	private Value<Double> maxValue;

	/**
	 * Mean value
	 */
	@Output(doc = "The mean value.", name = "mean")
	private Value<Double> meanValue;

	/**
	 * Minimum value
	 */
	@Output(doc = "The minimum value.", name = "min")
	private Value<Double> minValue;

	/**
	 * Variance value
	 */
	@Output(doc = "The variance value.", name = "variance")
	private Value<Double> varianceValue;

	private double mean = 0.0f;
	private double max = Double.NEGATIVE_INFINITY;
	private double min = Double.POSITIVE_INFINITY;
	private int count = 0;

	private double f1 = 0.0f;
	private double f2 = 0.0f;

	@OnProcess(dest = {})
	public void process(final DataVector vector) {
		final Object val = vector.get(dataIndex);

		// count the number of data sets
		count = count + 1;

		// extract value of row
		double value = 0.0f;
		if (val instanceof Number) {
			value = ((Number) val).doubleValue();
		} else {
			log.error("No numeric value for statistics calculation: {}.", val);
		}

		mean += value;
		max = Math.max(max, value);
		min = Math.min(min, value);

		f1 += Math.pow(value, 2);
		f2 += value;
	}

	@OnFinish
	public void setConstants() {
		mean /= count;
		float variance = (float) (f1 - 2 * mean * f2 + count * Math.pow(mean, 2)) / count;

		log.info("Noise mean is {}.", mean);
		log.info("Noise variance is {}.", variance);
		log.info("Noise max is {}.", max);
		log.info("Noise min is {}.", min);

		meanValue.set((double) mean);
		minValue.set((double) min);
		maxValue.set((double) max);
		varianceValue.set((double) variance);
	}

	@Override
	public String toString() {
		return "SimpleStatistics [column-number=" + dataIndex + "]";
	}
}
