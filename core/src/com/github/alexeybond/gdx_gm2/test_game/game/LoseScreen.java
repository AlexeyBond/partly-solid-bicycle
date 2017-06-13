package com.github.alexeybond.gdx_gm2.test_game.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.alexeybond.gdx_commons.application.Layer;
import com.github.alexeybond.gdx_commons.application.Screen;
import com.github.alexeybond.gdx_commons.application.impl.DefaultScreen;
import com.github.alexeybond.gdx_commons.application.impl.layers.StageLayer;
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.drawing.Scene;
import com.github.alexeybond.gdx_commons.drawing.Technique;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.util.parts.AParts;

import java.util.regex.Pattern;

/**
 *
 */
public class LoseScreen extends DefaultScreen {
    private final Game lostGame;
    private final Scene oldScene;

    public LoseScreen(Game lostGame, Scene oldScene) {
        this.lostGame = lostGame;
        this.oldScene = oldScene;
    }

    @Override
    protected void createLayers(AParts<Screen, Layer> layers) {
        super.createLayers(layers);

        oldScene.enableMatching(Pattern.compile(".*minimap.*"), false);
        oldScene.enableMatching(Pattern.compile(".*ui.*"), false);

        scene().getPass("lost-game").addDrawable(new Drawable() {
            @Override
            public void draw(DrawingContext context) {
                oldScene.context().setOutputTarget(context.getCurrentRenderTarget());
                oldScene.draw();
            }
        });

        Stage stage = layers.add("ui", new StageLayer("ui")).stage();

        Skin skin = IoC.resolve("load skin", "ui/uiskin.json");

        TextureRegion wasted = IoC.resolve("get texture region", "old/space-gc/wasted");
        Image image = new Image(wasted);
        Container labelContainer = new Container<Image>(image);
        labelContainer.center().setFillParent(true);
        stage.addActor(labelContainer);

        Button menuButton = new TextButton("MENU", skin);
        Container<Button> buttonContainer = new Container<Button>(menuButton);
        buttonContainer.pad(50);
        buttonContainer.bottom().setFillParent(true);
        stage.addActor(buttonContainer);

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                next(IoC.<Screen>resolve("initial screen"));
            }
        });
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        lostGame.update(dt * 0.2f);
    }

    @Override
    protected Technique createTechnique() {
        return new LoseScreenTechnique();
    }

    @Override
    protected boolean checkKeepPrevious() {
        return prev() == null;
    }
}
