package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyNotFoundException;
import org.jetbrains.annotations.NotNull;

/**
 * Container that is a key-value storage where keys are dependency identifiers and values are dependency
 * resolution strategies.
 */
public interface IoCContainer {
    /**
     * Get strategy for dependency name.
     *
     * @param key dependency name
     * @return the strategy
     * @throws StrategyNotFoundException if there is no strategy for given key
     */
    @NotNull
    IoCStrategy resolveStrategy(@NotNull Object key) throws StrategyNotFoundException;

    /**
     * Register strategy for a dependency.
     *
     * @param key      dependency name
     * @param strategy strategy
     */
    void register(@NotNull Object key, IoCStrategy strategy);
}
