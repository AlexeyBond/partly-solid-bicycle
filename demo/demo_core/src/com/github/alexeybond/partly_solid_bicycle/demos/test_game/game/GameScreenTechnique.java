package com.github.alexeybond.partly_solid_bicycle.demos.test_game.game;

import com.github.alexeybond.partly_solid_bicycle.drawing.tech.EDSLTechnique;
import com.github.alexeybond.partly_solid_bicycle.drawing.projection.OrthoProjection;

/**
 *
 */
public class GameScreenTechnique extends EDSLTechnique {
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
                        // Android clears stencil buffer itself every frame
                        /*onResize(*/toStencil(seq(
                                clearStencil(),
                                withProjection(OrthoProjection.UNIT_PN),
                                circle(0.9f, 64)
                        ))/*)*/,
                        withStencilTest(seq(
                                withProjection(OrthoProjection.UNIT),
                                pass("minimap-background"),
                                pass("setup-minimap-camera"),
                                pass("minimap-objects")
                        ))
                )),
                toOutput(),
                pass("ui")
        );
    }
}
