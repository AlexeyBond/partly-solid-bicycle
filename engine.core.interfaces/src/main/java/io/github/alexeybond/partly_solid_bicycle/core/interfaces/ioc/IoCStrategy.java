package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyException;

/**
 * Strategy of dependency resolution.
 */
public interface IoCStrategy {
    Object resolve(Object... args) throws StrategyException;
}
