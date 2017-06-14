package com.github.alexeybond.gdx_gm2.test_game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.drawing.Technique;
import com.github.alexeybond.gdx_commons.drawing.rt.ScreenTarget;
import com.github.alexeybond.gdx_commons.drawing.rt.ViewportTarget;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.PhysicsSystem;
import com.github.alexeybond.gdx_commons.game.systems.input.InputSystem;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderSystem;
import com.github.alexeybond.gdx_commons.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.gdx_commons.game.systems.timing.TimingSystem;
import com.github.alexeybond.gdx_commons.input.InputEvents;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.modules.Modules;
import com.github.alexeybond.gdx_commons.resource_management.modules.ResourcesListModule;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;
import com.github.alexeybond.gdx_commons.util.parts.AParts;

import java.util.regex.Pattern;

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

        // -------------------- GAME SETUP --------------------
        final Game game = layers.add("game", new GameLayerWith2DPhysicalGame()).game();

        game.systems().add("physics", new PhysicsSystem());
        game.systems().add("tagging", new TaggingSystem());

        final PhysicsSystem physicsSystem = game.systems().get("physics");
        InputSystem inputSystem = game.systems().get("input");
        RenderSystem renderSystem = game.systems().get("render");
        TimingSystem timingSystem = game.systems().get("timing");
        TaggingSystem taggingSystem = game.systems().get("tagging");

        IoC.<GameDeclaration>resolve(
                "load game declaration",
                Gdx.files.internal("old/space-gc/game.json")).apply(game);

        game.events().event("lose").subscribe(new EventListener<GameSystem, Event<GameSystem>>() {
            @Override
            public boolean onTriggered(GameSystem gameSystem, Event<GameSystem> event) {
                next(new LoseScreen(game, scene()));

                return true;
            }
        });

        final Box2DDebugRenderer box2DDebugRenderer = new Box2DDebugRenderer();
        scene().getPass("game-debug").addDrawable(new Drawable() {
            @Override
            public void draw(DrawingContext context) {
                context.state().end();
                Camera camera = game.events().<ObjectProperty<Camera, GameSystem>>event("mainCamera").get();
                box2DDebugRenderer.render(physicsSystem.world(), camera.combined);
            }
        });

        // --------------------  UI SETUP  --------------------
        final Stage uiStage = layers.add("ui", new StageLayer("ui")).stage();
        uiStage.setDebugAll(true);
        Skin skin = IoC.resolve("load skin", "ui/uiskin.json");

        final ProgressBar progressBar = new ProgressBar(0, 100, 1, false, skin);
        Table table = new Table();

        table.add(new Label("Fuel", skin)).pad(20);
        table.add(progressBar).pad(20);

        table.setFillParent(true);
        table.top().left();

        uiStage.addActor(table);

        //

        taggingSystem.group("player").getOnly().events().<FloatProperty<Component>>event("fuel")
                .subscribe(new EventListener<Component, FloatProperty<Component>>() {
            @Override
            public boolean onTriggered(Component component, FloatProperty<Component> event) {
                progressBar.setValue(event.get());
                return true;
            }
        });

        input().keyEvent("`").subscribe(new EventListener<InputEvents, BooleanProperty<InputEvents>>() {
            boolean debug = false;

            private void set() {
                scene().enableMatching(Pattern.compile(".*debug.*"), debug);
                uiStage.setDebugAll(debug);
            }

            {set();}

            @Override
            public boolean onTriggered(InputEvents inputEvents, BooleanProperty<InputEvents> event) {
                if (event.get()) return false;
                debug = !debug;
                set();
                return true;
            }
        });
    }
}
