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

import java.util.Arrays;

import javax.inject.Named;

import org.slf4j.Logger;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnFinish;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "GroupMarker", doc = "This component groups parts of the input stream according to a boolean flag (within the data stream itself). Following group-oriented components can then calculate group-based information.")
public class GroupMarker {
	@Input(doc="Should non-group data records be filtered (deleted).", name="filter")
	private boolean filtering = false;

	@ColumnId(doc="Column name of the group flag.", name="grouping-flag-column", stream = "input-stream")
	private int groupFlagIndex;

	private String lastGroupName = "";

	@Log
	private Logger log;

	@Named("output-stream")
	private Stream outputStream;

	@Input(doc = "List of values that shows there is a group .", name = "values", required = true)
	private String [] values = {};

	private boolean wasLastRowInGroup = false;

	private void addGroupEndVector() throws ConversionException {
		log.info("Adding group end.");
		outputStream.append(DataVector.GROUP_END);
	}

	private void addGroupStartVector() throws ConversionException {
		log.info("Adding group start.");
		outputStream.append(DataVector.GROUP_START);
	}

	@OnInit
	public void copyHeaderInformation(@Named("input-stream") final Stream inputStream) {
		outputStream.setHeader(inputStream.getHeader());

		Arrays.sort(values);
	}

	@OnFinish
	public void finish() throws ConversionException {
		if(wasLastRowInGroup) {
			addGroupEndVector();
		}
	}

	@OnProcess(dest= {})
	public void process(final DataVector vector) throws ConversionException {
		final String value = vector.get(groupFlagIndex).toString();
		boolean isRowInGroup = Arrays.binarySearch(values, value) >= 0;

		log.info("Looking for {} in {} -> {}", value, values, isRowInGroup);

		if(isRowInGroup && !wasLastRowInGroup) {
			// entering new group
			addGroupStartVector();
			lastGroupName = value;
		} else if(!isRowInGroup && wasLastRowInGroup) {
			// leaving group
			addGroupEndVector();
			lastGroupName = "";
		} else if(isRowInGroup && wasLastRowInGroup && !lastGroupName.equals(value)) {
			// switched group identifier
			addGroupEndVector();
			addGroupStartVector();
		}

		if(isRowInGroup || !filtering) {
			outputStream.append(vector);
		}

		wasLastRowInGroup = isRowInGroup;
	}

	@Override
	public String toString() {
		return "GroupingComponent [group-flag-index=" + groupFlagIndex + ", filtering=" + filtering + "]";
	}
}
