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

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;

import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.utils.FileUtils;

@Component(name = "ChartWriter", doc = "Saves a chart.", sources={})
public class ChartSaver {
	@Log
	private static Logger log;

	@Input(doc = "Name pattern for the output files. Make sure to include a format string for integers, such as chart-%03d.png", name = "filename")
	private String filename;

	private int fileNumberSuffix = 0;

	@Input(doc = "Width of image", name = "width", required = false)
	private int width = 640;

	@Input(doc = "Height of image.", name = "height", required = false)
	private int height = 480;

	@ColumnId(doc = "Column containing the chart.", name = "chart-column", stream="input-stream")
	private int chartColumnIndex;

	@OnInit
	public void createOutputDirectory() {
		FileUtils.createBaseDirectory(new File(filename));
	}

	@OnProcess(dest={})
	public void saveChart(final DataVector vector) {
		final JFreeChart chart = (JFreeChart)vector.get(chartColumnIndex);

		final String nextFilename = String.format(filename, fileNumberSuffix++);
		final File chartFile = new File(nextFilename);

		try {
			log.info("Writing chart to {}.", chartFile);
			ChartUtils.saveChartAsPNG(chartFile, chart, width, height);
		} catch (IOException e) {
			log.error("Failed to save chart {}.", chartFile);
		}
	}

	@Override
	public String toString() {
		return "ChartSaver [filename=" + filename + "]";
	}
}
