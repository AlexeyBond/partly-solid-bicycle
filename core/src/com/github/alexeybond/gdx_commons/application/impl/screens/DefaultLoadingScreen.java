package com.github.alexeybond.gdx_commons.application.impl.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.alexeybond.gdx_commons.application.Layer;
import com.github.alexeybond.gdx_commons.application.Screen;
import com.github.alexeybond.gdx_commons.application.impl.DefaultScreen;
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.drawing.Technique;
import com.github.alexeybond.gdx_commons.drawing.tech.EDSLTechnique;
import com.github.alexeybond.gdx_commons.util.parts.AParts;

/**
 *
 */
public class DefaultLoadingScreen extends DefaultScreen {
    private final AssetManager assetManager;

    public DefaultLoadingScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    protected Technique createTechnique() {
        return new EDSLTechnique() {
            @Override
            protected Runnable build() {
                return seq(
                        toOutput(),
                        clearColor(),
                        pass("setup-camera"),
                        pass("draw")
                );
            }
        };
    }

    @Override
    protected void createLayers(AParts<Screen, Layer> layers) {
        super.createLayers(layers);

        scene().getPass("setup-camera").addDrawable(new Drawable() {
            @Override
            public void draw(DrawingContext context) {
                Camera camera = viewport().getCamera();
                camera.position.set(0,0,0);
                camera.update();
                context.state().setProjection(camera.combined);
            }
        });
        scene().getPass("draw").addDrawable(new Drawable() {
            @Override
            public void draw(DrawingContext context) {
                ShapeRenderer shapeRenderer = context.state().beginLines();
                shapeRenderer.setColor(Color.WHITE);

                shapeRenderer.rect(-100, -100, 200f, 200f);

                shapeRenderer = context.state().beginFilled();
                shapeRenderer.setColor(Color.WHITE);

                shapeRenderer.rect(-100, -100, 200f * assetManager.getProgress(), 200f);
            }
        });
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (assetManager.update()) {
            next(prev());
        }
    }

    @Override
    protected boolean checkKeepPrevious() {
        return true;
    }
}
