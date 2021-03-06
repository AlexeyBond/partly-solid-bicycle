package com.github.alexeybond.partly_solid_bicycle.game.common_components;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.util.event.Event;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventListener;

/**
 * Component that destroys it's owner when some event is triggered.
 */
public class DestroyOnEventComponent
        implements Component, EventListener<Event> {
    private final String eventName;
    private final String preDestroyEventName;
    private final boolean initEvent;

    private Event preDestroyEvent;
    private Event event;
    private Entity entity;
    private int eventSubIdx = -1;

    public DestroyOnEventComponent(String eventName, boolean initEvent, String preDestroyEventName) {
        this.eventName = eventName;
        this.initEvent = initEvent;
        this.preDestroyEventName = preDestroyEventName;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        if (initEvent) {
            event = entity.events().event(eventName, Event.makeEvent());
        } else {
            event = entity.events().event(eventName);
        }
        eventSubIdx = event.subscribe(this);

        preDestroyEvent = entity.events().event(preDestroyEventName, Event.makeEvent());
    }

    @Override
    public void onDisconnect(Entity entity) {
        eventSubIdx = event.unsubscribe(eventSubIdx);
    }

    @Override
    public boolean onTriggered(Event event) {
        preDestroyEvent.trigger();
        entity.destroy();
        return true;
    }
}
