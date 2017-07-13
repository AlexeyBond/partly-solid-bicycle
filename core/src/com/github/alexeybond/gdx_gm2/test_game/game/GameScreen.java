package com.github.alexeybond.gdx_gm2.test_game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.alexeybond.gdx_commons.application.Layer;
import com.github.alexeybond.gdx_commons.application.Screen;
import com.github.alexeybond.gdx_commons.application.impl.DefaultScreen;
import com.github.alexeybond.gdx_commons.application.impl.layers.GameLayerWith2DPhysicalGame;
import com.github.alexeybond.gdx_commons.application.impl.layers.StageLayer;
import com.github.alexeybond.gdx_commons.application.util.ScreenUtils;
import com.github.alexeybond.gdx_commons.drawing.Technique;
import com.github.alexeybond.gdx_commons.drawing.rt.ScreenTarget;
import com.github.alexeybond.gdx_commons.drawing.rt.ViewportTarget;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.visitor.impl.ApplyGameDeclarationVisitor;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.PhysicsSystem;
import com.github.alexeybond.gdx_commons.game.systems.input.InputSystem;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderSystem;
import com.github.alexeybond.gdx_commons.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.gdx_commons.game.systems.timing.TimingSystem;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.modules.Modules;
import com.github.alexeybond.gdx_commons.resource_management.modules.ResourcesListModule;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.parts.AParts;

/**
 *
 */
public class GameScreen extends DefaultScreen {
    @Override
    protected Technique createTechnique() {
        return new GameScreenTechnique();
    }

    @Override
    protected void createModules(Modules modules) {
        super.createModules(modules);

        modules.add(new ResourcesListModule("old/space-gc/preload.json"));
    }

    @Override
    protected void createLayers(AParts<Screen, Layer> layers) {
        super.createLayers(layers);

        ScreenUtils.enableToggleDebug(this, false);

        scene().context().getSlot("minimapViewport").set(
                new ViewportTarget(
                        ScreenTarget.INSTANCE,
                        new Viewport() {
                            {
                                setScreenBounds(32,32,256,256);
                                setWorldSize(256, 256);
                                setCamera(new OrthographicCamera());
                            }

                            @Override
                            public void update(int screenWidth, int screenHeight, boolean centerCamera) {
                                super.apply(centerCamera);
                            }
                        }
                ));

        //
//        layers.add("music", new MusicLayer("old/space-gc/sound/background.mp3", true));

        // -------------------- GAME SETUP --------------------
        final Game game = layers.add("game", new GameLayerWith2DPhysicalGame()).game();

        game.systems().add("physics", new PhysicsSystem());
        game.systems().add("tagging", new TaggingSystem());

        final PhysicsSystem physicsSystem = game.systems().get("physics");
        InputSystem inputSystem = game.systems().get("input");
        RenderSystem renderSystem = game.systems().get("render");
        TimingSystem timingSystem = game.systems().get("timing");
        TaggingSystem taggingSystem = game.systems().get("tagging");

        GameDeclaration gameDeclaration = IoC.resolve(
                "load game declaration",
                Gdx.files.internal("old/space-gc/game.json"));

        new ApplyGameDeclarationVisitor().doVisit(gameDeclaration, game);

        game.events().event("lose").subscribe(new EventListener<Event>() {
            @Override
            public boolean onTriggered(Event event) {
                next(new LoseScreen(game, scene()));

                return true;
            }
        });

        // --------------------  UI SETUP  --------------------
        final Stage uiStage = layers.add("ui", new StageLayer("ui")).stage();
        Skin skin = IoC.resolve("load skin", "ui/uiskin.json");

        final ProgressBar progressBar = new ProgressBar(0, 100, 1, false, skin);
        Table table = new Table();

        table.add(new Label("Fuel", skin)).pad(20);
        table.add(progressBar).pad(20);

        table.setFillParent(true);
        table.top().left();

        uiStage.addActor(table);

        //

        taggingSystem.group("player").getOnly().events().<FloatProperty>event("fuel")
                .subscribe(new EventListener<FloatProperty>() {
            @Override
            public boolean onTriggered(FloatProperty event) {
                progressBar.setValue(event.get());
                return true;
            }
        });
    }
}
