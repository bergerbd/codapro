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
package de.codapro.components.core.stream;

import javax.inject.Named;

import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "RowMerger", doc = "Takes two streams and merges their rows.", sinks=@Input(doc="A stream of input data.", name="input-stream-1"))
public class RowMerger {
	@Input(doc="A stream of input data.", name="input-stream-2")
	private Stream inputStream2;

	@OnInit
	public void setHeaders(final @Named("input-stream-1") Stream input1, final @Named("output-stream") Stream output) {
		output.setHeader(input1.getHeader());
		for(final String name : inputStream2.getHeader()) {
			output.getHeader().add(name);
		}
	}

	@OnProcess(value={"input-stream-1"}, dest={"output-stream"})
	public void mergeRow(final DataVector vector) {
		final DataVector other = inputStream2.get();
		vector.appendAll(other.getAll());
	}

	@Override
	public String toString() {
		return "RowMerger []";
	}
}
