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
package de.codapro.components.core.identifier;

import java.awt.geom.Point2D;

import javax.inject.Named;

import org.slf4j.Logger;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnEnterGroup;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnLeaveGroup;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.annotations.Output;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

/**
 *
 * @author cplump
 */

@Component(name="PeakBoundariesDeterminatorWithFixedValues", doc="Sets start and ending time values by fixed boundaries, to enable a review possibility.")
public class PeakBoundariesDeterminatorWithFixedValues {

	private static final String[] COLUMN_NAMES = new String[] { "starting time x", "starting time y", "ending time x", "ending time y", "secondary starting x time", "secondary starting y time", "secondary ending y time", "secondary ending time" , "maximal-value"};

	private static final int STARTING_TIME_X_INDEX = 0;

	private static final int STARTING_TIME_Y_INDEX = 1;

	private static final int ENDING_TIME_X_INDEX = 2;

	private static final int ENDING_TIME_Y_INDEX = 3;

	private static final int SECONDARY_STARTING_X_TIME_INDEX = 4;

	private static final int SECONDARY_STARTING_Y_TIME_INDEX = 5;

	private static final int SECONDARY_ENDING_X_TIME_INDEX = 6;

	private static final int SECONDARY_ENDING_Y_TIME_INDEX = 7;

	private static final int MAXIMAL_VALUE = 8;

	private DataVector descriptor;

	private boolean isFirst;

	@Log
	private static Logger log;

	@Output(doc = "A stream containing the descriptor data.", name = "descriptor-stream")
	private Stream descriptorStream;

	@ColumnId(name="independent-index" , stream="input-stream", doc=" specifies the column whose data is to be stored.")
	private int independentIndex;

	@ColumnId(name="dependent-index", stream= "input-stream", doc = " specifies the dependent values")
	private int dependentIndex;

	@Input(doc = "specifies position of secondary points relative to start/end and max/min", name = "relative-position")
	private double  relativePosition;

	@Input(doc = "Threshold", name = "threshold")
	private double threshold;

	private Point2D.Double max = new Point2D.Double();

	private Point2D.Double min = new Point2D.Double();

	private Point2D.Double start = new Point2D.Double();

	private Point2D.Double end = new Point2D.Double();

	private boolean inPeak = false;

	private Point2D.Double[] relativePositionMarker = {new Point2D.Double(), new Point2D.Double(), new Point2D.Double(), new Point2D.Double()};

	@OnInit
	public void addColumnNamesToOutputStream() {
		for (final String column : COLUMN_NAMES) {
			descriptorStream.getHeader().add(column);
		}
	}

	@OnEnterGroup
	public void startGroup() {
		log.info("Starting new group");
		descriptor = new DataVector(COLUMN_NAMES.length);
		isFirst = true; 
		inPeak = false;
	}

	@OnProcess
	public void setDescriptors(final DataVector vector) {
		double independentValue = ((Number) vector.get(independentIndex)).doubleValue();
		double dependentValue = ((Number)vector.get(dependentIndex)).doubleValue();

		if(isFirst) {
			//descriptor.set(STARTING_TIME_INDEX, vector.get(indexOfInterest));
			max.setLocation(independentValue, dependentValue);
			min.setLocation(independentValue, dependentValue);
			start.setLocation(independentValue, dependentValue);
			end.setLocation(independentValue, dependentValue);

			relativePositionMarker[0].setLocation(independentValue, dependentValue);
			relativePositionMarker[1].setLocation(independentValue, dependentValue);
			relativePositionMarker[2].setLocation(independentValue, dependentValue);
			relativePositionMarker[3].setLocation(independentValue, dependentValue);

			isFirst = false;
		}

		updateBoundaries(independentValue, dependentValue);
		updateExtremum(independentValue, dependentValue);
		relativePositionMarker[0].y = start.y + relativePosition * (max.y-start.y);
		relativePositionMarker[1].y = end.y + relativePosition * (max.y-end.y);
		relativePositionMarker[2].y = start.y - relativePosition * (start.y-min.y);
		relativePositionMarker[3].y = end.y - relativePosition * (end.y-min.y);

		end.y = dependentValue;
		descriptor.set(ENDING_TIME_X_INDEX, independentValue);
	}

	@OnLeaveGroup
	public void copyDescriptorToOutputStream() {
		if(min.y >= start.y) {
			descriptor.set(MAXIMAL_VALUE, max);
			descriptor.set(SECONDARY_STARTING_X_TIME_INDEX, relativePositionMarker[0].x);
			descriptor.set(SECONDARY_STARTING_Y_TIME_INDEX, relativePositionMarker[0].y);
			descriptor.set(SECONDARY_ENDING_X_TIME_INDEX, relativePositionMarker[1].x);
			descriptor.set(SECONDARY_STARTING_Y_TIME_INDEX, relativePositionMarker[1].y);
		} else {
			descriptor.set(MAXIMAL_VALUE, min);
			descriptor.set(SECONDARY_STARTING_X_TIME_INDEX, relativePositionMarker[2].x);
			descriptor.set(SECONDARY_STARTING_Y_TIME_INDEX, relativePositionMarker[2].y);
			descriptor.set(SECONDARY_ENDING_X_TIME_INDEX, relativePositionMarker[3].x);
			descriptor.set(SECONDARY_ENDING_Y_TIME_INDEX, relativePositionMarker[3].y);
		}

		try {
			descriptorStream.append(descriptor);
		} catch (ConversionException e) {
			log.error("Failed to write to the descriptor stream.", e);
		}
	}

	private void updateExtremum(final double newXValue, final double newYValue) {
		if(max.y > newYValue) {
			max.x = newXValue;
			max.y = newYValue;
		}
		
		if(min.y > newYValue) {
			min.x = newXValue;
			min.y = newYValue;
		}

	}

	private void updateBoundaries(double dependentValue, double independentValue) {
		if (Math.abs(dependentValue - start.y) < threshold && !inPeak) {
			start.setLocation(independentValue, dependentValue);
		} else if(Math.abs(dependentValue - start.y) >= threshold && !inPeak) {
			end.setLocation(independentValue, dependentValue);
			inPeak = true;
		} else if(Math.abs(dependentValue - end.y) >= threshold && inPeak) {
			end.setLocation(independentValue, dependentValue);
		} else if (Math.abs(dependentValue - end.y) < threshold && inPeak) {
			end.setLocation(independentValue, dependentValue);
			inPeak = false;
		}
	}
}
