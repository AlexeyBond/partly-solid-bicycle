package io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules;

import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.BaseModule;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.NodeFactories;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.manager.DefaultScreenContext;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.Screen;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget;

import java.util.Collection;

public class InteractiveApplicationStateModule extends BaseModule {
    {
        provide("initial_state");
        depend("ioc");
        depend("application_root");
        depend("main_render_target");
    }

    @Override
    public void init(Collection<Object> env) {
        IoC.register("initial application state", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) throws StrategyException {
                ApplicationState terminationState = (ApplicationState) args[0];

                RenderTarget target = IoC.resolve("main render target");

                LogicNode appRoot = IoC.resolve("application root node");
                IdSet<LogicNode> idSet = appRoot.getTreeContext().getIdSet();

                GenericFactory<Screen, ScreenContext> initialScreenFactory = appRoot
                        .get(idSet.get("config"))
                        .get(idSet.get("screens"))
                        .get(idSet.get("initial"))
                        .getComponent();

                LogicNode screensRoot = appRoot
                        .getOrAdd(idSet.get("screens"), NodeFactories.EMPTY_GROUP, null);

                return DefaultScreenContext.init(
                        "initial",
                        initialScreenFactory,
                        screensRoot,
                        target,
                        terminationState
                );
            }
        });
    }

    @Override
    public void shutdown() {

    }
}
