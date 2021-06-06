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

import java.util.Collection;
import java.util.Optional;

/**
 * The TDMs file correspondent.
 *
 */
public interface TDMsFile extends TDMsObject {
	/**
	 * Creates and adds a new group with the given name if there is no group with the given
	 *   name. Otherwise the method will just return the existing group.
	 * 
	 * @return A valid group.
	 */
	public TDMsGroup create(final String name);

	/**
	 * @return The group with the specified {@code name}.
	 */
	public Optional<TDMsGroup> getGroupByName(final String name);

	/**
	 * @return A read-only collection of all groups.
	 */
	public Collection<TDMsGroup> getGroups();

	@Override
	public default String getObjectPath() {
		return "/";
	}
}
