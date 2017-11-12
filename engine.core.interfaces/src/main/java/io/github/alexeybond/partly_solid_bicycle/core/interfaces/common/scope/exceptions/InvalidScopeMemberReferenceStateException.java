package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions;

public class InvalidScopeMemberReferenceStateException extends RuntimeException {
    public InvalidScopeMemberReferenceStateException(String message) {
        super(message);
    }

    public InvalidScopeMemberReferenceStateException(String message, Throwable cause) {
        super(message, cause);
    }
}