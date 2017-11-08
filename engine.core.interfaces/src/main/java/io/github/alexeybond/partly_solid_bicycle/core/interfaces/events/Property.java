package io.github.alexeybond.partly_solid_bicycle.core.interfaces.events;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.OutputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.DuplicateFieldException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidOutputDataTypeException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;
import org.jetbrains.annotations.NotNull;

/**
 * Property is a (probably mutable) container containing value of some type and emitting events when
 * the value is changed.
 *
 * @param <T>
 */
public interface Property<T extends Property<T>> extends EventSource<T> {
    /**
     * Load value from given data object.
     *
     * @param data data object
     * @throws InvalidInputDataTypeException if methods of {@code data} throw
     * @throws UndefinedFieldException if methods of {@code data} throw
     */
    void read(InputDataObject data)
            throws InvalidInputDataTypeException, UndefinedFieldException;

    /**
     * Store value of this property to given data object.
     *
     * @param data data object
     * @throws InvalidOutputDataTypeException if methods of {@code data} throw
     * @throws DuplicateFieldException if methods of {@code data} throw
     */
    void write(OutputDataObject data)
            throws InvalidOutputDataTypeException, DuplicateFieldException;

    /**
     * Returns the actual property instance, usually {@code this}.
     *
     * <p>
     *  Unlike {@link EventSource#origin()} method of {@link Property} is guaranteed to return a value.
     * </p>
     *
     * @return the actual property instance
     * @see EventSource#origin()
     */
    @NotNull
    @Override
    T origin();
}
