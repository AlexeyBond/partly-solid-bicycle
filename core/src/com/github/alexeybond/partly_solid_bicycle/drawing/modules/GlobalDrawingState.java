package com.github.alexeybond.partly_solid_bicycle.drawing.modules;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.alexeybond.partly_solid_bicycle.drawing.DrawingState;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;
import com.github.alexeybond.partly_solid_bicycle.ioc.strategy.Singleton;

/**
 *
 */
public class GlobalDrawingState implements Module {
    private DrawingState drawingState;

    @Override
    public void init() {
        // TODO:: Use CpuSpriteBatch? Or better add a way to choose...
        Batch batch = new PolygonSpriteBatch();
        ShapeRenderer shaper = new ShapeRenderer();
        drawingState = new DrawingState(batch, shaper);

        IoC.register("drawing state", new Singleton(drawingState));
    }

    @Override
    public void shutdown() {
        drawingState.end();
    }
}
