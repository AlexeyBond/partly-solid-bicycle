package com.github.alexeybond.partly_solid_bicycle.application.impl.layers;

import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.GameSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.PhysicsSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.input.InputSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.RenderSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.timing.TimingSystem;
import com.github.alexeybond.partly_solid_bicycle.util.parts.AParts;

/**
 * Game screen that initializes all game systems required for a 2-d game with Box2D physics.
 */
public class GameLayerWith2DPhysicalGame extends GameLayer {
    @Override
    protected Game setupGame(Game game) {
        game = super.setupGame(game);

        AParts<Game, GameSystem> systems = game.systems();
        systems.add("timing", new TimingSystem());
        systems.add("tagging", new TaggingSystem());
        systems.add("render", new RenderSystem(screen().scene()));
        systems.add("input", new InputSystem(screen().input().makeChild()));
        systems.add("physics", new PhysicsSystem());

        return game;
    }
}
