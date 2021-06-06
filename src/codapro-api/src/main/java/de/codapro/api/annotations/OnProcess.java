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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mark a single method of a pipe-like component to process data vectors.
 * 
 * The default behavior is to copy data vectors from {@code input-stream} to
 *   {@code output-stream} and to process them by this method. If the {@code dest}
 *   parameter is set to an empty list the vectors will just be consumed. If there
 *   are multiple input and output streams data vectors from the first input stream
 *   will be copied to the first output stream, the second to the second, and so on.
 *
 * Annotations relevant for pipe-like components: {@link OnInit}, {@link OnEnterGroup},
 *   {@link OnLeaveGroup}, and {@link OnFinish}
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface OnProcess {
	public String [] value() default {"input-stream"};
	public String [] dest() default {"output-stream"};
}
