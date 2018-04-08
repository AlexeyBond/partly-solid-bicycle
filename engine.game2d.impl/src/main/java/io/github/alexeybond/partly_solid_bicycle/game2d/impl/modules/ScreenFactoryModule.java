package io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules;

import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies.DefaultCompositeStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.GeneratedModule;
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.factory.actions.PopAction;
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.factory.actions.PushAction;
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.factory.actions.ReplaceAction;
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.factory.actions.TerminateAction;

import java.util.Collection;
import java.util.HashMap;

@GeneratedModule(
        reverseDepends = {"application_config"}
)
public class ScreenFactoryModule extends ScreenFactoryModuleImpl {
    @Override
    public void init(Collection<Object> envs) {
        super.init(envs);

        DefaultCompositeStrategy strategy = new DefaultCompositeStrategy(new HashMap<Object, IoCStrategy>());

        strategy.add("pop", PopAction.PARSE_STRATEGY);
        strategy.add("push", PushAction.PARSE_STRATEGY);
        strategy.add("replace", ReplaceAction.PARSE_STRATEGY);
        strategy.add("terminate", TerminateAction.PARSE_STRATEGY);

        IoC.register("screen event action", strategy);
    }
}
