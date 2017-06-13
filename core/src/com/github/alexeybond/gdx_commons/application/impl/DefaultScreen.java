package com.github.alexeybond.gdx_commons.application.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.alexeybond.gdx_commons.application.Layer;
import com.github.alexeybond.gdx_commons.application.Screen;
import com.github.alexeybond.gdx_commons.drawing.*;
import com.github.alexeybond.gdx_commons.drawing.rt.ScreenTarget;
import com.github.alexeybond.gdx_commons.drawing.rt.ViewportTarget;
import com.github.alexeybond.gdx_commons.input.InputEvents;
import com.github.alexeybond.gdx_commons.input.impl.InputEventsImpl;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.modules.Modules;
import com.github.alexeybond.gdx_commons.util.parts.AParts;
import com.github.alexeybond.gdx_commons.util.parts.IterableParts;
import com.github.alexeybond.gdx_commons.util.parts.Parts;

/**
 * Base class for application screens.
 */
public abstract class DefaultScreen implements Screen {
    private final IterableParts<Screen, Layer> layers
            = new IterableParts<Screen, Layer>(new Parts<Screen, Layer>(this));
    private final Modules modules = new Modules();

    private int refCount = 0;
    private boolean layersCreated = false;

    private InputEvents input;
    private Scene scene;
    private Viewport viewport;
    private RenderTarget screenTarget;

    private Screen next = null;
    private Screen prev = null;

    @Override
    public void update(float dt) {
        if (!layersCreated) {
            createLayers(layers);
            layersCreated = true;
        }

        Array<Layer> layers = this.layers.startIterations();

        try {
            for (int i = 0; i < layers.size; i++) {
                layers.get(i).update(dt);
            }
        } finally {
            this.layers.endIterations();
        }
    }

    @Override
    public void enter(Screen prev) {
        Gdx.input.setInputProcessor(input().inputProcessor());

        Array<Layer> layers = this.layers.startIterations();

        try {
            for (int i = 0; i < layers.size; i++) {
                layers.get(i).enter(prev);
            }
        } finally {
            this.layers.endIterations();
        }

        if (checkKeepPrevious()) {
            if (null != this.prev) {
                this.prev.release();
                this.prev = null;
            }

            prev.acquire();
            this.prev = prev;
        }
    }

    @Override
    public void leave(Screen next) {
        Gdx.input.setInputProcessor(null);

        Array<Layer> layers = this.layers.startIterations();

        try {
            for (int i = 0; i < layers.size; i++) {
                layers.get(i).leave(next);
            }
        } finally {
            this.layers.endIterations();
        }
    }

    @Override
    public void pause() {
        input().disable();

        Array<Layer> layers = this.layers.startIterations();

        try {
            for (int i = 0; i < layers.size; i++) {
                layers.get(i).pause();
            }
        } finally {
            this.layers.endIterations();
        }
    }

    @Override
    public void unpause() {
        input().enable();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Array<Layer> layers = this.layers.startIterations();

        try {
            for (int i = 0; i < layers.size; i++) {
                layers.get(i).unpause();
            }
        } finally {
            this.layers.endIterations();
        }
    }

    @Override
    public AParts<Screen, Layer> layers() {
        return layers;
    }

    @Override
    public InputEvents input() {
        return input;
    }

    @Override
    public Modules modules() {
        return modules;
    }

    @Override
    public Scene scene() {
        return scene;
    }

    @Override
    public Viewport viewport() {
        return viewport;
    }

    @Override
    public RenderTarget screenTarget() {
        return screenTarget;
    }

    @Override
    public void acquire() {
        if (refCount == 0) {
            create0();
        }

        refCount++;
    }

    @Override
    public void release() {
        if (refCount <= 0) throw new IllegalStateException("Illegal reference count <= 0");

        refCount--;

        if (refCount == 0) {
            dispose0();
        }
    }

    @Override
    public Screen next() {
        return next;
    }

    @Override
    public void next(Screen screen) {
        this.next = screen;
    }

    @Override
    public Screen prev() {
        return prev;
    }

    private void create0() {
        System.out.printf("create %s\n", getClass().getSimpleName());

        createModules(modules);

        viewport = createViewport();
        screenTarget = new ViewportTarget(ScreenTarget.INSTANCE, viewport());
        input = new InputEventsImpl(viewport);

        scene = new Scene(
                new DrawingContext(
                        IoC.<DrawingState>resolve("drawing state"),
                        screenTarget
                ),
                createTechnique()
        );
    }

    private void dispose0() {
        System.out.printf("dispose %s\n", getClass().getSimpleName());

        layers.removeAll();
        scene.context().release();
        modules.dispose();
        layersCreated = false;

        if (null != prev) {
            prev.release();
            prev = null;
        }
    }

    /**
     * Override to use custom viewport on a particular screen.
     */
    protected Viewport createViewport() {
        return new ScreenViewport();
    }

    /**
     * Override to load additional modules (and keep them up during screen life).
     */
    protected void createModules(Modules modules) {

    }

    /**
     * Override to create screen layers.
     * <p>
     * This method is called inside of update/rendering loop at most once after each sequence of
     * calls to create* methods. At moment of call of this method the screen is always an active
     * unpaused screen.
     * </p>
     */
    protected void createLayers(AParts<Screen, Layer> layers) {

    }

    /**
     * Check if reference to previous screen should be kept.
     * <p>
     * <p>Some of possible overloads are:</p>
     * <ul>
     * <li>{@code return true;} - to keep reference to any screen. Is safe only for loading screens.</li>
     * <li>{@code return prev() == null;} - to keep reference only to the first screen
     * this screen appeared after. It's safe to re-use screen instances even with such overload
     * as screen resets it's previous screen reference when destroyed.</li>
     * </ul>
     */
    protected boolean checkKeepPrevious() {
        return false;
    }

    /**
     * Override to create a technique.
     */
    protected abstract Technique createTechnique();
}
