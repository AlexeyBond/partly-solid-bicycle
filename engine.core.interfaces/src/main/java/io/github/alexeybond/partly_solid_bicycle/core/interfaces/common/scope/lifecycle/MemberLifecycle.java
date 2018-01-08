package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.lifecycle;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import org.jetbrains.annotations.NotNull;

public interface MemberLifecycle<T> {
    /**
     * Called when a scope member gets resolved (created using a factory provided
     * to {@link Scope#put(Id, Factory, Object)} or as result of forwarding a member from super-scope).
     *
     * @param id     identifier of the member
     * @param member member object
     * @param <A>    factory argument type
     * @return listener that should receive all events happening with the member
     */
    @NotNull
    <A> IndividualMemberLifecycle<T> onResolved(
            @NotNull Id<T> id,
            @NotNull T member);
}
