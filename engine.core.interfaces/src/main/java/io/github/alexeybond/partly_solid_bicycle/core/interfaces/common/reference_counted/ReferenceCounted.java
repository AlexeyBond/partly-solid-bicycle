package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.reference_counted;

/**
 * Interface for objects that have an explicit reference counter.
 */
public interface ReferenceCounted {
    void acquire();

    void release();
}
