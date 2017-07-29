package com.github.alexeybond.partly_solid_bicycle.application.impl.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.alexeybond.partly_solid_bicycle.application.Layer;
import com.github.alexeybond.partly_solid_bicycle.application.Screen;
import com.github.alexeybond.partly_solid_bicycle.application.impl.DefaultScreen;
import com.github.alexeybond.partly_solid_bicycle.drawing.Drawable;
import com.github.alexeybond.partly_solid_bicycle.drawing.DrawingContext;
import com.github.alexeybond.partly_solid_bicycle.drawing.Technique;
import com.github.alexeybond.partly_solid_bicycle.drawing.tech.EDSLTechnique;
import com.github.alexeybond.partly_solid_bicycle.util.parts.AParts;

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
