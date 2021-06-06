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
package de.codapro.components.core.stream.impl;

/**
 * Data class that keeps track of data conversions.
 */
public class ConversionRule {
	private final int sourceIndex;
	private final int targetIndex;

	public ConversionRule(final int sourceIndex, final int targetIndex) {
		this.sourceIndex = sourceIndex;
		this.targetIndex = targetIndex;
	}

	/**
	 * @return Which column index contains the input data of the conversion
	 */
	public int getSourceIndex() {
		return sourceIndex;
	}

	/**
	 * @return The target index for the converted data.
	 */
	public int getTargetIndex() {
		return targetIndex;
	}

}
