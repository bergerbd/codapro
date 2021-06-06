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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.components.core.stream.impl.ConversionRule;

@Component(name = "WhiteNoiseFilter", doc = "Median filter to remove noise.")
public class WhiteNoiseFilter {
	@Input(doc="Conversion rules for the median filter, e.g. 1->3;2->4", name="conversion-rules")
	private String conversionRulesDescription;

	@Input(doc="The mean value used for the noise.", name="mean")
	private double mean = 0.0;

	@Input(doc="The variance value used for the noise.", name="variance")
	private double variance = 0.0;

	private List<ConversionRule> conversionRules = new ArrayList<>();

	private SecureRandom randomness = new SecureRandom();

	@OnInit
	public void init() {
		final String [] splittedConversionRules = conversionRulesDescription.split(";");
		conversionRules.clear();

		for(final String conversionRule : splittedConversionRules) {
			final String [] rule = conversionRule.split("->");

			conversionRules.add(new ConversionRule(Integer.parseInt(rule[0]), Integer.parseInt(rule[1])));
		}
	}

	@OnProcess
	public void process(final DataVector vector) {
		for(final ConversionRule rule : conversionRules) {
			vector.set(rule.getTargetIndex(), ((Double)vector.get(rule.getSourceIndex())) - randomness.nextGaussian() * Math.sqrt(variance) + mean);
		}
	}

	@Override
	public String toString() {
		return "WhiteNoiseFilter [conversionRule=" + conversionRulesDescription + "]";
	}
}
