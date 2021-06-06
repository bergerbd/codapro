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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import javax.inject.Named;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnEnterGroup;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnLeaveGroup;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "XYGroupChart", doc = "Creates a XY-chart for each data group.")
public class XYGroupChart {
	private static final String LABEL_COLOR = "#666666";

	private XYSeries[] dataSeries;

	private int chartSuffix = 0;

	@ColumnId(doc = "Column index of the x-value", name = "x-data", stream="input-stream")
	private int xDataIndex;

	@Input(doc = "Name of the x-axis.", name = "x-axis-label")
	private String xLabel = "x-data";

	@ColumnId(doc = "Column names of the y-values", name = "y-data", stream="input-stream")
	private int [] yDataIndices;

	@Input(doc = "Name of the y-axis.", name = "y-axis-label")
	private String yLabel = "y-data";

	@Input(doc = "Name of the generated chart column.", name = "chart-name")
	private String chartColumnName;

	@Input(doc = "Name of font to use.", name = "font-name")
	private String fontName = "Helvetica";

	@OnInit
	public void initColumnIndices(final @Named("output-stream") Stream output) {
		output.getHeader().add(chartColumnName);
	}

	@OnLeaveGroup
	public void finishChart(final @Named("output-stream") Stream stream) throws ConversionException {
		final XYSeriesCollection dataset = new XYSeriesCollection();
		for (final XYSeries series : dataSeries) {
			dataset.addSeries(series);
		}


		final StandardChartTheme theme = (StandardChartTheme) org.jfree.chart.StandardChartTheme.createJFreeTheme();

		theme.setExtraLargeFont(new Font(fontName, Font.BOLD, 20)); // title
		theme.setLargeFont(new Font(fontName, Font.BOLD, 15)); // axis-title
		theme.setRegularFont(new Font(fontName, Font.PLAIN, 11));
		theme.setRangeGridlinePaint(Color.decode("#C0C0C0"));
		theme.setPlotBackgroundPaint(Color.white);
		theme.setChartBackgroundPaint(Color.white);
		theme.setGridBandPaint(Color.red);
		theme.setAxisOffset(new RectangleInsets(10, 0, 0, 0));
		theme.setAxisLabelPaint(Color.decode(LABEL_COLOR));

		final JFreeChart xyLineChart = ChartFactory.createXYLineChart(String.format("Group %03d", chartSuffix++),
				xLabel, yLabel, dataset, PlotOrientation.VERTICAL, true, true, false);

		theme.apply(xyLineChart);

		xyLineChart.setPadding(new RectangleInsets(10,10,10,10));
		xyLineChart.getXYPlot().setOutlineVisible(false);
		xyLineChart.getXYPlot().getRangeAxis().setAxisLineVisible(false);
		xyLineChart.getXYPlot().getRangeAxis().setTickMarksVisible(false);
		xyLineChart.getXYPlot().getRangeAxis().setMinorTickCount(0);
		xyLineChart.getXYPlot().getRangeAxis().setMinorTickMarksVisible(false);
		xyLineChart.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		xyLineChart.getXYPlot().setRangeGridlineStroke(new BasicStroke());
		xyLineChart.getXYPlot().getRangeAxis().setTickLabelPaint(Color.decode(LABEL_COLOR));
		xyLineChart.getXYPlot().getDomainAxis().setTickLabelPaint(Color.decode(LABEL_COLOR));
		xyLineChart.setTextAntiAlias(true);
		xyLineChart.setAntiAlias(true);
		xyLineChart.getXYPlot().getRenderer().setSeriesPaint(0, Color.decode("#4572a7"));

		final DataVector vector = new DataVector();
		vector.append(xyLineChart);
		stream.append(vector);
	}

	@OnProcess(dest={})
	public void process(final DataVector vector) {
		final Number xValue = (Number) vector.get(xDataIndex);

		for (int index = 0; index < yDataIndices.length; ++index) {
			int yIndex = yDataIndices[index];
			Number yValue = (Number) vector.get(yIndex);

			dataSeries[index].add(xValue, yValue, false);
		}
	}

	@OnEnterGroup
	public void startChart(final @Named("input-stream") Stream stream) {
		dataSeries = new XYSeries[yDataIndices.length];

		for (int index = 0; index < yDataIndices.length; ++index) {
			dataSeries[index] = new XYSeries(stream.getHeader().nameOf(yDataIndices[index]));
		}
	}

	@Override
	public String toString() {
		return "XYGroupChart []";
	}
}
