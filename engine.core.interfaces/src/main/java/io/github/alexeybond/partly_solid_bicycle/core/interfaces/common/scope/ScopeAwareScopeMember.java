package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

import org.jetbrains.annotations.NotNull;

/**
 * Scope member that belongs to only one scope and knows to what scope it belongs.
 *
 * @param <TMember> type of this class visible to scope
 * @param <TScope>  type of scope this object may belong to
 */
public interface ScopeAwareScopeMember<TMember, TScope extends Scope<TMember>> {
    /**
     * Get the scope this object belongs to.
     *
     * @return scope this object belongs to
     * @throws IllegalStateException if this object does not belong to any scope
     */
    @NotNull
    TScope getNativeScope()
        throws IllegalStateException;
}
