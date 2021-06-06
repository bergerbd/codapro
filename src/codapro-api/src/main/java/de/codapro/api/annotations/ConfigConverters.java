package de.codapro.api.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for allowing to repeat the {@link ConfigConverter} annotation.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface ConfigConverters {
	public ConfigConverter [] value() default {};
}
