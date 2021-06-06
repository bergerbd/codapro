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

import java.util.Map;
import java.util.Optional;

/**
 * Base interface for all TDSm objects. Channels, groups and files have in common
 *   that they have a name and properties.
 */
public interface TDMsObject {
	/**
	 * Binds the property {@code name} to the given {@code value}.
	 */
	public void addProperty(final String name, final Object value);

	/**
	 * @return Returns the name of the object.
	 */
	public String getName();

	/**
	 * @return The object path.
	 */
	public String getObjectPath();

	/**
	 * @return The parent of the object
	 */
	public Optional<TDMsObject> getParent();

	/**
	 * @return A read-only copy of all properties.
	 */
	public Map<String, Object> getProperties();
}
