package com.github.alexeybond.gdx_gm2.test_game.game;

import com.github.alexeybond.gdx_commons.drawing.Technique;
import com.github.alexeybond.gdx_commons.drawing.projection.OrthoProjection;

/**
 *
 */
public class GameScreenTechnique extends Technique {
    @Override
    protected Runnable build() {
        return seq(
                clearColor(),
                withProjection(OrthoProjection.UNIT),
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
