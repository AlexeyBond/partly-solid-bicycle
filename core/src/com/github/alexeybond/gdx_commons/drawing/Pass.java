package com.github.alexeybond.gdx_commons.drawing;

import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class Pass implements Runnable, Drawable {
    public static int DEFAULT_RESERVED_CAPACITY = 32;

    private final Array<Drawable> drawables;
    private final DrawingContext defaultContext;
    private final PassController controller;

    public Pass(DrawingContext defaultContext, PassController controller) {
        this.defaultContext = defaultContext;
        this.controller = controller;

        this.drawables = new Array<Drawable>(false, DEFAULT_RESERVED_CAPACITY);
    }

    @Override
    public void draw(DrawingContext context) {
        controller.beforePass(context);
        for (int i = 0; i < drawables.size; i++) {
            controller.beforeItem(context);
            drawables.get(i).draw(context);
            controller.afterItem(context);
        }
        controller.afterPass(context);
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
}
