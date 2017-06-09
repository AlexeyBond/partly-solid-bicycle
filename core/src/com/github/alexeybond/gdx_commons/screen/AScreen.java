package com.github.alexeybond.gdx_commons.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.alexeybond.gdx_commons.drawing.*;
import com.github.alexeybond.gdx_commons.drawing.rt.ScreenTarget;
import com.github.alexeybond.gdx_commons.drawing.rt.ViewportTarget;
import com.github.alexeybond.gdx_commons.input.InputEvents;
import com.github.alexeybond.gdx_commons.input.impl.InputEventsImpl;
import com.github.alexeybond.gdx_commons.ioc.IoC;

/**
 *
 */
public abstract class AScreen {
    private AScreen next;
    private AScreen prev;

    private final Array<ALayer> layers = new Array<ALayer>(false, 4);
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private final Technique technique;

    private Scene scene;
    private InputEvents inputEvents;

    private Viewport mainViewport;
    private RenderTarget mainTarget;

    private boolean created = false;

    public AScreen(Technique technique) {
        this.technique = technique;

        preload();
    }

    private void ensureCreated() {
        if (created) return;

        DrawingState drawingState = IoC.resolve("drawing state");

        mainViewport = initViewport();

        mainTarget = new ViewportTarget(ScreenTarget.INSTANCE, mainViewport);

        inputEvents = new InputEventsImpl(mainViewport);
        inputMultiplexer.addProcessor(inputEvents.inputProcessor());
        inputEvents.enable();

        this.scene = new Scene(new DrawingContext(drawingState, mainTarget), technique);

        create();

        created = true;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Initialize all screen-specific things such as layers.
     *
     * Called at most once for every screen instance.
     */
    protected abstract void create();

    /**
     * Preload resources.
     *
     * Called before {@link #create()}. {@link #create()} will be called only after all resources
     * are loaded completely.
     */
    protected void preload() {

    }

    protected boolean created() {
        return created;
    }

    protected Viewport initViewport() {
        return new ScreenViewport();
    }

    protected boolean keepPrev() {
        return false;
    }

    public final Scene scene() {
        return scene;
    }

    public final Viewport viewport() {
        return mainViewport;
    }

    public final InputEvents input() {
        return inputEvents;
    }

    public void next(AScreen next) {
        this.next = next;
    }

    public AScreen next() {
        return this.next;
    }

    public void back() {
        next(prev);
    }

    /**
     * @param front    {@code true} if layer should receive input events before all the previously added layers
     */
    public final <T extends ALayer> T addLayer(T layer, boolean front) {
        layers.add(layer);
        InputProcessor ip = layer.getInputProcessor();
        if (null != ip) inputMultiplexer.addProcessor(front ? 0 : inputMultiplexer.size(), ip);
        return layer;
    }

    public final <T extends ALayer> T addLayerFront(T layer) {
        return addLayer(layer, true);
    }

    public final <T extends ALayer> T addLayerBack(T layer) {
        return addLayer(layer, false);
    }

    public final void resize(int width, int height) {
        if (!created) return;

        mainViewport.update(width, height);

        for (int i = 0; i < layers.size; i++) {
        }
    }

    public void enter(AScreen after) {
        if (keepPrev() && prev == null) {
            this.prev = after;
        } else if (null != after) {
            after.forget();
        }
    }

    public void leave(AScreen next) {
        if (next == this.next) {
            this.next = null;
        }
    }

    public void forget() {
        scene.context().release();
    }

    public void pause() {
        Gdx.input.setInputProcessor(null);
    }

    public void resume() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void draw() {
        ensureCreated();

        float dt = Gdx.graphics.getDeltaTime();
        for (int i = 0; i < layers.size; i++) {
            layers.get(i).update(dt);
        }

        scene.draw();
    }
}
