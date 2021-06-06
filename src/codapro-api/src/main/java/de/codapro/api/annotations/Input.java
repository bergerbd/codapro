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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Injects an input value of a component.
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Inherited
public @interface Input {
	/**
	 * Description of the input value.
	 */
	String doc();

	/**
	 * Name of the input stream.
	 */
	String name();

	/**
	 * Iff the value is required by the component to run.
	 */
	boolean required() default true;

	/**
	 * Optional parameter that is necessary for constant dependencies and describes
	 *   the parameter's type.
	 */
	Class<?> type() default Object.class;
}
