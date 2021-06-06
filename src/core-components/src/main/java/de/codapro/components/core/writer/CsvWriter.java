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
import de.codapro.api.annotations.OnFinish;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;
import de.codapro.components.core.utils.csv.CsvWriterBase;

@Component(
		name	= "CsvWriter",
		doc		= "Writes a vector stream into a csv file.",
		sources	= {}
)
public class CsvWriter extends CsvWriterBase {
	@Input(doc="Name of the input file.", name="filename")
	public String filename;

	@OnFinish
	public void closePrinter() {
		close();
	}

	@OnInit
	public void createPrinter(final @Named("input-stream") Stream stream) {
		initColumnIds(stream);
		open(stream, new File(filename));
	}

	@OnProcess(dest={})
	public void process(final DataVector vector) {
		write(vector);
	}

	@Override
	public String toString() {
		return "CsvWriter [filename=" + filename + "]";
	}
}
