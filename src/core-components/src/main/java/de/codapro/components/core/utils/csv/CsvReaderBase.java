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
package de.codapro.components.core.utils.csv;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.rmi.AccessException;
import java.util.Iterator;
import java.util.Locale;
import java.util.function.Function;

import de.codapro.components.core.utils.StringConverter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.Input;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

/**
 * Base class for reading csv's.
 */
public abstract class CsvReaderBase {
	private static final Logger log = LoggerFactory.getLogger(CsvReaderBase.class); 

	@Input(doc = "If there are no column names in the file.", name = "column-names", required = false)
	private String[] columnNames = {};

	@Input(doc = "The actual column types.", name = "column-types", required = false)
	private String [] columnTypes = null;

	/**
	 * Array of type conversions.
	 */
	private Function<String, Object> [] conversions = null;

	@Input(doc = "The field delimiter (e.g. ',', ';' or '\\t').", name = "delimiter", required = false)
	private String delimiter = ";";

	@Input(doc = "If the first row contains column names.", name = "columns-in-first-row", required = false)
	private boolean firstRowContainsHeader = false;

	private Iterator<CSVRecord> iterator;

	@Input(doc="Locale used for parsing numeric values.", name="locale", required = false)
	private Locale locale = Locale.getDefault();

	private CSVParser parser;

	@Input(doc = "The line separator that is used (Mac, Unix or Windows).", name = "record-separator", required = false)
	private RecordSeparator recordSeparator = RecordSeparator.Unix;

	/**
	 * Closes the current parser instance
	 */
	protected void close() throws IOException {
		parser.close();
	}

	/**
	 * Converts the column types to conversion callbacks
	 */
	protected void convertConversionCallbacks() {
		if(columnTypes != null) {
			log.info("Creating conversions.");
			conversions = new Function [columnTypes.length];
			for(int i = 0; i < columnTypes.length; ++i) {
				conversions[i] = StringConverter.getFunction(columnTypes[i], locale);
			}
		}
	}

	/**
	 * Copies all data set from CSV to the stream
	 */
	protected void copyCsvToStream(final Stream stream) throws ConversionException {
		final int streamSize = stream.getHeader().size();
		int counter = 0;

		while(iterator.hasNext()) {
			stream.append(toDataVector(iterator.next(), streamSize));
			counter = counter + 1;
		}

		log.info("Read {} data vectors from CSV.", counter);
	}

	/**
	 * Opens a CSV file
	 */
	protected void open(final File csvFile) throws IOException {
		if(!csvFile.exists()) {
			File current = csvFile;

			do {
				log.info("Visiting {} - {}", current, current.exists());

				if(current.exists()) {
					for(String content : current.list()) {
						log.info("  contains {}", content	);
					}
				}

				current = current.getParentFile();
			} while(current != null);
			throw new FileNotFoundException("Could not find " + csvFile + ".");
		}

		if(!csvFile.isFile()) {
			throw new IllegalArgumentException("Input file " + csvFile + " is not a file.");
		}

		if(!csvFile.canRead()) {
			throw new AccessException("Cannot read input file " + csvFile);
		}

		final InputStream fis = new FileInputStream(csvFile);
		final InputStream bis = new BufferedInputStream(fis);
		final CSVFormat format = CSVFormat.newFormat(delimiter.charAt(0)).withRecordSeparator(recordSeparator.getSeparator());
		parser = CSVParser.parse(bis, Charset.defaultCharset(), format);
		iterator = parser.iterator();
	}

	private boolean headerAlreadySet = false;

	protected void setStreamHeader(final Stream stream, final boolean respectFileHeader) {
		if(firstRowContainsHeader && respectFileHeader) {
			final CSVRecord record = iterator.next();
			for(final String column : record) {
				stream.getHeader().add(column);
			}
		} else if(columnNames != null && !headerAlreadySet) {
			stream.getHeader().add(columnNames);
		}

		if(firstRowContainsHeader && !respectFileHeader && iterator != null) {
			// skip header row
			iterator.next();
		}

		headerAlreadySet = true;
	}

	/**
	 * Converts a csv record into a valid data record.
	 */
	protected DataVector toDataVector(final CSVRecord record, final int streamSize) {
		int recordSize = record.size();
		if(recordSize > streamSize) {
		    recordSize = streamSize; // truncate records to size of the resulting stream
		}

		final DataVector vector = new DataVector(recordSize);

		if(conversions != null) {
			for(int i = 0; i < conversions.length; ++i) {
				final String stringValue = record.get(i);
				final Object convertedValue = conversions[i].apply(stringValue);

				vector.set(i, convertedValue);
			}
		} else {
			vector.replace(record.iterator());
		}

		return vector;
	}
}
