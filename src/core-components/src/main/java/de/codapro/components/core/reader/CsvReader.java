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

import java.io.File;
import java.io.IOException;

import javax.inject.Named;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnExecute;
import de.codapro.api.annotations.OnFinish;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.model.Stream;
import de.codapro.components.core.utils.csv.CsvReaderBase;

/**
 * Component for reading csv files and convert them into a stream of strings.
 *
 * The column names of {@code output-stream} can either be determined by setting {@code columns-in-first-row}
 *   to {@code true} or by setting explicitly using {@code column-names}. The parameters {@code delimiter}
 *   and {@code record-separator} let you control the layout of the input file.
 *
 */
@Component(name = "CsvReader", doc = "Component for reading a CSV file and converting it into a stream of data vectors.", sinks= {})
public class CsvReader extends CsvReaderBase {

	@Input(doc="Name of the input file.", name="filename")
	private String filename;

	@OnFinish
	@Override
	public void close() throws IOException {
		super.close();
	}

	@OnExecute
	public void execute(final @Named("output-stream") Stream stream) throws ConversionException {
		copyCsvToStream(stream);
	}

	@OnInit
	public void initStream(final @Named("output-stream") Stream stream) throws IOException {
		final File inputFile = new File(filename);

		open(inputFile);
		setStreamHeader(stream, true);
		convertConversionCallbacks();
	}

	@Override
	public String toString() {
		return "CsvReader [filename=" + filename + "]";
	}
}
