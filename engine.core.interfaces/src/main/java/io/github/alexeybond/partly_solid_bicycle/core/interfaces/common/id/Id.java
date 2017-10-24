package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id;

import org.jetbrains.annotations.NotNull;

public interface Id<T> {
    /**
     * Get serializable representation of this identifier.
     *
     * <p>
     *  For identifiers created by call of {@link IdSet#get(Object)} the passed key object should be returned
     *  (the key object is assumed to be serializable).
     *  For identifiers created by call of {@link IdSet#unnamed()} unique serializable representation should
     *  be created (UUID or unique long number).
     * </p>
     *
     * @return serializable representation of this identifier
     */
    @NotNull
    Object serializable();
}
