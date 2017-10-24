package io.github.alexeybond.partly_solid_bicycle.core.common.scope.exceptions;

import io.github.alexeybond.partly_solid_bicycle.core.common.id.Id;

public class ScopeMemberNotFoundException extends RuntimeException {
    private final Id<?> id;

    public ScopeMemberNotFoundException(Id<?> id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Scope member with id='" + id.toString() + "' not found.";
    }
}
