package com.github.alexeybond.partly_solid_bicycle.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.github.alexeybond.partly_solid_bicycle.util.event.Events;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventsOwner;
import com.github.alexeybond.partly_solid_bicycle.util.parts.AParts;
import com.github.alexeybond.partly_solid_bicycle.util.parts.IterableParts;
import com.github.alexeybond.partly_solid_bicycle.util.parts.Parts;

/**
 *
 */
public class Game implements EventsOwner, Disposable {
    private final Events events = new Events();
    private final IterableParts<Game, GameSystem> systems
            = new IterableParts<Game, GameSystem>(new Parts<Game, GameSystem>(this));

    public AParts<Game, GameSystem> systems() {
        return systems;
    }

    @Override
    public Events events() {
        return events;
    }

    public void update(float deltaTime) {
        Array<GameSystem> systemsArray = systems.startIterations();

        try {
            for (int i = 0; i < systemsArray.size; i++) {
                systemsArray.get(i).update(deltaTime);
            }
        } finally {
            systems.endIterations();
        }
    }

    @Override
    public void dispose() {
        systems.removeAll();
    }
}
