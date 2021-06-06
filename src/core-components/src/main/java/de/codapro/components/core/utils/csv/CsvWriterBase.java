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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;

import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;
import de.codapro.api.utils.FileUtils;

public abstract class CsvWriterBase {
	@Input(doc="The field delimiter (e.g. ',', ';' or '\\t').", name="delimiter")
	private String delimiter = ";";

	@Log
	protected Logger log;

	/**
	 * The printer for writing csv
	 */
	private CSVPrinter printer;

	@Input(doc="The line separator that is used (Mac, Unix or Windows).", name="record-separator")
	private RecordSeparator recordSeparator = RecordSeparator.Unix;

	@Input(doc="Wether to write the column headers.", name="write-header")
	private Boolean writeHeader = true;

	@ColumnId(doc = "Columns you want to write (leave empty if you want to write all columns)", name = "column-names", stream = "input-stream", required = false)
	private int [] columnIds;

	@Input(doc="Locale used for printing numeric values.", name="locale", required = false)
	private Locale locale = Locale.getDefault();

	@Input(doc = "Normalize values to non-scientific writing.", name = "normalize", required = false)
	private boolean normalize = false;

	/**
	 * The underlying file stream.
	 */
	protected Writer writer;

	protected void close() {
		try {
			printer.flush();
			writer.flush();

			printer.close();
			writer.close();

			printer = null;
			writer = null;
		} catch (final IOException e) {
			log.error("Failed to close output stream.", e);
		}
	}

	protected void initColumnIds(final Stream stream) {
		if(columnIds == null) {
			columnIds = new int [stream.getHeader().size()];
			for(int i = 0; i < stream.getHeader().size(); ++i) {
				columnIds[i] = i;
			}
		}
	}

	protected void open(final Stream stream, final File csvFile) {
		log.info("Creating new output file {}.", csvFile);

		try {
			FileUtils.createBaseDirectory(csvFile);

			if(csvFile.exists()) {
				log.info("Output file {} already exists. Overwriting it.", csvFile);
			}

			final CSVFormat format = CSVFormat.newFormat(delimiter.charAt(0)).withRecordSeparator(recordSeparator.getSeparator());

			writer = new BufferedWriter(new FileWriter(csvFile));
			printer = new CSVPrinter(writer, format);

			if(writeHeader) {
				for(int i = 0; i < columnIds.length; ++i) {
					printer.print(stream.getHeader().nameOf(columnIds[i]));
				}
				printer.println();
			}
		} catch(final IOException e) {
			log.error("Failed to open {} for writing.", csvFile, e);
			writer = null;
			printer = null;
		}
	}

	protected void write(final DataVector vector) {
		try {
			for(int index : columnIds) {
				Object value = vector.get(index);

				if(value instanceof Float || value instanceof Double) {
					if(normalize) {
						if(value instanceof Float) {
							value = String.format(locale, "%.8f", value);
						} else {
							value = String.format(locale, "%.17f", value);						}
					} else {
						value = String.format(locale, "%f", value);
					}
				}

				printer.print(value);
			}
			printer.println();
		} catch (final IOException e) {
			log.info("Failed to write vector {} to file.", vector, e);
		}
	}
}
