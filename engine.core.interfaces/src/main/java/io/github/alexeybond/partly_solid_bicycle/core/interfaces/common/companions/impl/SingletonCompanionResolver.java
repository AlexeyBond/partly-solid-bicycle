package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.impl;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver;
import org.jetbrains.annotations.NotNull;

/**
 * {@link CompanionResolver} that returns the same companion instance for all objects.
 * <p>
 * This class is placed in {@code engine.core.interfaces} to avoid possible dependency loop
 * between {@code engine.core.impl} and {@code engine.preprocessing}.
 * </p>
 *
 * @param <TO>
 * @param <TC>
 */
public class SingletonCompanionResolver<TO, TC extends Companion<TO>>
        implements CompanionResolver<TO, TC> {
    @NotNull
    private final TC companion;

    public SingletonCompanionResolver(@NotNull TC companion) {
        this.companion = companion;
    }

    @NotNull
    @Override
    public TC resolve(@NotNull TO object) {
        return companion;
    }
}
