package com.github.alexeybond.gdx_commons.drawing;

import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class Pass implements Runnable, Drawable {
    public static int DEFAULT_RESERVED_CAPACITY = 32;

    private final Array<Drawable> drawables;
    private final DrawingContext defaultContext;

    private boolean enabled = true;

    public Pass(DrawingContext defaultContext) {
        this.defaultContext = defaultContext;

        this.drawables = new Array<Drawable>(false, DEFAULT_RESERVED_CAPACITY);
    }

    @Override
    public void draw(DrawingContext context) {
        if (!enabled) return;

        for (int i = 0; i < drawables.size; i++) {
            drawables.get(i).draw(context);
        }
    }

    @Override
    public void run() {
        draw(defaultContext);
    }

    public void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }

    public void removeDrawable(Drawable drawable) {
        drawables.removeValue(drawable, true);
    }

    public boolean enabled() {
        return enabled;
    }

    public void enable(boolean enable) {
        this.enabled = enable;
    }
}
