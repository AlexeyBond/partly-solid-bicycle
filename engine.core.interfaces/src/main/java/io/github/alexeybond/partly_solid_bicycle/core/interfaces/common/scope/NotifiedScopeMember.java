package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import org.jetbrains.annotations.NotNull;

/**
 * Scope member that is notified when it is placed in scope/removed from it.
 *
 * @param <TMember> type of this class visible to scope
 * @param <TOwner>  type of owner of scope this object may belong to
 */
public interface NotifiedScopeMember<TMember, TOwner extends ScopeOwner<? extends Scope<TMember>>> {
    /**
     * Called when this object is added to scope.
     *
     * @param owner owner of the scope
     * @param id    identifier of this object in scope
     * @throws Exception if any error occurs
     */
    void onJoin(@NotNull TOwner owner, @NotNull Id<TMember> id)
        throws Exception;

    /**
     * Called when this object is removed from scope.
     *
     * @param owner owner of the scope
     * @param id    identifier of this object in scope
     * @throws Exception if any error occurs
     */
    void onLeave(@NotNull TOwner owner, @NotNull Id<TMember> id)
        throws Exception;

    /**
     * Called instead of {@link #onLeave(TOwner, Id)} when the root scope is being destroyed.
     *
     * @param owner owner of the scope
     * @param id    identifier of this object in scope
     * @throws Exception if any error occurs
     */
    void onDispose(@NotNull TOwner owner, @NotNull Id<TMember> id)
        throws Exception;
}
