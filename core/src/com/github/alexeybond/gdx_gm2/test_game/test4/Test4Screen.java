package com.github.alexeybond.gdx_gm2.test_game.test4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.application.Layer;
import com.github.alexeybond.gdx_commons.application.Screen;
import com.github.alexeybond.gdx_commons.application.impl.DefaultScreen;
import com.github.alexeybond.gdx_commons.application.impl.layers.GameLayerWith2DPhysicalGame;
import com.github.alexeybond.gdx_commons.application.util.ScreenUtils;
import com.github.alexeybond.gdx_commons.drawing.Technique;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.visitor.impl.ApplyGameDeclarationVisitor;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.PhysicsSystem;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces.APhysicsSystem;
import com.github.alexeybond.gdx_commons.game.utils.spriter_animation.modules.SpriterAnimationModule;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.modules.Modules;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;
import com.github.alexeybond.gdx_commons.util.parts.AParts;

/**
 *
 */
public class Test4Screen extends DefaultScreen {
    public static boolean show_n = false;

    @Override
    protected Technique createTechnique() {
        return new TestTechnique();
    }

    @Override
    protected void createModules(Modules modules) {
        super.createModules(modules);
        modules.add(new SpriterAnimationModule());
    }

    @Override
    protected void createLayers(AParts<Screen, Layer> layers) {
        super.createLayers(layers);

        ScreenUtils.enableToggleDebug(this, true);

        Game game = layers.add("game", new GameLayerWith2DPhysicalGame()).game();

        GameDeclaration gameDeclaration = IoC.resolve(
                "load game declaration",
                Gdx.files.internal("test/game.lighting.test.json"));

        game = new ApplyGameDeclarationVisitor().doVisit(gameDeclaration, game);

        APhysicsSystem physicsSystem = game.systems().get("physics");
        physicsSystem.world().setGravity(new Vector2(0, -100));

        input().keyEvent("R").subscribe(new EventListener<BooleanProperty>() {
            @Override
            public boolean onTriggered(BooleanProperty event) {
                if (!event.get()) return false;
                next(new Test4Screen());
                return true;
            }
        });

        input().keyEvent("N").subscribe(new EventListener<BooleanProperty>() {
            @Override
            public boolean onTriggered(BooleanProperty event) {
                if (event.get()) return false;
                show_n = !show_n;
                return true;
            }
        });
    }
}
