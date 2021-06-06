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

import org.slf4j.Logger;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnEnterGroup;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnLeaveGroup;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.annotations.Output;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "StreamCopyCreator",
			doc = "Takes an input stream and create two similar output streams.",
			sources= {@Output(doc="First output stream containing all input values.", name="output-stream-1"),
					  @Output(doc="Second output stream containing all input values.", name="output-stream-2")})
public class StreamCopyCreator {
	@Log
	private static Logger log;

	@Named("output-stream-2")
	private Stream outputStream2;

	@OnEnterGroup
	public void enterGroup() {
		try {
			outputStream2.append(DataVector.GROUP_START);
		} catch (ConversionException e) {
			log.error("Unable to add start vector to second stream.");
		}
	}

	@OnLeaveGroup
	public void leaveGroup() {
		try {
			outputStream2.append(DataVector.GROUP_END);
		} catch (ConversionException e) {
			log.error("Unable to add end vector to second stream.");
		}
	}

	@OnProcess(value="input-stream", dest="output-stream-1")
	public void process(final DataVector vector) throws CloneNotSupportedException {
		try {
			outputStream2.append((DataVector)vector.clone());
		} catch (ConversionException e) {
			log.error("Unable to add vector {} to second stream.", vector);
		}
	}

	@OnInit
	public void setStreamHeader(@Named("input-stream") Stream inputStream) {
		outputStream2.setHeader(inputStream.getHeader());
	}

	@Override
	public String toString() {
		return "CopyStreamReadOnly []";
	}
}
