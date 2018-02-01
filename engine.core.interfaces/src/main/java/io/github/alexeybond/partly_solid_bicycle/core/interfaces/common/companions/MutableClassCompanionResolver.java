package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions;

import org.jetbrains.annotations.NotNull;

public interface MutableClassCompanionResolver<TO> extends ClassCompanionResolver<TO> {
    void register(@NotNull String type, @NotNull CompanionResolver<TO, ?> resolver);
}
