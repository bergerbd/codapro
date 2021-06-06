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
package de.codapro.api.model;

import java.util.Objects;
/**
 * A dependency to a constant value that might be produced by a component or set by
 *   the configuration
 *
 * Getting the value blocks until the value has been produced.
 * 
 * @author Bernhard J. Berger
 */
public class Value<T> {
	/**
	 * Tracks if value was not set so far.
	 */
	private boolean isUnset = true;

	/**
	 * The value of the constant. This field is {@code null} as long as the value
	 *   hasn't been set so far.
	 */
	private T object = null;

	/**
	 * The get methods waits for the producer to create the value.
	 *
	 * @return The value.
	 */
	public T get() {
		while(isUnset) {
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		return object;
	}

	/**
	 * @return If the value is still unset.
	 */
	public boolean isUnset() {
		return isUnset;
	}

	public void set(final T value) {
		this.object = value;
		this.isUnset = false;
	}

	@Override
	public String toString() {
		return "Constant<" + Objects.toString(object) + ">";
	}
}
