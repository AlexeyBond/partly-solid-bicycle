package com.github.alexeybond.gdx_gm2.test_game.test4;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.alexeybond.gdx_commons.drawing.Pass;
import com.github.alexeybond.gdx_commons.drawing.TargetSlot;
import com.github.alexeybond.gdx_commons.drawing.tech.PlainTechnique;
import com.github.alexeybond.gdx_commons.ioc.IoC;

import static com.badlogic.gdx.Gdx.gl;

/**
 *
 */
public class TestTechnique extends PlainTechnique {
    private final static int NORMALS_SAMPLER_ID = 1;
    private final static int LIGHT_SAMPLER_ID = 1;

    private ShaderProgram lightShader, objectsShader, normalShader;

    private TargetSlot normalSlot;
    private TargetSlot lightSlot;
    private Pass cameraPass, normalsPass, lightsPass, objectsPass, debugPass, uiPass;
    private Pass particelsPass;

    @Override
    protected void setup() {
        normalSlot = context().getSlot("normals");
        lightSlot = context().getSlot("light");

        cameraPass = newPass("setup-main-camera");
        normalsPass = newPass("game-normals");
        lightsPass = newPass("game-light");
        objectsPass = newPass("game-objects");
        debugPass = newPass("game-debug");
        particelsPass = newPass("game-particles");
        uiPass = newPass("ui");

        lightShader = IoC.resolve("load shader from files",
                "test/shaders/light_pass_vs.glsl",
                "test/shaders/light_pass_ps.glsl"
        );
        objectsShader = IoC.resolve("load shader from files",
                "test/shaders/objects_pass_vs.glsl",
                "test/shaders/objects_pass_ps.glsl"
        );
        normalShader = IoC.resolve("load shader from files",
                "test/shaders/normal_pass_vs.glsl",
                "test/shaders/normal_pass_ps.glsl"
        );
    }

    @Override
    protected void draw() {
        ensureMatchingFBO(normalSlot, context().getOutputTarget());
        ensureMatchingFBO(lightSlot, context().getOutputTarget());

        toTarget(normalSlot);
        doPass(cameraPass);
        withShader(normalShader);
        doPass(normalsPass);
        withShader(null);

        toTarget(lightSlot);
        normalSlot.get().asColorTexture().getTexture().bind(NORMALS_SAMPLER_ID);
        gl.glActiveTexture(GL20.GL_TEXTURE0);
        clear();
        gl.glBlendEquation(GL20.GL_FUNC_ADD);
        gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
        withShader(lightShader);
        lightShader.setUniformi("u_normalTexture", NORMALS_SAMPLER_ID);
        doPass(cameraPass);
        doPass(lightsPass);
        withShader(null);
        gl.glActiveTexture(GL20.GL_TEXTURE1);
        gl.glBindTexture(GL20.GL_TEXTURE_2D, 0);
        gl.glActiveTexture(GL20.GL_TEXTURE0);
        gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        toOutput();
        clear();
        withShader(objectsShader);
        objectsShader.setUniformi("u_lightTexture", LIGHT_SAMPLER_ID);
        lightSlot.get().asColorTexture().getTexture().bind(LIGHT_SAMPLER_ID);
        gl.glActiveTexture(GL20.GL_TEXTURE0);
        doPass(cameraPass);
        doPass(objectsPass);
        withShader(null);
        gl.glActiveTexture(GL20.GL_TEXTURE1);
        gl.glBindTexture(GL20.GL_TEXTURE_2D, 0);
        gl.glActiveTexture(GL20.GL_TEXTURE0);

        doPass(particelsPass);

        doPass(debugPass);
        doPass(uiPass);

        if (Test4Screen.show_n) {
            screenQuad(normalSlot.get().asColorTexture(), true);
        }
    }
}
