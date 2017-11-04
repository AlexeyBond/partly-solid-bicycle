package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;

/**
 * Factory that takes a declarative description (represented as {@link InputDataObject})
 * of a object to create as argument and creates a object based on the provided description.
 *
 * @param <T> type of creatable object
 */
public interface DataDrivenFactory<T> extends Factory<T, InputDataObject> {
}
