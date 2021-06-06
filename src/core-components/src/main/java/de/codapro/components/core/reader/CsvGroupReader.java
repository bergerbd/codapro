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
package de.codapro.components.core.reader;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;
import de.codapro.components.core.utils.csv.CsvReaderBase;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;
import org.slf4j.Logger;

@Component(name = "CsvGroupReader", doc = "Put groups back together into a stream, with start - end tags")
public class CsvGroupReader extends CsvReaderBase {

	@ColumnId(doc = "value to choose after", name = "column-of-interest", stream = "input-stream")
	private int columnIdOfInterest;

	@Input(doc = "Name of the input file. Make sure to inclue a format string for integers, such as group-%03d.csv", name = "filename")
	private String filename;

	@Log
	private Logger log;

	private @Named("output-stream") Stream stream;

	private void openCsv(final int filenameSuffix) throws IOException {
		final String calcFilename = String.format(filename, filenameSuffix);
		log.info("Opening file {}.", calcFilename);
		final File inputFile = new File(calcFilename);

		open(inputFile);
		setStreamHeader(stream);
	}

	@OnProcess(dest = {})
	public void process(final DataVector vector) throws IOException, ConversionException {
		try {
			final int filenameSuffix = ((Number) vector.get(columnIdOfInterest)).intValue();

			openCsv(filenameSuffix);
			stream.append(DataVector.GROUP_START);
			copyCsvToStream(stream);
			stream.append(DataVector.GROUP_END);
		} finally {
			close();
		}
	}

	@OnInit
	public void setStreamHeader(@Named("output-stream") Stream stream) {
		log.info("Output stream is {}  with header {}.", stream, stream.getHeader());
		super.setStreamHeader(stream, false);

		convertConversionCallbacks();
	}

	@Override
	public String toString() {
		return "CsvGroupReader [filename=" + filename + "]";
	}
}
