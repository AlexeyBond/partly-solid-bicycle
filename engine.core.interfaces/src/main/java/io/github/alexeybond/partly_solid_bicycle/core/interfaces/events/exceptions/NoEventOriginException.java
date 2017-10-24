package io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.exceptions;

public class NoEventOriginException extends RuntimeException {
    public NoEventOriginException() {
        super();
    }

    public NoEventOriginException(String message) {
        super(message);
    }
}
