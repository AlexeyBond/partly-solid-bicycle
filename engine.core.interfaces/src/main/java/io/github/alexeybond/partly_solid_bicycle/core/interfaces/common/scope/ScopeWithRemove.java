package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Scope} that allows to remove members.
 *
 * @param <T> scope member type
 */
public interface ScopeWithRemove<T> extends Scope<T> {
    /**
     * Remove member with given id. Has no effect if there is no member with given id.
     *
     * @param id member id
     */
    void removeId(@NotNull Id<T> id);
}
