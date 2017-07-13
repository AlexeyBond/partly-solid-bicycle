package com.github.alexeybond.gdx_commons.drawing.animation.impl;

import com.badlogic.gdx.utils.Array;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.Events;

/**
 *
 */
public class BoundAnimationInstance extends BasicAnimationInstance {
    private final Array<Event> events;

    public BoundAnimationInstance(Animation animation, Events events, Array<String> eventNames) {
        super(animation);

        this.events = new Array<Event>(true, eventNames.size);

        for (int i = 0; i < eventNames.size; i++) {
            this.events.add(events.event(eventNames.get(i), Event.makeEvent()));
        }
    }

    @Override
    protected void triggerEvent(int eventIndex) {
        events.get(eventIndex).trigger();
    }
}
