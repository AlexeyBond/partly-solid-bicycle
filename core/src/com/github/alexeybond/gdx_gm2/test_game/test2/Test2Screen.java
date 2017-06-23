package com.github.alexeybond.gdx_gm2.test_game.test2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.application.Layer;
import com.github.alexeybond.gdx_commons.application.Screen;
import com.github.alexeybond.gdx_commons.application.impl.DefaultScreen;
import com.github.alexeybond.gdx_commons.application.impl.layers.GameLayerWith2DPhysicalGame;
import com.github.alexeybond.gdx_commons.application.util.ScreenUtils;
import com.github.alexeybond.gdx_commons.drawing.Technique;
import com.github.alexeybond.gdx_commons.drawing.tech.EDSLTechnique;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.PhysicsSystem;
import com.github.alexeybond.gdx_commons.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.gdx_commons.input.InputEvents;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.IntProperty;
import com.github.alexeybond.gdx_commons.util.parts.AParts;

/**
 *
 */
public class Test2Screen extends DefaultScreen {
    @Override
    protected Technique createTechnique() {
        return new EDSLTechnique() {
            @Override
            protected Runnable build() {
                return seq(
                        clearColor(),
                        pass("setup-main-camera"),
                        pass("game-objects"),
                        pass("game-debug")
                );
            }
        };
    }

    @Override
    protected void createLayers(AParts<Screen, Layer> layers) {
        super.createLayers(layers);

        ScreenUtils.enableToggleDebug(this, true);

        Game game = layers.add("game", new GameLayerWith2DPhysicalGame()).game();

        game.systems().<PhysicsSystem>get("physics").world().setGravity(new Vector2(0, -100));
        game.systems().<PhysicsSystem>get("physics").events()
                .<IntProperty<PhysicsSystem>>event("positionIterations").set(null, 4);
        game.systems().<PhysicsSystem>get("physics").events()
                .<IntProperty<PhysicsSystem>>event("velocityIterations").set(null, 6);
        game.systems().<PhysicsSystem>get("physics").events()
                .<FloatProperty<PhysicsSystem>>event("simulationStep").set(null, 0.005f);

        IoC.<GameDeclaration>resolve(
                "load game declaration",
                Gdx.files.internal("test/game.test.json")).apply(game);

        final Entity box = game.systems().<TaggingSystem>get("tagging").group("box").getOnly();

        input().keyEvent("S").subscribe(new EventListener<InputEvents, Event<InputEvents>>() {
            @Override
            public boolean onTriggered(InputEvents inputEvents, Event<InputEvents> event) {
                box.events().event("break").trigger(null);
                return true;
            }
        });
        input().keyEvent("R").subscribe(new EventListener<InputEvents, Event<InputEvents>>() {
            @Override
            public boolean onTriggered(InputEvents inputEvents, Event<InputEvents> event) {
                next(new Test2Screen());
                return true;
            }
        });
    }
}
