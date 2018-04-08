package io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.factory.actions;

import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies.Singleton;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenEventAction;
import org.jetbrains.annotations.NotNull;

public class TerminateAction implements ScreenEventAction {
    @Override
    public void act(@NotNull ScreenContext context) {
        context.pop("null");
    }

    public static final IoCStrategy PARSE_STRATEGY = new Singleton(new TerminateAction());
}
