package io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lifecycle.member;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.lifecycle.IndividualMemberLifecycle;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.lifecycle.MemberLifecycle;
import org.jetbrains.annotations.NotNull;

public abstract class MemberLifecycleDecorator<T>
        implements MemberLifecycle<T> {
    @NotNull
    private final MemberLifecycle<T> decorated;

    protected MemberLifecycleDecorator(@NotNull MemberLifecycle<T> decorated) {
        this.decorated = decorated;
    }

    protected abstract <A> IndividualMemberLifecycle<T> decorateResolved(
            @NotNull Id<T> id,
            @NotNull T member,
            @NotNull IndividualMemberLifecycle<T> decorated);

    @NotNull
    @Override
    public final <A> IndividualMemberLifecycle<T> onResolved(
            @NotNull Id<T> id,
            @NotNull T member) {
        return decorateResolved(
                id, member,
                decorated.onResolved(id, member));
    }
}
