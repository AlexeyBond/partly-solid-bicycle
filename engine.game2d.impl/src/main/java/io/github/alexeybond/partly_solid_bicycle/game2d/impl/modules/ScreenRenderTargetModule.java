package io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules;

import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.BaseModule;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies.Singleton;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.render.target.ScreenTarget;

import java.util.Collection;

/**
 * Registers {@link ScreenTarget} instance as singleton named "main render target".
 * <p>
 * This module is required as some PSB applications may exist within one LibGDX application
 * and some of them may be rendered to targets other than the screen.
 * The application rendered to a separate screen will have it's own IoC context and register it's own
 * target as "main render target" within that context.
 */
public class ScreenRenderTargetModule extends BaseModule {
    {
        provide("main_render_target");
        depend("ioc");
    }

    @Override
    public void init(Collection<Object> env) {
        IoC.register("main render target", new Singleton(new ScreenTarget()));
    }

    @Override
    public void shutdown() {

    }
}
