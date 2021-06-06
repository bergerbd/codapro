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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.inject.Named;

import org.slf4j.Logger;

import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.api.ConversionException;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnExecute;
import de.codapro.api.annotations.OnFinish;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

/**
 * Component for reading tdms files and convert them into a DataVector stream.
 */
@Component(name = "TDMsReader", doc = "Component for reading a TDMs file and converting it into a stream of data vectors.", sinks= {})
public class TDMsReader {
	@Log
	private static Logger log;

	@Input(doc="The TDMs channels to stream.", name="channels")
	private String [] channels;

	@Named("output-stream")
	private Stream dataStream;

	@Input(doc="Name of the input file.", name="filename")
	private String filename;

	@Input(doc="Names of the read columns.", name="names")
	private String [] names;

	private int vectorCount;

	@OnExecute
	public void execute() throws IOException, ConversionException {
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

		vectorCount = 0;

		de.codapro.tdms.io.TDMsReader reader = null;
		try {
			reader = new de.codapro.tdms.io.TDMsReader(inputFile);
			final TDMsFile file = reader.read();

			final List<Iterator<Object>> dataStreams = new ArrayList<>(channels.length);

			Arrays.stream(channels)
				  .map(channel -> findChannelIterator(file, channel))
				  .filter(opt -> opt.isPresent())
				  .map(opt -> opt.get())
				  .forEach(dataStreams::add);

			if(!dataStreams.isEmpty()) {
				final Iterator<Object> first = dataStreams.get(0);

				while(first.hasNext()) {
					final DataVector data = new DataVector();

					for(final Iterator<Object> iter : dataStreams) {
						data.append(iter.next());
					}

					++vectorCount;
					dataStream.append(data);
				}
			}
		} finally {
			closeIfOpened(reader);
		}
	}

	private void closeIfOpened(de.codapro.tdms.io.TDMsReader reader) throws ConversionException {
		if(reader != null) {
			try {
				reader.close();
			} catch(final Exception e) {
				log.error("Failed to close reader {}.", reader);
				throw new ConversionException("Failed to close reader.", e);
			}
		}
	}

	private Optional<Iterator<Object>> findChannelIterator(final TDMsFile file, final String channelPart) {
		final String [] path = channelPart.split("/");

		final Optional<TDMsGroup> group = file.getGroupByName(path[1]);

		if(!group.isPresent()) {
			log.warn("Cannot find channel of {}.", channelPart);
			return Optional.empty();
		}

		final Optional<TDMsChannel> channel = group.get().getChannelByName(path[2]);

		if(!channel.isPresent()) {
			log.warn("Cannot find channel of {}.", channelPart);
			return Optional.empty();
		}

		return Optional.of(channel.get().iterator());
	}

	@OnInit
	public void init() {
		for(int i = 0; i < names.length; ++i) {
			dataStream.getHeader().add(names[i]);
		}
	}

	@OnFinish
	public void logDataCount() {
		log.info("Read {} data vectors.", vectorCount);
	}

	@Override
	public String toString() {
		return "TDMsReader [filename=" + filename + ", " + vectorCount + "]";
	}
}
