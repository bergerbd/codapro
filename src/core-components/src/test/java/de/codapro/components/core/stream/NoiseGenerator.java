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
package de.codapro.components.core.stream;

import javax.inject.Named;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnExecute;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "NoiseGenerator", doc = "Generates some noise for testing purposes", sinks={})
public class NoiseGenerator {
	@Input(name = "leading-noise-count", doc = "Number of leading zero noise data sets.")
	private int leadingNoise = 0;

	@Input(name = "data-noise-count", doc = "Number of data noise data sets.")
	private int dataNoise = 0;

	@Input(name = "data-columns", doc = "Number of data columns.")
	private int dataColumns = 1;

	@OnExecute
	public void generate(final @Named("output-stream") Stream output) throws ConversionException {
		for(int i = 0; i < leadingNoise; ++i) {
			final DataVector vector = new DataVector();

			for(int col = 0; col < dataColumns; ++col) {
				vector.append(0.0);
			}

			output.append(vector);
		}

		for(int i = 0; i < dataNoise; ++i) {
			final DataVector vector = new DataVector();

			for(int col = 0; col < dataColumns; ++col) {
				vector.append(50.0);
			}

			output.append(vector);
		}
	}
}
