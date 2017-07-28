package com.github.alexeybond.partly_solid_bicycle.demos.test_game;

import com.github.alexeybond.partly_solid_bicycle.drawing.tech.EDSLTechnique;

/**
 *
 */
public class UIScreenTechnique extends EDSLTechnique {
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
