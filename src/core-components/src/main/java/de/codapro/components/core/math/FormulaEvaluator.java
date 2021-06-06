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
package de.codapro.components.core.math;

import javax.inject.Named;

import org.mariuszgromada.math.mxparser.Constant;
import org.mariuszgromada.math.mxparser.Expression;

import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;
import de.codapro.components.core.utils.math.FormulaEvaluatorBase;

@Component(doc = "A component for calculating new values based on configurable formulas.", name = "FormulaEvaluator")
public class FormulaEvaluator extends FormulaEvaluatorBase {
	@OnInit
	public void init(final @Named("output-stream") Stream stream) {
		addColumnNamesToOutputStream(stream);
		resolveFunctions();

		for(final Expression formula : formulas) {
			formula.addFunctions(functions);
		}
		
	}

	@OnProcess
	public void evaluateFormulas(final DataVector vector) {
		for(final Expression exp : formulas) {
			exp.removeAllConstants();

			exp.addConstants(constants);

			for(final String column : stream.getHeader()) {
				final Object value = vector.get(stream.getHeader().indexOf(column));

				if(!(value instanceof Number)) {
					continue;
				}

				final Constant arg = new Constant(mangleName(column), ((Number)value).doubleValue());
				exp.addConstants(arg);
			}

			vector.append(exp.calculate());
		}
	}
}
