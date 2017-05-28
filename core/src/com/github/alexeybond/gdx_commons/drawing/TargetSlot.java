package com.github.alexeybond.gdx_commons.drawing;

/**
 * Object that holds a reference to a {@link RenderTarget}.
 */
public class TargetSlot {
    private final String name;
    private RenderTarget target;

    public TargetSlot(String name) {
        this.name = name;
    }

    RenderTarget get() {
        if (null == target)
            throw new IllegalStateException("Render target slot \"" + name + "\" is empty.");
        return target;
    }

    void set(RenderTarget target) {
        if (null != this.target)
            throw new IllegalStateException("Render target slot \"" + name + "\" is not empty.");
        this.target = target;
    }

    void clear() {
        if (null != this.target) {
            this.target.unuse();
            this.target = null;
        }
    }

    void swap(TargetSlot that) {
        RenderTarget thatTarget = that.target;
        that.target = this.target;
        this.target = thatTarget;
    }
}
