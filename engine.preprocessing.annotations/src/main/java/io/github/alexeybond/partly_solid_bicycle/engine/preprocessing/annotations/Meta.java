package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

/**
 *
 */
public @interface Meta {
    /**
     * Array of key-value pairs represented as strings where key and value are separated by '='.
     * <p>
     * Leading and trailing whitespaces of key and value will be trimmed.
     */
    String[] value() default {};
}
