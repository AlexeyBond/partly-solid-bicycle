package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.lifecycle;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IndividualMemberLifecycle<T> {
    /**
     * Called when a member is removed from a scope.
     *
     * @param id     member id
     * @param member the member object
     */
    void onRemoved(
            @NotNull Id<T> id,
            @NotNull T member);

    /**
     * Called when a scope containing a member is disposed.
     *
     * @param id     member id
     * @param member the member object
     */
    void onDisposed(
            @NotNull Id<T> id,
            @NotNull T member);

    /**
     * Called when a member is required from a child scope of a scope it belongs to.
     *
     * @param member the member object
     * @param scope  the child scope
     * @return the new member that should be visible in a child scope or {@code null} if
     * the member should be forwarded by reference
     */
    @Nullable
    T forward(@NotNull T member, @NotNull Scope<T, ?> scope);
}
