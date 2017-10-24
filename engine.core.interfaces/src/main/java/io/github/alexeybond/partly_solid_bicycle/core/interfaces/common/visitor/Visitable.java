package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.visitor;

import org.jetbrains.annotations.NotNull;

public interface Visitable<TVisitor extends Visitor> {
    void accept(@NotNull TVisitor visitor);
}
