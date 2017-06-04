package com.github.alexeybond.gdx_gm2.test_game.game;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.PhysicsSystem;
import com.github.alexeybond.gdx_commons.game.systems.input.InputSystem;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderSystem;
import com.github.alexeybond.gdx_commons.game.systems.timing.TimingSystem;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.screen.AScreen;
import com.github.alexeybond.gdx_commons.screen.layers.GameLayer;
import com.github.alexeybond.gdx_commons.screen.layers.StageLayer;

/**
 *
 */
public class GameScreen extends AScreen {
    public GameScreen() {
        super(new GameScreenTechnique());

        // -------------------- GAME SETUP --------------------
        Game game = addLayerFront(new GameLayer(this)).game();

        game.systems().add("physics", new PhysicsSystem());

        final PhysicsSystem physicsSystem = game.systems().get("physics");
        InputSystem inputSystem = game.systems().get("input");
        RenderSystem renderSystem = game.systems().get("render");
        TimingSystem timingSystem = game.systems().get("timing");



        final Box2DDebugRenderer box2DDebugRenderer = new Box2DDebugRenderer();
        scene().getPass("game-debug").addDrawable(new Drawable() {
            @Override
            public void draw(DrawingContext context) {
                context.state().end();
                box2DDebugRenderer.render(physicsSystem.world(), viewport().getCamera().combined);
            }
        });

        // --------------------  UI SETUP  --------------------
        Stage uiStage = addLayerFront(new StageLayer(this, "ui")).stage();
        uiStage.setDebugAll(true);
        Skin skin = IoC.resolve("load skin", "ui/uiskin.json");

        final ProgressBar progressBar = new ProgressBar(0, 100, 1, false, skin);
        Table table = new Table();

        table.add(new Label("Fuel", skin)).pad(20);
        table.add(progressBar).pad(20);

        table.setFillParent(true);
        table.top().left();

        uiStage.addActor(table);
    }
}
