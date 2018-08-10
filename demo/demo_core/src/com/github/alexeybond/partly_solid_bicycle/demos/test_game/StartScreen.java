package com.github.alexeybond.partly_solid_bicycle.demos.test_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.alexeybond.partly_solid_bicycle.application.Layer;
import com.github.alexeybond.partly_solid_bicycle.application.Screen;
import com.github.alexeybond.partly_solid_bicycle.application.impl.DefaultScreen;
import com.github.alexeybond.partly_solid_bicycle.application.impl.layers.StageLayer;
import com.github.alexeybond.partly_solid_bicycle.demos.test_game.test5.Test5Screen;
import com.github.alexeybond.partly_solid_bicycle.drawing.Drawable;
import com.github.alexeybond.partly_solid_bicycle.drawing.DrawingContext;
import com.github.alexeybond.partly_solid_bicycle.drawing.Technique;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.util.parts.AParts;
import com.github.alexeybond.partly_solid_bicycle.demos.test_game.game.GameScreen;
import com.github.alexeybond.partly_solid_bicycle.demos.test_game.test2.Test2Screen;
import com.github.alexeybond.partly_solid_bicycle.demos.test_game.test3.Test3Screen;
import com.github.alexeybond.partly_solid_bicycle.demos.test_game.test4.Test4Screen;

/**
 *
 */
public class StartScreen extends DefaultScreen {
    @Override
    protected Technique createTechnique() {
        return new UIScreenTechnique();
    }

    @Override
    protected void createLayers(AParts<Screen, Layer> layers) {
        super.createLayers(layers);

        Skin skin = IoC.resolve("load skin", "ui/uiskin.json");

        final Stage stage = layers.add("ui", new StageLayer("ui")).stage();

        TextButton textButton1 = new TextButton("NEW GAME", skin);
        textButton1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToGame();
            }
        });

        TextButton textButton2 = new TextButton("DESTRUCTION", skin);
        textButton2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                next(new Test2Screen());
            }
        });

        TextButton textButton25 = new TextButton("PHYSICS & PLATFORMER CONTROLS", skin);
        textButton25.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                next(new Test3Screen());
            }
        });

        TextButton textButton26 = new TextButton("LIGHTING & ANIMATION", skin);
        textButton26.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                next(new Test4Screen());
            }
        });

        TextButton textButton5 = new TextButton("CONTROLLER", skin);
        textButton5.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                next(new Test5Screen());
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
        group.addActor(textButton25);
        group.addActor(textButton26);
        group.addActor(textButton5);
        group.addActor(textButton3);

        group.setFillParent(true);
        group.pad(50);
        group.center();

        group.layout();

        stage.addActor(group);

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

//        goToGame();
    }

    private void goToGame() {
        next(new GameScreen());
    }
}
