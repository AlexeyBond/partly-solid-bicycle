package com.github.alexeybond.partly_solid_bicycle.drawing;

/**
 * Object that holds a reference to a {@link RenderTarget}.
 */
public class TargetSlot {
    private final String name;
    private RenderTarget target;

    public TargetSlot(String name) {
        this.name = name;
    }

    public RenderTarget get() {
        if (null == target)
            throw new IllegalStateException("Render target slot \"" + name + "\" is empty.");
        return target;
    }

    public void set(RenderTarget target) {
        if (null != this.target)
            throw new IllegalStateException("Render target slot \"" + name + "\" is not empty.");
        this.target = target;
    }

    public void clear() {
        if (null != this.target) {
            this.target.unuse();
            this.target = null;
        }
    }

    public void swap(TargetSlot that) {
        RenderTarget thatTarget = that.target;
        that.target = this.target;
        this.target = thatTarget;
    }
}
