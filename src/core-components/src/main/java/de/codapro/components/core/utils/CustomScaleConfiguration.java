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
package de.codapro.components.core.utils;

import de.codapro.niusb.api.PreScaledUnits;

/**
 * NiUSB custom scale configuration.
 */
public class CustomScaleConfiguration {
	public static CustomScaleConfiguration fromString(final String value) {
		final String [] parts = value.split(";");

		return new CustomScaleConfiguration(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), PreScaledUnits.valueOf(parts[3]), parts[4]);
	}

	private final String name;
	private final PreScaledUnits preScaledUnits;
	private final String scaledUnits;
	private final double slope;
	private final double yIntercept;

	public CustomScaleConfiguration(final String name, final double slope, final double yIntercept, final PreScaledUnits preScaledUnits, final String scaledUnits) {
		this.name = name;
		this.slope = slope;
		this.yIntercept = yIntercept;
		this.preScaledUnits = preScaledUnits;
		this.scaledUnits = scaledUnits;
	}

	public String getName() {
		return name;
	}

	public PreScaledUnits getPreScaledUnits() {
		return preScaledUnits;
	}

	public String getScaledUnits() {
		return scaledUnits;
	}

	public double getSlope() {
		return slope;
	}

	public double getYIntercept() {
		return yIntercept;
	}
}
