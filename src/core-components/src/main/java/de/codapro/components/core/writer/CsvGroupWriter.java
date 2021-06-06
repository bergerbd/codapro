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
package de.codapro.components.core.writer;

import java.io.File;

import javax.inject.Named;

import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnEnterGroup;
import de.codapro.api.annotations.OnLeaveGroup;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;
import de.codapro.components.core.utils.csv.CsvWriterBase;

@Component(	name = "CsvGroupWriter",
			doc = "Writes each data vector group into a separate file.",
			sources= {})
public class CsvGroupWriter extends CsvWriterBase {
	@Input(doc="Name of the input file. Make sure to inclue a format string for integers, such as part-%03d.csv", name="filename")
	private String filename;

	private int fileNumberSuffix = 0;

	/**
	 * @return iff we are currently writing.
	 */
	private boolean isWriting() {
		return writer != null;
	}

	@OnEnterGroup
	public void openNextCsv(final @Named("input-stream") Stream stream) {
		initColumnIds(stream);

		final String nextFilename = String.format(filename, fileNumberSuffix++);
		open(stream, new File(nextFilename));
	}

	@OnLeaveGroup
	public void closeCurrentCsv() {
		close();
	}

	@Override
	public String toString() {
		return "CsvGroupWriter [filename=" + filename + "]";
	}

	@OnProcess(dest={})
	public void writeVector(final DataVector vector) {
		if(isWriting()) {
			write(vector);
		}
	}
}
