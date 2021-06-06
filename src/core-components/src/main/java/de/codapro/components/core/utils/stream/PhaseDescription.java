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
package de.codapro.components.core.utils.stream;

public abstract class PhaseDescription {

	protected final double endValue;
	private final String name;
	protected final double startValue;

	public PhaseDescription(final String name, final double startValue, final double endValue) {

		this.name = name;
		this.endValue = endValue;
		this.startValue = startValue;

	}

	public double getEndValue() {
		return endValue;
	}

	public String getName() {
		return name;
	}

	public double getStartValue() {
		return startValue;
	}
	
	public abstract boolean matches(final double value);
}
