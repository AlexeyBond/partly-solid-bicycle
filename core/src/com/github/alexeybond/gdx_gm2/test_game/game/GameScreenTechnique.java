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
                toOutput(),
                clearColor(),
                pushingProjection(seq(
                        withProjection(OrthoProjection.UNIT),
                        pass("game-background"),
                        pass("setup-main-camera"),
                        pass("game-objects"),
                        pass("game-particles"),
                        pass("game-debug")
                )),
                pushingProjection(seq(
                        toTarget("minimapViewport"),
                        withProjection(OrthoProjection.UNIT),
                        pass("minimap-background"),
                        pass("setup-minimap-camera"),
                        pass("minimap-objects")
                )),
                toOutput(),
                pass("ui")
        );
    }
}
