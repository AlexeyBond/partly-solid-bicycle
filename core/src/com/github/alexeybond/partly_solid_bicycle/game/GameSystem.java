package com.github.alexeybond.partly_solid_bicycle.game;

import com.github.alexeybond.partly_solid_bicycle.util.parts.Part;

/**
 *
 */
public interface GameSystem extends Part<Game> {
    void update(float deltaTime);
}
