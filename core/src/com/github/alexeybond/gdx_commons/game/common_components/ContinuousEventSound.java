package com.github.alexeybond.gdx_commons.game.common_components;

import com.badlogic.gdx.audio.Sound;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;

/**
 * Plays a looping sound when a boolean property of owner is true.
 */
public class ContinuousEventSound
        implements Component, EventListener<Component, BooleanProperty<Component>> {
    private final Sound sound;
    private final String eventName;

    private BooleanProperty<Component> event;
    private int eventSubIdx = -1;

    private long soundPlayIdx = -1;

    public ContinuousEventSound(Sound sound, String eventName) {
        this.sound = sound;
        this.eventName = eventName;
    }

    @Override
    public void onConnect(Entity entity) {
        event = entity.events().event(eventName);
        eventSubIdx = event.subscribe(this);

        if (event.get()) start();
    }

    @Override
    public void onDisconnect(Entity entity) {
        eventSubIdx = event.unsubscribe(eventSubIdx);
        stop();
    }

    @Override
    public boolean onTriggered(Component component, BooleanProperty<Component> event) {
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
