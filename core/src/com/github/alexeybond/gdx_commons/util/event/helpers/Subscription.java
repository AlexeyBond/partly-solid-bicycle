package com.github.alexeybond.gdx_commons.util.event.helpers;

import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;

public abstract class Subscription <ET extends Event> implements EventListener<ET> {
    private ET event;
    private int subIdx = -1;

    public final void set(ET event, boolean enable) {
        clear();
        this.event = event;
        if (enable) enable();
    }

    public final void set(ET event) {
        set(event, true);
    }

    public final void clear() {
        if (null != event && subIdx >= 0) {
            disable();
        }

        event = null;
    }

    public final void enable() {
        if (subIdx >= 0) return;

        subIdx = event.subscribe(this);
    }

    public final void disable() {
        subIdx = event.unsubscribe(subIdx);
    }

    public ET event() {
        return event;
    }
}
