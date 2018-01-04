package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for a {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module module}
 * which should register components and their companions (including generated ones).
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Module {
    /**
     * @return {@code true} iff the module should be used as default module for current artifact
     */
    boolean useAsDefault() default false;
}
