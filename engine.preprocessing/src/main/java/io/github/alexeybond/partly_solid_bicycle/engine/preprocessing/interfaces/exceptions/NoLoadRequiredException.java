package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.exceptions;

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.FieldLoadGenerator;

/**
 * Exception thrown by {@link FieldLoadGenerator} when it decides that no load operation is required
 * for a variable.
 */
public class NoLoadRequiredException extends RuntimeException {
}
