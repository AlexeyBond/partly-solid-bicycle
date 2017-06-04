package com.github.alexeybond.gdx_gm2.test_game.game;

import com.github.alexeybond.gdx_commons.drawing.Technique;

/**
 *
 */
public class GameScreenTechnique extends Technique {
    @Override
    protected Runnable build() {
        return seq(
                clearColor(),
                pass("game-background"),
                pushingProjection(seq(
                        pass("setup-main-camera"),
                        pass("game-objects"),
                        pass("game-debug")
                )),
                pass("ui")
        );
    }
}
