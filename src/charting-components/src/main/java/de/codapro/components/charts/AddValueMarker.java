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

import java.awt.Color;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;

import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.annotations.Output;
import de.codapro.api.model.DataVector;

@Component(name		= "ValueMarker",
			doc		= "Adds an marker.",
			sinks	= {
						@Input(name="input-stream", doc="The component's input stream.", required=true),
						@Input(doc = "A stream containing marker values.", name = "marker-input-stream")
					  },
			sources	= {
						@Output(name="output-stream", doc="The component's output stream"),
						@Output(doc = "The marker output stream.", name = "marker-output-stream")
					  })
public class AddValueMarker {
	@Input(doc = "The color of the marker.", name = "marker-colors")
	private Color [] markerColor;

	@ColumnId(doc = "Column containing the chart.", name = "chart-column", stream="input-stream")
	private int chartColumnIndex;

	@ColumnId(doc = "Columns containing the marker value.", name = "marker-columns", stream="marker-input-stream")
	private int [] markerColumnIndices;

	@OnProcess(value = {"input-stream", "marker-input-stream"}, dest = {"output-stream", "marker-output-stream"})
	public void addMarker(final DataVector chartVector, final DataVector markerVector) {
		final JFreeChart chart = (JFreeChart)chartVector.get(chartColumnIndex);

		for(int index = 0; index < markerColumnIndices.length; ++index) {
			final ValueMarker marker = new ValueMarker(((Number)markerVector.get(markerColumnIndices[index])).doubleValue());
			marker.setPaint(markerColor[index]);

			final XYPlot plot = (XYPlot)chart.getPlot();
			plot.addDomainMarker(marker);
		}
	}

	@Override
	public String toString() {
		return "ValueMarker []";
	}
}
