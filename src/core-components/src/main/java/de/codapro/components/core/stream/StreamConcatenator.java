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

import java.util.NoSuchElementException;

import javax.inject.Named;

import org.slf4j.Logger;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnExecute;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "StreamConcat", doc = "Takes two stream and concats them.", sinks=@Input(doc="A stream of input data.", name="input-stream-1"))
public class StreamConcatenator {
	@Input(doc="A stream of input data.", name="input-stream-2")
	private Stream input2;
	
	@Log
	private Logger log;

	@OnInit
	public void compareHeaders(final @Named("input-stream-1") Stream input1, final @Named("output-stream") Stream output) {
		if (input1.getHeader().size() != input2.getHeader().size()) {
			log.error("Headers don't have the same length");
			throw new IllegalArgumentException("Headers of both input streams are not equally large.");
		}
		for(int i=0; i < input1.getHeader().size(); i++) {
			if(!input1.getHeader().nameOf(i).equals(input2.getHeader().nameOf(i))) {
				log.error("Headers are not identical.");
				throw new IllegalArgumentException("Headers of both input streams are not the same.");				
			}			
		}
		output.setHeader(input1.getHeader());
		
	}
	
	@OnExecute
	public void concatStream(final @Named("input-stream-1") Stream input1, final @Named("input-stream-2") Stream input2, final @Named("output-stream") Stream output) throws ConversionException{
		while(true) {
			try {
				output.append(input1.get()) ;
			} catch (NoSuchElementException ne) {
				//stream is empty
				break;
			}
		}
		
		while(true) {
			try {
				output.append(input2.get());
			} catch (NoSuchElementException ne2) {
				//stream is empty
				break;
			}
		}
	}
	
	
	

	

	@Override
	public String toString() {
		return "StreamConcat []";
	}
}

