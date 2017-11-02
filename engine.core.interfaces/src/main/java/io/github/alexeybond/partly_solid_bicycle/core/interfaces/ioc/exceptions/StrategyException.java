package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions;

public class StrategyException extends RuntimeException {
    public StrategyException(String msg) {
        super(msg);
    }

    public StrategyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
