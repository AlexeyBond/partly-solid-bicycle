package io.github.alexeybond.partly_solid_bicycle.core.impl.common.companions;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver;
import org.jetbrains.annotations.NotNull;

public class ProxyCompanionResolver<TO, TC extends Companion<TO>>
        implements CompanionResolver<TO, TC> {
    @NotNull
    private CompanionResolver<TO, TC> resolver;

    public ProxyCompanionResolver(@NotNull CompanionResolver<TO, TC> resolver) {
        this.resolver = resolver;
    }

    @NotNull
    @Override
    public TC resolve(@NotNull TO object) {
        return resolver.resolve(object);
    }

    public void setResolver(@NotNull CompanionResolver<TO, TC> resolver) {
        this.resolver = resolver;
    }
}
