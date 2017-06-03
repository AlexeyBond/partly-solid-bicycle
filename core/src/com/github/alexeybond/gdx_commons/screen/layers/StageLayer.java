package com.github.alexeybond.gdx_commons.screen.layers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.screen.ALayer;
import com.github.alexeybond.gdx_commons.screen.AScreen;

/**
 *
 */
public class StageLayer extends ALayer {
    private final Stage stage;

    /**
     * A layer that draws a LibGDX {@link Stage} and delegates input processing to it.
     *
     * Stage will use default screen's viewport to translate input coordinates.
     *
     * @param screen      parent screen
     * @param passName    name of the pass when to draw the stage
     */
    public StageLayer(AScreen screen, String passName) {
        super(screen);

        stage = new Stage(screen.viewport(), screen.scene().context().state().beginBatch());

        screen.scene().getPass(passName).addDrawable(new Drawable() {
            @Override
            public void draw(DrawingContext context) {
                // Stage calls batch.begin() so batch drawing should be ended
                context.state().end();
                stage.draw();
            }
        });
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    public Stage stage() {
        return stage;
    }
}
