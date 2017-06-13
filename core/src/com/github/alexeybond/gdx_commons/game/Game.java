package com.github.alexeybond.gdx_commons.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.github.alexeybond.gdx_commons.util.event.Events;
import com.github.alexeybond.gdx_commons.util.event.EventsOwner;
import com.github.alexeybond.gdx_commons.util.parts.AParts;
import com.github.alexeybond.gdx_commons.util.parts.IterableParts;
import com.github.alexeybond.gdx_commons.util.parts.Parts;

/**
 *
 */
public class Game implements EventsOwner<GameSystem>, Disposable {
    private final Events<GameSystem> events = new Events<GameSystem>();
    private final IterableParts<Game, GameSystem> systems
            = new IterableParts<Game, GameSystem>(new Parts<Game, GameSystem>(this));

    public AParts<Game, GameSystem> systems() {
        return systems;
    }

    @Override
    public Events<GameSystem> events() {
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
