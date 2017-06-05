package com.github.alexeybond.gdx_commons.drawing;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
        passes.put(name, pass);
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

    /**
     * Enable/disable passes with names matching given pattern.
     *
     * Method uses regular expressions so do not call it very frequently.
     */
    public void enableMatching(Pattern pattern, boolean enable) {
        for (Map.Entry<String, Pass> entry : passes.entrySet())
            if (pattern.matcher(entry.getKey()).matches())
                entry.getValue().enable(enable);
    }
}
