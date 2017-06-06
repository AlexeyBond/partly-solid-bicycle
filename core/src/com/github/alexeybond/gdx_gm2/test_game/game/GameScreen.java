package com.github.alexeybond.gdx_gm2.test_game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.PhysicsSystem;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.DynamicBoxComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.EmptyBodyComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.FixtureDefFixtureComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.StaticBoxBodyComponent;
import com.github.alexeybond.gdx_commons.game.systems.input.InputSystem;
import com.github.alexeybond.gdx_commons.game.systems.input.components.KeyBindingsComponent;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderSystem;
import com.github.alexeybond.gdx_commons.game.systems.render.components.AttachedContinuousParticleEffect;
import com.github.alexeybond.gdx_commons.game.systems.render.components.BackgroundLoopComponent;
import com.github.alexeybond.gdx_commons.game.systems.render.components.OrthographicCameraComponent;
import com.github.alexeybond.gdx_commons.game.systems.render.components.StaticSpriteComponent;
import com.github.alexeybond.gdx_commons.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.gdx_commons.game.systems.tagging.components.SingleTagComponent;
import com.github.alexeybond.gdx_commons.game.systems.timing.TimingSystem;
import com.github.alexeybond.gdx_commons.input.InputEvents;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.screen.AScreen;
import com.github.alexeybond.gdx_commons.screen.layers.GameLayer;
import com.github.alexeybond.gdx_commons.screen.layers.StageLayer;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;
import com.github.alexeybond.gdx_gm2.test_game.game.components.ControlledAttractor;
import com.github.alexeybond.gdx_gm2.test_game.game.components.FuelItem;
import com.github.alexeybond.gdx_gm2.test_game.game.components.SpaceshipCollector;
import com.github.alexeybond.gdx_gm2.test_game.game.components.SpaceshipEngines;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 *
 */
public class GameScreen extends AScreen {
    public GameScreen() {
        super(new GameScreenTechnique());

        // -------------------- GAME SETUP --------------------
        Game game = addLayerFront(new GameLayer(this)).game();

        game.systems().add("physics", new PhysicsSystem());
        game.systems().add("tagging", new TaggingSystem());

        final PhysicsSystem physicsSystem = game.systems().get("physics");
        InputSystem inputSystem = game.systems().get("input");
        RenderSystem renderSystem = game.systems().get("render");
        TimingSystem timingSystem = game.systems().get("timing");

        PhysicsDefs physicsDefs = new PhysicsDefs();

        final Entity player = new Entity(game);
        OrthographicCameraComponent cameraComponent = new OrthographicCameraComponent("setup-main-camera");
        player.components().add("camera", cameraComponent);
        player.components().add("body", new EmptyBodyComponent("DynamicBody"));
        player.components().add("bodyFixture",
                new FixtureDefFixtureComponent(physicsDefs.spaceshipBodyFixture));
        player.components().add("trigger",
                new FixtureDefFixtureComponent(
                        "collectorHitStart", "collectorHitEnd", physicsDefs.spaceshipTriggerFixture));
        player.components().add("collector", new SpaceshipCollector());
        player.components().add("attractorTrigger",
                new FixtureDefFixtureComponent(
                        "attractorHitBegin", "attractorHitEnd", physicsDefs.spaceshipAttractorTrigger));
        player.components().add("attractorController", new ControlledAttractor());
        player.components().add("background", BackgroundLoopComponent.withCamera(
                "game-background",
                IoC.<Texture>resolve("load texture loop", "old/space-gc/kosmosbg.png"),
                cameraComponent.camera()
        ));
        player.components().add("tag:player", new SingleTagComponent("player"));
        player.components().add("engines", new SpaceshipEngines());
        player.components().add("sprite", new StaticSpriteComponent(
                "game-objects",
                "old/space-gc/256x256spaceship_1"
        ));
        player.components().add("leftEngineParticles", new AttachedContinuousParticleEffect(
                "game-particles",
                "particles/spaceship-engine.p",
                new Vector2(-100,-70), -90f,
                "leftEngineEnabled"
        ));
        player.components().add("rightEngineParticles", new AttachedContinuousParticleEffect(
                "game-particles",
                "particles/spaceship-engine.p",
                new Vector2(95,-70), -90f,
                "rightEngineEnabled"
        ));
        player.components().add("attractorParticles", new AttachedContinuousParticleEffect(
                "game-particles",
                "particles/spaceship-attractor.p",
                new Vector2(0, 440), -90f,
                "attractorEnabled"
        ));
        player.components().add("keyboardInput", new KeyBindingsComponent(new HashMap<String, String>() {{
            put("rightEngineControl", "X");
            put("leftEngineControl", "Z");
            put("attractorControl", "S");
        }}));

        player.events().<FloatProperty<Component>>event("fuel").set(null, 100);
        player.events().<FloatProperty<Component>>event("zoom").set(null, .5f);

        Entity thing = new Entity(game);
        thing.components().add("body", new DynamicBoxComponent());
        thing.components().add("fuel", new FuelItem(10));
        thing.events().<Vec2Property<Component>>event("position").set(null, 230, 400);

        Entity thing2 = new Entity(game);
        thing2.components().add("body", new StaticBoxBodyComponent());
        thing2.components().add("fuel", new FuelItem(10));
        thing2.events().<Vec2Property<Component>>event("position").set(null, 180, -256);

        Json json = new Json();
        GameDeclaration gameDeclaration = json.fromJson(
                GameDeclaration.class,
                Gdx.files.internal("old/space-gc/game.json"));
        gameDeclaration.apply(game);

//        physicsSystem.world().setGravity(new Vector2(0, -10));

        final Box2DDebugRenderer box2DDebugRenderer = new Box2DDebugRenderer();
        scene().getPass("game-debug").addDrawable(new Drawable() {
            @Override
            public void draw(DrawingContext context) {
                context.state().end();
                Camera camera = player.components().<OrthographicCameraComponent>get("camera").camera();
                box2DDebugRenderer.render(physicsSystem.world(), camera.combined);
            }
        });

        // --------------------  UI SETUP  --------------------
        final Stage uiStage = addLayerFront(new StageLayer(this, "ui")).stage();
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

        player.events().<FloatProperty<Component>>event("fuel")
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
