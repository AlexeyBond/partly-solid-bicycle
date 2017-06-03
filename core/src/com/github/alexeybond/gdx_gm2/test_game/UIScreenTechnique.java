package com.github.alexeybond.gdx_gm2.test_game;

import com.github.alexeybond.gdx_commons.drawing.Technique;

/**
 *
 */
public class UIScreenTechnique extends Technique {
    @Override
    protected Runnable build() {
        return seq(
                clearColor(),
                pass("bg"),
                pass("ui"),
                pass("dbg")
        );
    }
}
