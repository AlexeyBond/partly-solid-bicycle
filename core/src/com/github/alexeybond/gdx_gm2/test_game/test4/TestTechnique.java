package com.github.alexeybond.gdx_gm2.test_game.test4;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.github.alexeybond.gdx_commons.drawing.Pass;
import com.github.alexeybond.gdx_commons.drawing.TargetSlot;
import com.github.alexeybond.gdx_commons.drawing.tech.PlainTechnique;

/**
 *
 */
public class TestTechnique extends PlainTechnique {
    private final static int NORMALS_SAMPLER_ID = 1;
    private final static int LIGHT_SAMPLER_ID = 1;

    private ShaderProgram lightShader, objectsShader, normalShader, finalShader;

    private TargetSlot normalSlot;
    private TargetSlot lightSlot;
    private TargetSlot colorSlot;
    private TargetSlot refractionSlot;
    private Pass cameraPass, normalsPass, lightsPass, objectsPass, debugPass, uiPass;
    private Pass particlesPass;
    private Pass refractionPass;

    @Override
    protected void setup() {
        normalSlot = context().getSlot("normals");
        lightSlot = context().getSlot("light");
        colorSlot = context().getSlot("color");
        refractionSlot = context().getSlot("refraction");

        cameraPass = newPass("setup-main-camera");
        normalsPass = newPass("game-normals");
        lightsPass = newPass("game-light");
        objectsPass = newPass("game-objects");
        debugPass = newPass("game-debug");
        particlesPass = newPass("game-particles");
        refractionPass = newPass("game-refraction");
        uiPass = newPass("ui");

        lightShader = loadShader(
                "test/shaders/light_pass_vs.glsl",
                "test/shaders/light_pass_ps.glsl"
        );
        objectsShader = loadShader(
                "test/shaders/objects_pass_vs.glsl",
                "test/shaders/objects_pass_ps.glsl"
        );
        normalShader = loadShader(
                "test/shaders/normal_pass_vs.glsl",
                "test/shaders/normal_pass_ps.glsl"
        );
        finalShader = loadShader(
                "test/shaders/final_pass_vs.glsl",
                "test/shaders/final_pass_ps.glsl"
        );
    }

    private final Vector3 tmp = new Vector3();

    @Override
    protected void draw() {
        ensureMatchingFBO(normalSlot, context().getOutputTarget());
        ensureMatchingFBO(lightSlot, context().getOutputTarget());
        ensureMatchingFBO(colorSlot, context().getOutputTarget());
        ensureMatchingFBO(refractionSlot, context().getOutputTarget());

        toTarget(normalSlot);
        doPass(cameraPass);
        withShader(normalShader);
        doPass(normalsPass);
        withShader(null);

        toTarget(lightSlot);
        bindTexture(NORMALS_SAMPLER_ID, normalSlot.get().asColorTexture().getTexture());
        clear();
        Batch batch = state().beginBatch();
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
        withShader(lightShader);
        lightShader.setUniformi("u_normalTexture", NORMALS_SAMPLER_ID);
        doPass(cameraPass);
        doPass(lightsPass);
        withShader(null);
        bindTexture(NORMALS_SAMPLER_ID, null);
        gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        toTarget(colorSlot);
        clear();
        withShader(objectsShader);
        objectsShader.setUniformi("u_lightTexture", LIGHT_SAMPLER_ID);
        bindTexture(LIGHT_SAMPLER_ID, lightSlot.get().asColorTexture().getTexture());
        doPass(cameraPass);
        doPass(objectsPass);
        withShader(null);
        bindTexture(LIGHT_SAMPLER_ID, null);

        doPass(particlesPass);

        toTarget(refractionSlot);
        gl.glClearColor(0,0,0,0);
        clear();
        batch = state().beginBatch();
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
        batch.enableBlending();
        doPass(refractionPass);

        toOutput();
        clear();
        batch = state().beginBatch();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.enableBlending();
        float pxPerUnit = tmp.set(1,0,0).rot(batch.getProjectionMatrix()).len();
        bindTexture(1, refractionSlot.get().asColorTexture().getTexture());
        withShader(finalShader);
        finalShader.setUniformi("u_refractionMap", 1);
        finalShader.setUniformf("u_targetSizeInv",
                1f / (float) context().getOutputTarget().getPixelsWidth(),
                1f / (float) context().getOutputTarget().getPixelsHeight()
        );
        finalShader.setUniformf("u_pxPerUnit", pxPerUnit);

        screenQuad(colorSlot.get().asColorTexture(), true);
        batch.flush();
        bindTexture(1, null);

        withShader(null);
        doPass(debugPass);
        doPass(uiPass);

        if (Test4Screen.show_n) {
//            screenQuad(normalSlot.get().asColorTexture(), true);
            screenQuad(refractionSlot.get().asColorTexture(), true);
        }
    }
}
