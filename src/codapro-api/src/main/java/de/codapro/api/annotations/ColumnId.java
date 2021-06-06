package de.codapro.api.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for declaring column ids. A column id is an implicit input for a component.
 *   The type of the annotated field have to be {@code int} or {@code int []}. At runtime
 *   the framework will look up the value bound to the field and look up the column ids
 *   within the {@code stream}.
 *
 * Example:
 * <code>
 * @ColumndId(doc="Name of the column to process", name="column-name-to-use", stream="stream-name", required=true)
 * private int fpIndex;
 * </code>
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Inherited
public @interface ColumnId {
	/**
	 * Description of the column id input.
	 */
	String doc();

	/**
	 * Name of the component parameter.
	 */
	String name();

	/**
	 * Name of a valid stream within the component.
	 */
	String stream();

	/**
	 * Is the column id required?
	 */
	boolean required() default true;
}
