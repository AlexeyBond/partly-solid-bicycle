package io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyException;

public class Singleton implements IoCStrategy {
    private final Object object;

    public Singleton(Object object) {
        this.object = object;
    }

    @Override
    public Object resolve(Object... args) throws StrategyException {
        return object;
    }
}
