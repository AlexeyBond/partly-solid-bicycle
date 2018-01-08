package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.visitor;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeOwner;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.visitor.Visitor;
import org.jetbrains.annotations.NotNull;

public interface ScopeVisitor<T, TOwner extends ScopeOwner<Scope<T, TOwner>>>
        extends Visitor<Scope<T, TOwner>> {
    void visitSuperScope(@NotNull Scope<T, ?> scope);

    void visitReference(@NotNull Id<T> id, @NotNull MemberReference<T> member);
}
