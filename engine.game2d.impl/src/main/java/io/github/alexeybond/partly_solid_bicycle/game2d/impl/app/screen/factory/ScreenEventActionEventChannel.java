package io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.factory;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Channel;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenEventAction;
import org.jetbrains.annotations.NotNull;

/**
 * Channel that receives events that should result in change of screens stack.
 */
public class ScreenEventActionEventChannel implements Channel<InputDataObject> {
    @NotNull
    private final ScreenEventAction action;

    @NotNull
    private final ScreenContext context;

    public ScreenEventActionEventChannel(
            @NotNull ScreenEventAction action,
            @NotNull ScreenContext context) {
        this.action = action;
        this.context = context;
    }

    @Override
    public void send(@NotNull InputDataObject event) {
        action.act(context);
    }
}
