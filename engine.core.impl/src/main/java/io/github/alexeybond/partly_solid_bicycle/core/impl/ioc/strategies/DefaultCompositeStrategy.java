package io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.CompositeIoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

public class DefaultCompositeStrategy implements IoCStrategy, CompositeIoCStrategy {
    @NotNull
    private final HashMap<Object, IoCStrategy> nested;

    public DefaultCompositeStrategy(@NotNull HashMap<Object, IoCStrategy> nested) {
        this.nested = nested;
    }

    @Override
    public Object resolve(Object... args) throws StrategyException {
        IoCStrategy choice = nested.get(args[0]);

        if (null == choice) {
            throw new StrategyException("No nested strategy found for key '" + args[0] + "'");
        }

        return choice.resolve(Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public void add(@NotNull Object key, @NotNull IoCStrategy strategy) {
        nested.put(key, strategy);
    }
}
