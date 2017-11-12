package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;

/**
 * Companion object that knows how to load state of another object from {@link InputDataObject}.
 *
 * @param <T>
 */
public interface Loader<T> extends Companion<T> {
    void load(T dst, InputDataObject data);
}
