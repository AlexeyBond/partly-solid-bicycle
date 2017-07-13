package com.github.alexeybond.gdx_commons.application.impl.layers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.alexeybond.gdx_commons.application.Screen;
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;

/**
 *
 */
public class StageLayer extends LayerAdapter implements Drawable {
    private final String passName;
    private Stage stage;

    private BooleanProperty debugEnabledProp;

    public Stage stage() {
        return stage;
    }

    public StageLayer(String passName) {
        this.passName = passName;
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void onConnect(Screen screen) {
        this.stage = setupStage(this.stage = new Stage(screen.viewport()));
        screen.input().addSlaveProcessor(this.stage, true);
        screen.scene().getPass(passName).addDrawable(this);
        debugEnabledProp = screen.input().events()
                .event("debugEnabled", BooleanProperty.make(false));
    }

    @Override
    public void onDisconnect(Screen screen) {
        screen.scene().getPass(passName).removeDrawable(this);
        screen.input().removeSlaveProcessor(this.stage);
        this.stage.dispose();

        this.stage = null;
    }

    @Override
    public void draw(DrawingContext context) {
        // Stage calls batch.begin() so batch drawing should be ended
        context.state().end();
        stage.setDebugAll(debugEnabledProp.get());
        stage.draw();
    }

    /**
     * Override to add custom elements to stage.
     */
    protected Stage setupStage(Stage stage) {
        return stage;
    }
}
