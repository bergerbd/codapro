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
package de.codapro.components.charts;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;

import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Value;

@Component(name = "RangeSetter", doc = "Sets the range of the x axis.")
public class RangeSetter {
	@Input(doc = "The maximum value for the y-axis.", name = "y-max", type = Double.class)
	private Value<Double> yMax;

	@Input(doc = "The minimum value for the y-axis.", name = "y-min", type = Double.class)
	private Value<Double> yMin;

	@ColumnId(doc = "Column containing the chart.", name = "chart-column", stream="input-stream")
	private int chartColumnIndex;

	@OnProcess
	public void setRange(final DataVector vector) {
		final JFreeChart chart = (JFreeChart)vector.get(chartColumnIndex);

		final NumberAxis range = (NumberAxis) ((XYPlot)chart.getPlot()).getRangeAxis();
		range.setRange(yMin.get(), yMax.get());
	}

	@Override
	public String toString() {
		return "RangeSetter [x-min=" + yMin + ", y-max=" + yMax + "]";
	}
}
