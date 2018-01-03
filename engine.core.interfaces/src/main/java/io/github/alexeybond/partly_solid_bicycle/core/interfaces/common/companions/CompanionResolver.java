package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions;

import org.jetbrains.annotations.NotNull;

/**
 * Object that knows how to get a companion of type {@code TC} for a object of type {@code TO}.
 *
 * @param <TO>
 * @param <TC>
 */
public interface CompanionResolver<TO, TC extends Companion<TO>> {
    @NotNull
    TC resolve(@NotNull TO object);
}
