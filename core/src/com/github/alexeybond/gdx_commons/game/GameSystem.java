package com.github.alexeybond.gdx_commons.game;

import com.github.alexeybond.gdx_commons.util.parts.Part;

/**
 *
 */
public interface GameSystem extends Part<Game> {
    void update(float deltaTime);
}
