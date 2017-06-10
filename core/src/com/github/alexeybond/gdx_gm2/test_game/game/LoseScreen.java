package com.github.alexeybond.gdx_gm2.test_game.game;

import com.badlogic.gdx.Gdx;
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.drawing.Scene;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.screen.AScreen;

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
    }

    @Override
    public void draw() {
        super.draw();

        lostGame.update(Gdx.graphics.getDeltaTime() * 0.2f);
    }
}
