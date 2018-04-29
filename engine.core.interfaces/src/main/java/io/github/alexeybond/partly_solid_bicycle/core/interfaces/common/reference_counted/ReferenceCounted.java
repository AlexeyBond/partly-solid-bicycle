package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.reference_counted;

public interface ReferenceCounted {
    void acquire();

    void release();
}
