package com.github.alexeybond.gdx_commons.drawing.modules;

import com.github.alexeybond.gdx_commons.drawing.DrawingState;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.Module;
import com.github.alexeybond.gdx_commons.ioc.strategy.Singleton;

/**
 *
 */
public class GlobalDrawingState implements Module {
    private DrawingState drawingState;

    @Override
    public void init() {
        drawingState = new DrawingState();

        IoC.register("drawing state", new Singleton(drawingState));
    }

    @Override
    public void shutdown() {
        drawingState.end();
    }
}
