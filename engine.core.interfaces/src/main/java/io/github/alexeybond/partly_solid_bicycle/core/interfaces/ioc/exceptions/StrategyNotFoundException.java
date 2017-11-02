package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions;

public class StrategyNotFoundException extends RuntimeException {
    private final String key;

    public StrategyNotFoundException(String key) {
        this.key = key;
    }

    @Override
    public String getMessage() {
        return "No strategy found for key '" + key + "'.";
    }
}
