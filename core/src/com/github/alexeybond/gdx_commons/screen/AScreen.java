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

/**
 *
 */
public class AScreen {
    private AScreen next;
    private AScreen prev;

    private final Array<ALayer> layers = new Array<ALayer>(false, 4);
    private final Scene scene;
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

    private Viewport mainViewport;
    private RenderTarget mainTarget;

    public AScreen(DrawingState drawingState, Technique technique) {
        mainViewport = initViewport();

        mainTarget = new ViewportTarget(ScreenTarget.INSTANCE, mainViewport);

        this.scene = new Scene(new DrawingContext(drawingState, mainTarget), technique);
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
    public final ALayer addLayer(ALayer layer, boolean front) {
        layers.add(layer);
        InputProcessor ip = layer.getInputProcessor();
        if (null != ip) inputMultiplexer.addProcessor(front ? 0 : inputMultiplexer.size(), ip);
        return layer;
    }

    public final ALayer addLayerFront(ALayer layer) {
        return addLayer(layer, true);
    }

    public final ALayer addLayerBack(ALayer layer) {
        return addLayer(layer, false);
    }

    public final void resize(int width, int height) {
        for (int i = 0; i < layers.size; i++) {
        }
    }

    public void enter(AScreen after) {
        if (keepPrev() && prev == null) {
            this.prev = after;
        } else {
            after.forget();
        }
    }

    public void leave(AScreen next) {
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
        float dt = Gdx.graphics.getDeltaTime();
        for (int i = 0; i < layers.size; i++) {
            layers.get(i).update(dt);
        }

        scene.draw();
    }
}
