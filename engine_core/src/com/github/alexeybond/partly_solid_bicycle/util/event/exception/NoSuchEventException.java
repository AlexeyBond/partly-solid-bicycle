package com.github.alexeybond.partly_solid_bicycle.util.event.exception;

public class NoSuchEventException extends RuntimeException {
    public NoSuchEventException(String eventName) {
        super("No such event: '" + eventName + "'");
    }
}
