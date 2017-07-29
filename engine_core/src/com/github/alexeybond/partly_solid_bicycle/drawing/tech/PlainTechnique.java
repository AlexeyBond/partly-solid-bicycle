package com.github.alexeybond.partly_solid_bicycle.drawing.tech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.alexeybond.partly_solid_bicycle.drawing.*;
import com.github.alexeybond.partly_solid_bicycle.drawing.rt.FboTarget;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;

/**
 *
 */
public abstract class PlainTechnique implements Technique, Runnable {
    protected final GL20 gl = Gdx.gl20;

    private Scene scene;

    @Override
    public final Runnable initFor(Scene scene) {
        this.scene = scene;
        setup();
        return this;
    }

    @Override
    public final void run() {
        draw();
    }

    protected Scene scene() {
        return scene;
    }

    protected DrawingContext context() {
        return scene.context();
    }

    protected DrawingState state() {
        return context().state();
    }

    protected TargetSlot target(String name) {
        return context().getSlot(name);
    }

    protected Pass newPass(String name) {
        return scene.addPass(name, new Pass(context()));
    }

    protected void doPass(Pass pass) {
        pass.draw(context());
    }

    protected abstract void setup();

    protected abstract void draw();

    protected void withShader(ShaderProgram shader) {
        state().beginBatch().setShader(shader);
    }

    protected void clear(int bits) {
        gl.glClear(bits);
    }

    protected void clear() {
        clear(GL20.GL_COLOR_BUFFER_BIT);
    }

    protected void ensureMatchingFBO(TargetSlot slot, RenderTarget matchTarget) {
        RenderTarget target;

        try {
            target = slot.get();
        } catch (IllegalStateException e) {
            allocFBO(slot, matchTarget.getPixelsWidth(), matchTarget.getPixelsHeight());
            return;
        }

        if (target.getPixelsHeight() != matchTarget.getPixelsHeight() ||
                target.getPixelsWidth() != matchTarget.getPixelsWidth()) {
            slot.clear();
            allocFBO(slot, matchTarget.getPixelsWidth(), matchTarget.getPixelsHeight());
        }
    }

    protected void allocFBO(TargetSlot slot, int width, int height) {
        slot.set(new FboTarget(new FrameBuffer(
                Pixmap.Format.RGBA8888, width, height, false, false
        )));
    }

    protected void toTarget(TargetSlot slot) {
        context().renderTo(slot);
    }

    protected void toOutput() {
        context().renderTo(context().getOutputTarget());
    }

    protected void screenQuad(TextureRegion texture, boolean noFlipY) {
        Batch batch = state().beginBatch();
        batch.setProjectionMatrix(batch.getProjectionMatrix().idt());
        if (noFlipY) {
            batch.draw(texture, -1, 1, 2, -2);
        } else {
            batch.draw(texture, -1, -1, 2, 2);
        }
    }

    protected final void bindTexture(int sampler, Texture texture) {
        if (sampler != 0) {
            gl.glActiveTexture(GL20.GL_TEXTURE0 + sampler);
        }

        if (null != texture) {
            texture.bind(GL20.GL_TEXTURE0 + sampler);
        } else {
            gl.glBindTexture(GL20.GL_TEXTURE_2D, 0);
        }

        if (sampler != 0) {
            gl.glActiveTexture(GL20.GL_TEXTURE0);
        }
    }

    protected ShaderProgram loadShader(String vsFile, String psFile) {
        return IoC.resolve("load shader from files", vsFile, psFile);
    }
}
