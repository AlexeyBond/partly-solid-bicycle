package com.github.alexeybond.gdx_commons.screen.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.drawing.EDSLTechnique;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.screen.AScreen;

/**
 *
 */
public class DefaultLoadingScreen extends AScreen {
    private AssetManager assetManager;

    public DefaultLoadingScreen() {
        super(new EDSLTechnique() {
            @Override
            protected Runnable build() {
                return seq(
                        toOutput(),
                        clearColor(),
                        pass("setup-camera"),
                        pass("draw")
                );
            }
        });

        assetManager = IoC.resolve("asset manager");
    }

    @Override
    protected void create() {
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
    protected boolean keepPrev() {
        // Keep previous screen to go back to it when load finishes
        return true;
    }

    @Override
    public void draw() {
        super.draw();

        if (assetManager.update()) back();
    }
}
