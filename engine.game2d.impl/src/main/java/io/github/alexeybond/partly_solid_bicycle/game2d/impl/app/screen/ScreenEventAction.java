package io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen;

import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext;
import org.jetbrains.annotations.NotNull;

public interface ScreenEventAction {
    void act(@NotNull ScreenContext context);
}
