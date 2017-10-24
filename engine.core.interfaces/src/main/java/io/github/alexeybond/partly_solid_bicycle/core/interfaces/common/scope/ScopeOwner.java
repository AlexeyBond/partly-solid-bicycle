package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

public interface ScopeOwner<TScope extends Scope> {
    TScope getScope();
}
