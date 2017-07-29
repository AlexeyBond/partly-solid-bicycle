package com.github.alexeybond.partly_solid_bicycle.application;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.alexeybond.partly_solid_bicycle.drawing.RenderTarget;
import com.github.alexeybond.partly_solid_bicycle.drawing.Scene;
import com.github.alexeybond.partly_solid_bicycle.input.InputEvents;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Modules;
import com.github.alexeybond.partly_solid_bicycle.util.parts.AParts;

/**
 *
 */
public interface Screen extends ScreenLifecycleListener {
    AParts<Screen, Layer> layers();

    InputEvents input();

    Modules modules();

    Scene scene();

    /** {@link Viewport} instance used by this screen */
    Viewport viewport();

    /** {@link RenderTarget Target} that represents a viewport of the window where this screen should be drawn */
    RenderTarget screenTarget();

    /** Increment reference counter, should be called before any other calls (lifecycle methods, etc) */
    void acquire();

    /** Decrement reference counter, dispose resources if reference count is 0 */
    void release();

    /** Get the screen that should be made active in next frame */
    Screen next();

    void next(Screen screen);

    Screen prev();
}
