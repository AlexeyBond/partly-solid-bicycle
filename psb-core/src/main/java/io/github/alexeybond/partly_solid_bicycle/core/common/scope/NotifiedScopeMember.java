package io.github.alexeybond.partly_solid_bicycle.core.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.common.id.Id;
import org.jetbrains.annotations.NotNull;

/**
 * Scope member that is notified when it is placed in scope/removed from it.
 *
 * @param <TMember> type of this class visible to scope
 * @param <TScope>  type of scope this object may belong to
 */
public interface NotifiedScopeMember<TMember, TScope extends Scope<TMember>> {
    /**
     * Called when this object is added to scope.
     *
     * @param scope the scope
     * @param id    identifier of this object in scope
     */
    void onJoin(@NotNull TScope scope, @NotNull Id<TMember> id);

    /**
     * Called when this object is removed from scope.
     *
     * @param scope the scope
     * @param id    identifier of this object in scope
     */
    void onLeave(@NotNull TScope scope, @NotNull Id<TMember> id);

    /**
     * Called instead of {@link #onLeave(Scope, Id)} when the root scope is being destroyed.
     *
     * @param scope the scope
     * @param id    identifier of this object in scope
     */
    void onDispose(@NotNull TScope scope, @NotNull Id<TMember> id);
}
