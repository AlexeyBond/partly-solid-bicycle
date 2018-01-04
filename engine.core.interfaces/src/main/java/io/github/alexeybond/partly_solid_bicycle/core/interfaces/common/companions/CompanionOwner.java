package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions;

import org.jetbrains.annotations.NotNull;

/**
 * A object that has companion(s).
 *
 * @param <TO> type of this object as seen by companion
 */
public interface CompanionOwner<TO> {
    @NotNull
    <T extends Companion<TO>> T getCompanionObject(@NotNull String name);
}
