package io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.factory;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventListener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventSource;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenEventAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO:: Should this actually implement EventListener... or some another interface?
public class ScreenEventActionEventListener implements EventListener {
    @NotNull
    private final ScreenEventAction action;

    @NotNull
    private final ScreenContext context;

    public ScreenEventActionEventListener(
            @NotNull ScreenEventAction action,
            @NotNull ScreenContext context) {
        this.action = action;
        this.context = context;
    }

    @Override
    public void onEvent(@NotNull EventSource source, @Nullable Object initializer) {
        action.act(context);
    }
}
