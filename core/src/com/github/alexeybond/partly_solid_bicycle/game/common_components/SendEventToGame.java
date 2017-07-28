package com.github.alexeybond.partly_solid_bicycle.game.common_components;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.util.event.Event;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventListener;

/**
 *
 */
public class SendEventToGame
        implements Component, EventListener<Event> {
    private final String gameEventName, entityEventName;

    private Event entityEvent;
    private Event gameEvent;
    private int entityEventSubIdx = -1;

    public SendEventToGame(String gameEventName, String entityEventName) {
        this.gameEventName = gameEventName;
        this.entityEventName = entityEventName;
    }

    @Override
    public void onConnect(Entity entity) {
        entityEvent = entity.events().event(entityEventName, Event.makeEvent());
        gameEvent = entity.game().events().event(gameEventName, Event.makeEvent());

        entityEventSubIdx = entityEvent.subscribe(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        entityEventSubIdx = entityEvent.unsubscribe(entityEventSubIdx);
    }

    @Override
    public boolean onTriggered(Event event) {
        return gameEvent.trigger();
    }
}
