package io.github.alexeybond.partly_solid_bicycle.core.impl.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCContainer;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DefaultContainer implements IoCContainer {
    @NotNull
    private final Map<Object, IoCStrategy> strategies;

    public DefaultContainer(@NotNull Map<Object, IoCStrategy> strategies) {
        this.strategies = strategies;
    }

    @NotNull
    @Override
    public IoCStrategy resolveStrategy(@NotNull Object key) throws StrategyNotFoundException {
        IoCStrategy strategy = strategies.get(key);

        if (null == strategy) {
            throw new StrategyNotFoundException(key);
        }

        return strategy;
    }

    @Override
    public void register(@NotNull Object key, IoCStrategy strategy) {
        strategies.put(key, strategy);
    }
}
