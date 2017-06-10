package com.github.alexeybond.gdx_gm2.test_game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.drawing.Scene;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.screen.AScreen;
import com.github.alexeybond.gdx_commons.screen.layers.StageLayer;

import java.util.regex.Pattern;

/**
 *
 */
public class LoseScreen extends AScreen {
    private final Game lostGame;
    private final Scene oldScene;

    public LoseScreen(Game lostGame, Scene oldScene) {
        super(new LoseScreenTechnique());

        this.lostGame = lostGame;
        this.oldScene = oldScene;
    }

    @Override
    protected boolean keepPrev() {
        // Unless there is already no way back we still have to keep a reference
        // to prevent scene context destruction
        return true;
    }

    @Override
    protected void create() {
        oldScene.enableMatching(Pattern.compile(".*minimap.*"), false);
        oldScene.enableMatching(Pattern.compile(".*ui.*"), false);

        scene().getPass("lost-game").addDrawable(new Drawable() {
            @Override
            public void draw(DrawingContext context) {
                oldScene.context().setOutputTarget(context.getCurrentRenderTarget());
                oldScene.draw();
            }
        });

        Stage stage = addLayerFront(new StageLayer(this, "ui")).stage();

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
                next(IoC.<AScreen>resolve("initial screen"));
            }
        });
    }

    @Override
    public void draw() {
        super.draw();

        lostGame.update(Gdx.graphics.getDeltaTime() * 0.2f);
    }
}
