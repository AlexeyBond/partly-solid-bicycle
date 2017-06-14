package com.github.alexeybond.gdx_commons.drawing.animation.impl;

import com.badlogic.gdx.utils.Array;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.Events;

/**
 *
 */
public class BoundAnimationInstance<I> extends BasicAnimationInstance {
    private final I initiator;
    private final Array<Event<I>> events;

    public BoundAnimationInstance(Animation animation, I initiator, Events<I> events, Array<String> eventNames) {
        super(animation);

        this.initiator = initiator;

        this.events = new Array<Event<I>>(true, eventNames.size);

        for (int i = 0; i < eventNames.size; i++) {
            this.events.add(events.event(eventNames.get(i), Event.<I>make()));
        }
    }

    @Override
    protected void triggerEvent(int eventIndex) {
        events.get(eventIndex).trigger(initiator);
    }
}
