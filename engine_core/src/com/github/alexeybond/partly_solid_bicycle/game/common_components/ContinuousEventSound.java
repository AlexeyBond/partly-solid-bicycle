package com.github.alexeybond.partly_solid_bicycle.game.common_components;

import com.badlogic.gdx.audio.Sound;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventListener;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.BooleanProperty;

/**
 * Plays a looping sound when a boolean property of owner is true.
 */
public class ContinuousEventSound
        implements Component, EventListener<BooleanProperty> {
    private final Sound sound;
    private final String eventName;

    private BooleanProperty event;
    private int eventSubIdx = -1;

    private long soundPlayIdx = -1;

    public ContinuousEventSound(Sound sound, String eventName) {
        this.sound = sound;
        this.eventName = eventName;
    }

    @Override
    public void onConnect(Entity entity) {
        event = entity.events().event(eventName, BooleanProperty.make(false));
        eventSubIdx = event.subscribe(this);

        if (event.get()) start();
    }

    @Override
    public void onDisconnect(Entity entity) {
        eventSubIdx = event.unsubscribe(eventSubIdx);
        stop();
    }

    @Override
    public boolean onTriggered(BooleanProperty event) {
        if (event.get()) {
            start();
        } else {
            stop();
        }

        return true;
    }

    private void start() {
        stop();
        soundPlayIdx = sound.play();
        sound.setLooping(soundPlayIdx, true);
    }

    private void stop() {
        if (soundPlayIdx < 0) return;
        sound.stop(soundPlayIdx);
    }
}
