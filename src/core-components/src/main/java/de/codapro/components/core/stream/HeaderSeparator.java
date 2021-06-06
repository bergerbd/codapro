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

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.annotations.Output;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "HeaderSeparator", doc = "Splices a data stream into a fixed length header and a body.")
public class HeaderSeparator {
	@Input(doc="The size of the header.", name="header-size")
	private int headerSize = 100;

	@Output(doc="A stream containing all header rows.", name="header-stream")
	private Stream headerStream;

	@Named("output-stream")
	private Stream outputStream;

	private int columnIndex = 0;

	@OnInit
	public void copyHeaders(final @Named("input-stream") Stream stream) {
		headerStream.setHeader(stream.getHeader());
		outputStream.setHeader(stream.getHeader());
	}

	@OnProcess(dest={})
	public void execute(final DataVector vector) throws ConversionException {
		boolean isHeader = columnIndex < headerSize;

		if(isHeader) {
			headerStream.append(vector);
			if(++columnIndex >= headerSize) {
				headerStream.markClosed();
			}
		} else {
			outputStream.append(vector);
		}
	}

	@Override
	public String toString() {
		return "HeaderSeparator [headerSize=" + headerSize + "]";
	}
}
