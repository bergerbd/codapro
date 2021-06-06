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
package de.codapro.tdms.model;

/**
 * A data provider for writing interleaved data to a file.
 *
 * @param <T> Type of the data provided.
 */
public interface InterleavedChannelDataProvider extends DataProvider<Object []> {

	/**
	 * @return Type of the provided data.
	 */
	public Type getType(final int index);

	/**
	 * @return The number of channels this provider produces data for.
	 */
	public int getChannelCount();

	/**
	 * Sets the size of the data provider.
	 * 
	 * WARNING: Internal method.
	 */
	public void setSize(long count);

	public boolean isModified();

	public void setClean();
}
