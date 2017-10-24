package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Scope member that gets notified when it is accessed from nested scopes.
 *
 * @param <TMember> type of this class visible to scope
 * @param <TScope>  type of scope this object may belong to
 */
public interface ForwardAwareScopeMember<TMember, TScope extends Scope<TMember>> {
    /**
     * Called when a member gets first time accessed from child scope of a scope it is accessible from.
     *
     * <p>
     *  Some scope members may need to make a copy/child of themselves and make only it accessible
     *  from child scope.
     * </p>
     *
     * @param fromScope the scope this object is accessible from
     * @param toScope   the scope where the object is required from
     * @return the object that will be accessible from {@code toScope} or {@code null} if this object
     *          should not be accessible from child scopes
     */
    @Nullable
    TMember forward(@NotNull TScope fromScope, @NotNull TScope toScope);
}
