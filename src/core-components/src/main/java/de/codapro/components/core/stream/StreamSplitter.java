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

import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.Output;
import de.codapro.api.model.Stream;

@Component(doc= "Splits a stream into several output-streams depending on column input, "
		+ "so that the same or different actions can be performed on it", name = "StreamSplitter")
public class StreamSplitter {
	
	@ColumnId(doc = "Column deciding split", name= "column-of-interest", stream= "input-stream" )
	private int colId;
	
	@Input(doc="Markers for splitted streams", name="streams-to-create", required = true )
	private String[] streamMarker = {};
	
	//@Output(doc="Array containing output streams.")
	//private Stream[] outputStreams;
	
	
	
	
	@OnInit()
	public void copyHeaders(final @Named("input-stream") Stream stream) {
		//outputStream.setHeader(stream.getHeader()).add(nameOfColumn);
	}

}
