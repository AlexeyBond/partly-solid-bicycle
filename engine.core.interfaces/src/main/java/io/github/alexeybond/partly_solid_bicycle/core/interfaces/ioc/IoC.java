package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Service locator.
 */
public enum IoC {
    ;
    private static IoCContainerHolder containerHolder;

    @SuppressWarnings({"unchecked"})
    public static <T> T resolve(@NotNull Object key, Object... args)
            throws StrategyException, StrategyNotFoundException {
        return (T) resolveStrategy(key).resolve(args);
    }

    @NotNull
    public static IoCStrategy resolveStrategy(@NotNull Object key)
            throws StrategyNotFoundException {
        return containerHolder.get().resolveStrategy(key);
    }

    public static void register(@NotNull Object key, IoCStrategy strategy) {
        containerHolder.get().register(key, strategy);
    }

    public static void use(@NotNull IoCContainerHolder holder) {
        if (null != containerHolder) {
            throw new IllegalStateException("Container holder already initialized.");
        }

        containerHolder = holder;
    }

    public static void use(@Nullable IoCContainer container) {
        containerHolder.set(container);
    }
}
