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

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.AccessException;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnExecute;
import de.codapro.api.annotations.OnFinish;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;
import de.codapro.components.core.utils.StringConverter;
import de.codapro.components.core.utils.csv.RecordSeparator;
import de.codapro.components.core.utils.text.FieldFormat;

@Component(name = "TextFileReader", doc = "Component for reading a field-based text file and converting it into a stream of data vectors.", sinks= {})
public class TextFileReader {
	private static final Logger log = LoggerFactory.getLogger(TextFileReader.class);

	@Input(doc = "If there are no column names in the file.", name = "column-names", required = false)
	private String[] columnNames = {};

	/**
	 * Array of type conversions.
	 */
	private Function<String, Object> [] conversions = null;

	/**
	 * Buffered reader for reading stuff.
	 */
	private BufferedReader data;

	@Input(doc = "File format specifiers, e.g. 20-%b", name = "format")
	private FieldFormat [] fileFormat = {};

	@Input(doc="Name of the input file.", name="filename")
	private String filename;

	@Input(doc = "Number of rows containging column names.", name = "header-rows", required = false)
	private int headerRows = 1;

	@Input(doc="Locale used for parsing numeric values.", name="locale", required = false)
	private Locale locale = Locale.getDefault();

	@Input(doc = "The line separator that is used (Mac, Unix or Windows).", name = "record-separator", required = false)
	private RecordSeparator recordSeparator = RecordSeparator.Unix;

	private long lineLength;

	@OnFinish
	public void closeInput() throws IOException {
		data.close();
	}

	private void convertConversionCallbacks() {
		conversions = new Function [fileFormat.length];
		for(int i = 0; i < fileFormat.length; ++i) {
			conversions[i] = StringConverter.getFunction(fileFormat[i].getType(), locale);
		}

		lineLength = Arrays.stream(fileFormat)
						   .collect(Collectors.summarizingInt(ff -> ff.getLength()))
						   .getSum();
	}

	@OnExecute
	public void copyCsvToStream(final @Named("output-stream") Stream stream) throws ConversionException, IOException {
		int counter = 0;
		int errorCounter = 0;
		boolean isNotEof = true;

		while(isNotEof) {
			try {
				final String [] line = readLine();
				final DataVector vector = new DataVector(line.length);

				for(int i = 0; i < line.length; ++i) {
					vector.set(i, conversions[i].apply(line[i]));
				}

				stream.append(vector);
				counter = counter + 1;
			} catch(final IllegalStateException e) {
				errorCounter = errorCounter + 1; // Formating error
			} catch(final EOFException e) {
				isNotEof = false;
			}
		}

		log.info("Read {} data vectors from text file.", counter);
	}

	@OnInit
	public void initializeStream(final @Named("output-stream") Stream stream) throws IOException {
		openTextFile();

		convertConversionCallbacks();
		setStreamHeader(stream);
	}

	private void openTextFile() throws IOException {
		final File inputFile = new File(filename);

		if(!inputFile.exists()) {
			throw new FileNotFoundException("Could not find " + inputFile + ".");
		}

		if(!inputFile.isFile()) {
			throw new IllegalArgumentException("Input file " + inputFile + " is not a file.");
		}

		if(!inputFile.canRead()) {
			throw new AccessException("Cannot read input file " + inputFile);
		}

		data = Files.newBufferedReader(inputFile.toPath());
	}

	private String[] readLine() throws IOException {
		final int length = fileFormat.length;
		final String [] row = new String[length];

		String line = data.readLine();

		if(line == null) {
			throw new EOFException("Reached end of file.");
		}

		if(line.length() != lineLength) {
			log.error("Line length ({}) does not math expected line length ({}).", line.length(), lineLength);
			throw new IllegalStateException("Line length does not match.");
		}

		for(int i = 0; i < length; ++i) {
			row[i] = line.substring(0, fileFormat[i].getLength()).trim();
			line = line.substring(fileFormat[i].getLength());
		}

		return row;
	}

	private void setStreamHeader(final Stream stream) throws IOException {
		if(headerRows == 0) {
			// there are no row headers. We have to use the given ones.
			stream.getHeader().add(columnNames);
		} else {
			final int length = fileFormat.length;

			final String [] headers = readLine();

			for(int r = 1; r < headerRows; ++r) {
				final String row [] = readLine();

				for(int i = 0; i < length; ++i) {
					headers[i] = headers[i] + " " + row[i];
				}
			}

			stream.getHeader().add(headers);
		}
	}
}
