package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.OutputDataObject;

/**
 * Companion object that knows how to save state of another object to a {@link OutputDataObject}.
 *
 * @param <T>
 */
public interface Saver<T> extends Companion<T> {
    void save(T src, OutputDataObject data);
}
