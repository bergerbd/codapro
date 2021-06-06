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
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.TextAnchor;

import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.annotations.Output;
import de.codapro.api.model.DataVector;

@Component(	name	= "XYAnnotation",
			doc 	= "Component for adding an annotation to a XY chart.",
			sources	= {
					@Output(name="output-stream", doc="The component's output stream"),
					@Output(name="annotation-output-stream", doc="The output stream containing the annotations.")
			},
			sinks	= {
					@Input(name="input-stream", doc="The component's input stream.", required=true),
					@Input(name="annotation-input-stream", doc="The input stream containing the annotations.", required=true)
			})
public class XYAnnotation {
	@ColumnId(doc = "Column containing the chart.", name = "chart-column", stream="input-stream")
	private int chartColumn;

	@ColumnId(doc = "Column containing the value to add.", name = "value-columns", stream="annotation-input-stream")
	private int [] valueColumns;

	@Input(doc = "Value texts (including a %s value).", name = "value-texts")
	private String [] valueTexts;

	@OnProcess(value= {"input-stream", "annotation-input-stream"}, dest= {"output-stream", "annotation-output-stream"})
	public void createAnnotation(final DataVector chartVector, final DataVector annotationVector) {
		final JFreeChart chart = (JFreeChart)chartVector.get(chartColumn);
		final XYPlot plot = chart.getXYPlot();

		for(int i = 0; i < valueColumns.length; ++i) {
			final Object obj = annotationVector.get(valueColumns[i]);

			double x = plot.getDomainAxis().getRange().getLowerBound() + 0.02 * plot.getDomainAxis().getRange().getLength();

			double y = plot.getRangeAxis().getRange().getUpperBound() - 0.03 * (i + 1) * plot.getRangeAxis().getRange().getLength();

			final XYTextAnnotation annotation = new XYTextAnnotation(String.format(valueTexts[i], obj), x, y);
			annotation.setTextAnchor(TextAnchor.BASELINE_LEFT);

			plot.addAnnotation(annotation);
		}
	}

	@Override
	public String toString() {
		return "XYAnnotation [valueColumn=" + valueColumns + "]";
	}

}
