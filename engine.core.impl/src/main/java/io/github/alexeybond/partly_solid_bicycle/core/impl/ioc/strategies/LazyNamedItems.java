package io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LazyNamedItems implements IoCStrategy {
    private final Map<Object, IoCStrategy> strategies = new HashMap<Object, IoCStrategy>();

    @NotNull
    private final IoCStrategy itemStrategy;

    public LazyNamedItems(@NotNull IoCStrategy itemStrategy) {
        this.itemStrategy = itemStrategy;
    }

    @Override
    public Object resolve(Object... args) throws StrategyException {
        Object key = args[0];
        IoCStrategy strategy = null;

        Object[] nextArgs = Arrays.copyOfRange(args, 1, args.length);

        try {
            strategy = strategies.get(key);
            return strategy.resolve(nextArgs);
        } catch (NullPointerException e) {
            if (strategy != null && key != null) throw e;
        }

        strategy = (IoCStrategy) itemStrategy.resolve(args);

        strategies.put(key, strategy);

        return strategy.resolve(nextArgs);
    }
}
