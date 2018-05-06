package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for component field that is optional so loader companion should skip it if corresponding
 * field is not present in component configuration.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
public @interface Optional {
    Meta asMeta() default @Meta({
            "property.isOptional=true"
    });
}
