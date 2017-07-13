package com.github.alexeybond.gdx_commons.game.common_components;

import com.badlogic.gdx.audio.Sound;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;

/**
 *
 */
public class OneShotEventSound implements Component, EventListener<Event> {
    private final Sound sound;
    private final String eventName;

    private Event event;
    private int eventSubIdx = -1;

    public OneShotEventSound(Sound sound, String eventName) {
        this.sound = sound;
        this.eventName = eventName;
    }

    @Override
    public void onConnect(Entity entity) {
        event = entity.events().event(eventName);
        eventSubIdx = event.subscribe(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        eventSubIdx = event.unsubscribe(eventSubIdx);
    }

    @Override
    public boolean onTriggered(Event event) {
        sound.play();
        return false;
    }
}
