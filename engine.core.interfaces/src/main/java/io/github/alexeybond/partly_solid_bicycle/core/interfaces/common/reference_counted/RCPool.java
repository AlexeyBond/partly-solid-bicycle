package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.reference_counted;

/**
 * Pool of {@link ReferenceCounted} objects.
 * <p>
 * This pool doesn't provide a {@code release(T)} method as items should be released automatically
 * when their reference counter reaches 0.
 *
 * @param <T>
 */
public interface RCPool<T extends ReferenceCounted> {
    /**
     * @return the object with current reference count of 1
     */
    T acquire();
}
