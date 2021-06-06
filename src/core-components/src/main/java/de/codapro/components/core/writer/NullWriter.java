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
package de.codapro.components.core.writer;

import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;

@Component(name = "NullWriter", doc = "Discards all data.", sources= {})
public class NullWriter {
	@OnProcess(dest={})
	public void nop(final DataVector vecotr) {
		// The null writer dumps all data.
	}

	@Override
	public String toString() {
		return "NullWriter []";
	}
}
