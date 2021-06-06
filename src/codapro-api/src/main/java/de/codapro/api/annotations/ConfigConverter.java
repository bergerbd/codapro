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
package de.codapro.api.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * If a component needs a non-base type configuration CoDaPro offers the
 *   possibility of converting the configured value to any target type
 *   using Apache Beanutils. The annotated class has to implemented
 *   {@link org.apache.commons.beanutils.Converter}.
 *
 * @see <a href="http://commons.apache.org/proper/commons-beanutils/>Apache Beanutils</a>
 */
@Repeatable(ConfigConverters.class)
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface ConfigConverter {
	/**
	 * Target type of Apache Beanutils converter.
	 */
	public Class<?> type();
}
