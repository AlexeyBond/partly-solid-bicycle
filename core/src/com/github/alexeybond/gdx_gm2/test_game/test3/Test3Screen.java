package com.github.alexeybond.gdx_gm2.test_game.test3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.application.Layer;
import com.github.alexeybond.gdx_commons.application.Screen;
import com.github.alexeybond.gdx_commons.application.impl.DefaultScreen;
import com.github.alexeybond.gdx_commons.application.impl.layers.GameLayerWith2DPhysicalGame;
import com.github.alexeybond.gdx_commons.application.util.ScreenUtils;
import com.github.alexeybond.gdx_commons.drawing.Technique;
import com.github.alexeybond.gdx_commons.drawing.tech.EDSLTechnique;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.visitor.impl.ApplyGameDeclarationVisitor;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.PhysicsSystem;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.util.parts.AParts;

/**
 *
 */
public class Test3Screen extends DefaultScreen {
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

        GameDeclaration gameDeclaration = IoC.resolve(
                "load game declaration",
                Gdx.files.internal("test/game.platformer.test.json"));

        game = new ApplyGameDeclarationVisitor().doVisit(gameDeclaration, game);
    }
}
