package com.github.alexeybond.gdx_commons.drawing;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Scene {
    private final DrawingContext drawingContext;
    private final Map<String, Pass> passes;
    private Runnable drawRunnable;

    public Scene(DrawingContext drawingContext, Technique technique) {
        this.drawingContext = drawingContext;

        this.passes = new HashMap<String, Pass>();

        this.drawRunnable = technique.initFor(this);
    }

    public Pass getPass(String name) {
        if (!passes.containsKey(name))
            throw new IllegalArgumentException("No pass named \"" + name + "\"");
        return passes.get(name);
    }

    public Pass addPass(String name, Pass pass) {
        if (passes.containsKey(name))
            throw new IllegalArgumentException("There already is a pass named \"" + name + "\"");
        return pass;
    }

    public DrawingContext context() {
        return this.drawingContext;
    }

    public void draw() {
        context().renderTo(context().getOutputTarget());
        drawRunnable.run();
        context().state().flush();
        context().renderTo((RenderTarget) null);
    }
}
