package com.github.alexeybond.gdx_commons.game;

import com.badlogic.gdx.utils.Array;
import com.github.alexeybond.gdx_commons.game.event.Events;
import com.github.alexeybond.gdx_commons.game.parts.AParts;
import com.github.alexeybond.gdx_commons.game.parts.IterableParts;
import com.github.alexeybond.gdx_commons.game.parts.Parts;

/**
 *
 */
public class Game {
    private final Events<GameSystem> events = new Events<GameSystem>();
    private final IterableParts<Game, GameSystem> systems
            = new IterableParts<Game, GameSystem>(new Parts<Game, GameSystem>(this));

    public AParts<Game, GameSystem> systems() {
        return systems;
    }

    public Events<GameSystem> events() {
        return events;
    }

    public void update(float deltaTime) {
        Array<GameSystem> systemsArray = systems.startIterations();

        try {
            for (int i = 0; i < systemsArray.size; i++) {
                systemsArray.items[i].update(deltaTime);
            }
        } finally {
            systems.endIterations();
        }
    }
}
