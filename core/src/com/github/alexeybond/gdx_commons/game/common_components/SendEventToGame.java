package com.github.alexeybond.gdx_commons.game.common_components;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;

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
