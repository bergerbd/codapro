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
package de.codapro.tdms.model.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.codapro.tdms.model.TDMsObject;

/**
 * Base class for all TDSm objects.
 */
public abstract class ObjectImpl implements TDMsObject {
	/**
	 * If the object state is modified.
	 */
	protected boolean modified = true;

	/**
	 * Object name.
	 */
	private final String name;

	/**
	 * Parent of this object.
	 */
	private final ObjectImpl parent ;

	/**
	 * Object properties.
	 */
	private final Map<String, Object> properties = new HashMap<>();

	protected ObjectImpl(final ObjectImpl parent, final String name) {
		this.parent = parent;
		this.name = name;
	}

	@Override
	public void addProperty(final String name, final Object value) {
		final Object oldValue = properties.get(name);

		if(oldValue == null && value == null || oldValue != null && oldValue.equals(value)) {
			return;
		}

		modified = true;

		properties.put(name, value);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public final Optional<TDMsObject> getParent() {
		return Optional.ofNullable(parent);
	}

	@Override
	public Map<String, Object> getProperties() {
		return Collections.unmodifiableMap(properties);
	}

	public boolean isModified() {
		return modified;
	}

	protected StringBuilder objectToString(final String typeName) {
		final StringBuilder sb = new StringBuilder();

		sb.append(typeName);
		sb.append(" [name=");
		sb.append(getName());
		sb.append(", properties={");

		for(final Map.Entry<String, Object> entry : getProperties().entrySet()) {
			sb.append(entry.getKey());
			sb.append("->");
			sb.append(entry.getValue());
			sb.append(", ");
		}

		return sb.append("}");
	}

	@Override
	public String toString() {
		return "TDMsObject [name=" + name + "]";
	}

	public void setClean() {
		modified = false;
	}
}
