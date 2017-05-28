package com.github.alexeybond.gdx_commons.drawing;

import com.github.alexeybond.gdx_commons.drawing.rt.ScreenTarget;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DrawingContext {
    private final DrawingState state;
    private final Map<String, TargetSlot> slots;
    private RenderTarget outputRenderTarget;
    private RenderTarget currentRenderTarget;

    public DrawingContext(DrawingState state, RenderTarget outputRenderTarget) {
        this.state = state;
        this.slots = new HashMap<String, TargetSlot>();

        this.outputRenderTarget = outputRenderTarget;
        this.currentRenderTarget = ScreenTarget.INSTANCE;
    }

    public DrawingState state() {
        return this.state;
    }

    public TargetSlot getSlot(String name) {
        TargetSlot socket = slots.get(name);
        if (null == socket) {
            socket = new TargetSlot(name);
            slots.put(name, socket);
        }
        return socket;
    }

    public void release() {
        for (TargetSlot socket : slots.values())
            socket.clear();
    }

    public void renderTo(RenderTarget target) {
        if (null == target) target = ScreenTarget.INSTANCE;

        if (target != currentRenderTarget) {
            currentRenderTarget.end();
            currentRenderTarget = target;
            currentRenderTarget.begin();
        }
    }

    public void renderTo(TargetSlot slot) {
        if (null == slot) {
            renderTo(ScreenTarget.INSTANCE);
        } else {
            renderTo(slot.get());
        }
    }

    /**
     * {@link RenderTarget} everything within this context will be drawn to.
     */
    public RenderTarget getOutputTarget() {
        return outputRenderTarget;
    }

    public void setOutputTarget(RenderTarget target) {
        this.outputRenderTarget = target;
    }
}
