package com.github.alexeybond.gdx_gm2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.alexeybond.gdx_commons.screen.AScreen;
import com.github.alexeybond.gdx_commons.screen.Application;

public class MyGdxGame extends Application {
    @Override
    protected AScreen initialScreen() {
        return new StartScreen(drawingState());
    }
//    SpriteBatch batch;
//    Texture img;
//    ShaderProgram shader_00;
//    FrameBuffer frameBuffer_00;
//
//    @Override
//    public void create() {
//        batch = new SpriteBatch();
//        img = new Texture("badlogic.jpg");
//        shader_00 = new ShaderProgram(
//                Gdx.files.internal("shaders/postprocess/vertex_00.glsl").readString(),
//                Gdx.files.internal("shaders/postprocess/fragment_00.glsl").readString()
//        );
//        frameBuffer_00 = new FrameBuffer(Pixmap.Format.RGBA8888,
//                Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
//    }
//
//    @Override
//    public void render() {
//        Gdx.gl.glClearColor(1, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        frameBuffer_00.begin();
//        batch.begin();
//        batch.setShader(null);
//        batch.draw(img, 0, 0);
//        batch.draw(img, img.getWidth(), 0);
//        batch.draw(img, img.getWidth(), img.getHeight());
//        batch.draw(img, img.getWidth() * 2, img.getHeight());
//        batch.draw(img, img.getWidth() * 2, img.getHeight() * 2);
//        batch.end();
//        frameBuffer_00.end();
//
//        batch.begin();
//        batch.setShader(shader_00);
//        {
//            float a1 = .01f * (float)(TimeUtils.millis() & 0xFFFF);
//            float a2 = a1 + 2f * (float) Math.PI / 3.0f;
//            float a3 = a2 + 2f * (float) Math.PI / 3.0f;
//            float b = 0.05f + 0.05f * 0.5f * (1.0f + (float) Math.sin(a1 * 0.365f));
//            shader_00.setUniformf("u_delta_1",
//                    b * (float) Math.sin(a1), b * (float) Math.cos(a1));
//            shader_00.setUniformf("u_delta_2",
//                    b * (float) Math.sin(a2), b * (float) Math.cos(a2));
//            shader_00.setUniformf("u_delta_3",
//                    b * (float) Math.sin(a3), b * (float) Math.cos(a3));
//        }
//        batch.draw(frameBuffer_00.getColorBufferTexture(), 0, 0);
//        batch.end();
//    }
//
//    @Override
//    public void dispose() {
//        batch.dispose();
//        img.dispose();
//    }
}
