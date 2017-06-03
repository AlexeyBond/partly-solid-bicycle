package com.github.alexeybond.gdx_gm2.test_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.screen.AScreen;
import com.github.alexeybond.gdx_commons.screen.layers.StageLayer;

/**
 *
 */
public class StartScreen extends AScreen {
    public StartScreen() {
        super(new UIScreenTechnique());

        final Stage stage = addLayerFront(new StageLayer(this, "ui")).stage();

        TextButton textButton1 = new TextButton("Button 1", new TextButton.TextButtonStyle(
                null, null, null, new BitmapFont()
        ));
        textButton1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Pressed 1 !!!1!!");
            }
        });

        TextButton textButton2 = new TextButton("Button 2", new TextButton.TextButtonStyle(
                null, null, null, new BitmapFont()
        ));
        textButton2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Pressed 2 !!!1!!");
            }
        });

        TextButton textButton3 = new TextButton("EXIT", new TextButton.TextButtonStyle(
                null, null, null, new BitmapFont()
        ));
        textButton3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        VerticalGroup group = new VerticalGroup();

        group.addActor(textButton1);
        group.addActor(textButton2);
        group.addActor(textButton3);

        group.setFillParent(true);
        group.pad(50);
        group.center();

        group.layout();

        stage.addActor(group);

        stage.setDebugAll(true);

        scene().getPass("dbg").addDrawable(new Drawable() {
            @Override
            public void draw(DrawingContext context) {
                context.state().setProjection(stage.getCamera().combined);
                ShapeRenderer sr = context.state().beginLines();

                sr.rect(10, 10,
                        context.getCurrentRenderTarget().width() - 20,
                        context.getCurrentRenderTarget().height() - 20);
            }
        });
    }

    @Override
    public void draw() {
        super.draw();
        Gdx.graphics.setTitle("Fps: " + Gdx.graphics.getFramesPerSecond());
    }
}
