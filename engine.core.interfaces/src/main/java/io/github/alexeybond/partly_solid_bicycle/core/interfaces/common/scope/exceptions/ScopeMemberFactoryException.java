package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions;

public class ScopeMemberFactoryException extends RuntimeException {
    public ScopeMemberFactoryException(String message) {
        super(message);
    }

    public ScopeMemberFactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
