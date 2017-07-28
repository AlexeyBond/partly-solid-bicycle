package com.github.alexeybond.partly_solid_bicycle.demos.test_game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.alexeybond.partly_solid_bicycle.drawing.Pass;
import com.github.alexeybond.partly_solid_bicycle.drawing.TargetSlot;
import com.github.alexeybond.partly_solid_bicycle.drawing.tech.PlainTechnique;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;

/**
 *
 */
public class LoseScreenTechnique extends PlainTechnique {
    private final ShaderProgram shader;
    private Pass lostGamePass;
    private Pass uiPass;
    private TargetSlot fboSlot;
    private float timePassed;
    private Texture distortionMap;

    public LoseScreenTechnique() {
        shader = IoC.resolve("load shader from files",
                "old/space-gc/shaders/vs_lose_screen_fade.glsl",
                "old/space-gc/shaders/ps_lose_screen_fade.glsl"
        );
        distortionMap = IoC.resolve("load texture", "old/space-gc/lose-distortions.png");
        distortionMap.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        distortionMap.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    protected void setup() {
        lostGamePass = newPass("lost-game");
        uiPass = newPass("ui");
        fboSlot = target("lost-game-fbo");

        timePassed = 0;
    }

    @Override
    protected void draw() {
        clear();
        ensureMatchingFBO(fboSlot, context().getOutputTarget());
        toTarget(fboSlot);
        clear();
        doPass(lostGamePass);
        toOutput();
        withShader(shader);
        setupDistortionShader();
        screenQuad(fboSlot.get().asColorTexture(), true);
        withShader(null);
        if (timePassed >= 1f) {
            doPass(uiPass);
        }
    }

    private void setupDistortionShader() {
        GL20 gl = Gdx.gl;
        distortionMap.bind(1);
        shader.setUniformf("u_time", timePassed);
        shader.setUniformi("u_distortionTexture", 1);
        timePassed += Gdx.graphics.getDeltaTime();
        gl.glActiveTexture(GL20.GL_TEXTURE0);
    }
}
