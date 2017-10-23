package io.github.alexeybond.partly_solid_bicycle.core.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.common.visitor.Visitor;
import org.jetbrains.annotations.NotNull;

public interface ScopeVisitor<T> extends Visitor<Scope<T>> {
    void visitMember(@NotNull Id<T> id, @NotNull T member);
}
