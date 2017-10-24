package io.github.alexeybond.partly_solid_bicycle.core.common.scope.exceptions;

public class InvalidScopeMemberReference extends RuntimeException {
    public InvalidScopeMemberReference(String message) {
        super(message);
    }

    public InvalidScopeMemberReference(String message, Throwable cause) {
        super(message, cause);
    }
}
