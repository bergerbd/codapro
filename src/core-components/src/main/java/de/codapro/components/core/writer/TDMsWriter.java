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
import java.io.IOException;

import javax.inject.Named;

import de.codapro.components.core.utils.tdms.InterleavedStreamDataProvider;
import org.slf4j.Logger;

import de.codapro.tdms.model.InterleavedChannelDataProvider;
import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.Type;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnExecute;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.model.Stream;

/**
 * Component for writing tdms files.
 */
@Component(name = "TDMsWriter", doc = "Component for writing a TDMs file.", sources = {})
public class TDMsWriter {
	@Log
	private static Logger log;

	@Input(doc="Name of the output file.", name="filename")
	private String filename;

	@ColumnId(doc = "Columns to write as channels.", name = "columns", required = true, stream = "input-stream")
	private int [] columnIds = {};

	@Input(doc = "The column types to use for the channels.", name = "types", required = true)
	private Type [] columnTypes = {};

	private de.codapro.tdms.io.TDMsWriter writer;

	private TDMsFile file;

	@OnInit
	public void createTDMsWriter(final @Named("input-stream") Stream stream) {
		final File outputFile = new File(filename);

		if(outputFile.exists()) {
			log.info("Output file {} already exists. Overwriting it.", outputFile);
		}

		writer = new de.codapro.tdms.io.TDMsWriter(outputFile);
		file = writer.create();
		final TDMsGroup group = file.create("CoDaPro");

		if(columnIds.length != columnTypes.length) {
			log.warn("Columns to write ({}) and column types  ({}) differ.", columnIds.length, columnTypes.length);
		}

		final InterleavedChannelDataProvider provider = new InterleavedStreamDataProvider(stream, columnIds, columnTypes);

		final int upper = Math.min(columnIds.length, columnTypes.length);
		for(int i = 0; i < upper; ++i) {
			final String channelName = stream.getHeader().nameOf(columnIds[i]);
			final Type channelType = columnTypes[i];

			final TDMsChannel channel = group.create(channelName);
			channel.setType(channelType);
			channel.createDataSegment(provider, i);
		}
	}

	@OnExecute
	public void copyData() {
		try {
			writer.write(file);
		} catch (final IOException e) {
			log.error("Failed to write TDMs file '{}'.", filename);
		}
	}

	@Override
	public String toString() {
		return "TDMsWriter [filename=" + filename + "]";
	}
}
