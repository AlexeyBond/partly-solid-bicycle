package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.visitor.Visitor;
import org.jetbrains.annotations.NotNull;

public interface ScopeVisitor<T, TScope extends Scope<T, ?>> extends Visitor<TScope> {
    void visitMember(@NotNull Id<T> id, @NotNull MemberReference<T> member, @NotNull TScope scope);
}
