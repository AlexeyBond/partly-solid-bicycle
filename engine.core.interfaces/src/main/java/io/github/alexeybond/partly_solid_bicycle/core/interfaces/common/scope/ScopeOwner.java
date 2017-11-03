package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for a object that is a owner of a scope.
 *
 * <p>
 *  There should be a 1 to 1 relation between the scope and it's owner.
 * </p>
 *
 * @param <TScope> type of owned scope
 */
public interface ScopeOwner<TScope extends Scope> {
    /**
     * @return the scope this object is owner of
     */
    @NotNull
    TScope getScope();
}
