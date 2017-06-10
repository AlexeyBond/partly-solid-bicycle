package com.github.alexeybond.gdx_gm2.test_game.game;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.alexeybond.gdx_commons.drawing.EDSLTechnique;
import com.github.alexeybond.gdx_commons.ioc.IoC;

/**
 *
 */
public class LoseScreenTechnique extends EDSLTechnique {
    private final ShaderProgram shader;

    public LoseScreenTechnique() {
        shader = IoC.resolve("load shader from files",
                "old/space-gc/shaders/vs_lose_screen_fade.glsl",
                "old/space-gc/shaders/ps_lose_screen_fade.glsl"
        );
    }

    @Override
    protected Runnable build() {
        return seq(
                clearColor(),
                pass("lost-game")
        );
    }
}
