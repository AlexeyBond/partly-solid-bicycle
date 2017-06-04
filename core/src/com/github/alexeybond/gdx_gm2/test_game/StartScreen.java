package com.github.alexeybond.gdx_gm2.test_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.screen.AScreen;
import com.github.alexeybond.gdx_commons.screen.layers.StageLayer;
import com.github.alexeybond.gdx_gm2.test_game.game.GameScreen;

/**
 *
 */
public class StartScreen extends AScreen {
    public StartScreen() {
        super(new UIScreenTechnique());

        Skin skin = IoC.resolve("load skin", "ui/uiskin.json");

        final Stage stage = addLayerFront(new StageLayer(this, "ui")).stage();

        TextButton textButton1 = new TextButton("NEW GAME", skin);
        textButton1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToGame();
            }
        });

        TextButton textButton2 = new TextButton("Button 2", skin);
        textButton2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Pressed 2 !!!1!!");
            }
        });

        TextButton textButton3 = new TextButton("EXIT", skin);
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

        goToGame();
    }

    @Override
    public void draw() {
        super.draw();
//        Gdx.graphics.setTitle("Fps: " + Gdx.graphics.getFramesPerSecond());
    }

    private void goToGame() {
        next(new GameScreen());
    }
}
