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
package de.codapro.components.core.utils.math;

import javax.inject.Named;

import org.mariuszgromada.math.mxparser.Constant;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import de.codapro.api.annotations.Input;
import de.codapro.api.model.Stream;

public abstract class FormulaEvaluatorBase {
	@Input(doc = "List of column names for the results.", name = "result-column-names")
	private String [] resultColumns;

	@Input(doc = "List of parameter values used within the formulas, e.g. 'depth=10'.", name = "parameters", required=false)
	protected Constant [] constants = {};

	@Input(doc = "List of formulas, e.g. 'x * depth'.", name = "formulas")
	protected Expression [] formulas;

	@Input(doc = "List of functions, e.g. 'x * depth'.", name = "functions", required = false)
	protected Function [] functions = {};

	@Named("input-stream")
	protected Stream stream;

	protected void addColumnNamesToOutputStream(final Stream stream) {
		stream.getHeader().add(resultColumns);
	}

	protected void resolveFunctions() {
		for(int i = 0; i < functions.length; ++i) {
			final Function [] alreadyDefinedFunctions = new Function[i];
			System.arraycopy(functions, 0, alreadyDefinedFunctions, 0, i);

			functions[i].addFunctions(alreadyDefinedFunctions);
		}
	}

	protected String mangleName(final String name) {
		return name.replace(" ", "_").replace("-", "_");
	}
}
