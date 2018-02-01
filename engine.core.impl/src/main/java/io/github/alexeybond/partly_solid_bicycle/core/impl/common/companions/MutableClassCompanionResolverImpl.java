package io.github.alexeybond.partly_solid_bicycle.core.impl.common.companions;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.MutableClassCompanionResolver;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Implementation of {@link MutableClassCompanionResolver}.
 * <p>
 * TODO: Add implementation that uses IoC context (for environments where multiple PSB applications may run at the same time)
 *
 * @param <TO>
 */
public class MutableClassCompanionResolverImpl<TO> implements MutableClassCompanionResolver<TO> {
    private final HashMap<String, ProxyCompanionResolver> map
            = new HashMap<String, ProxyCompanionResolver>();

    @Override
    public void register(@NotNull String type, @NotNull CompanionResolver<TO, ?> resolver) {
        ProxyCompanionResolver proxy = map.get(type);
        if (null != proxy) {
            proxy.setResolver(resolver);
        } else {
            map.put(type, new ProxyCompanionResolver(resolver));
        }
    }

    @NotNull
    @Override
    public <TC extends Companion<TO>> CompanionResolver<TO, TC> resolve(@NotNull String type) {
        ProxyCompanionResolver resolver = map.get(type);

        if (null == resolver) {
            throw new IllegalArgumentException("Unknown companion type: " + type);
        }

        return resolver;
    }
}
