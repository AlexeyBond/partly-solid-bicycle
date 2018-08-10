package com.github.alexeybond.partly_solid_bicycle.demos.test_game.test5;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.alexeybond.partly_solid_bicycle.application.Layer;
import com.github.alexeybond.partly_solid_bicycle.application.Screen;
import com.github.alexeybond.partly_solid_bicycle.application.impl.DefaultScreen;
import com.github.alexeybond.partly_solid_bicycle.application.impl.layers.GameLayerWith2DPhysicalGame;
import com.github.alexeybond.partly_solid_bicycle.drawing.Drawable;
import com.github.alexeybond.partly_solid_bicycle.drawing.DrawingContext;
import com.github.alexeybond.partly_solid_bicycle.drawing.Technique;
import com.github.alexeybond.partly_solid_bicycle.drawing.projection.OrthoProjection;
import com.github.alexeybond.partly_solid_bicycle.drawing.tech.EDSLTechnique;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.visitor.impl.ApplyGameDeclarationVisitor;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventListener;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.BooleanProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.FloatProperty;
import com.github.alexeybond.partly_solid_bicycle.util.parts.AParts;
import main.java.com.github.alexeybond.partly_solid_bicycle.ext.controllers.AnyControllerSystem;

public class Test5Screen extends DefaultScreen {
    @Override
    protected Technique createTechnique() {
        return new EDSLTechnique() {
            @Override
            protected Runnable build() {
                return seq(
                        toOutput(),
                        clearColor(),
                        withProjection(OrthoProjection.SCREEN_MID),
                        pass("visualize")
                );
            }
        };
    }

    @Override
    protected void createLayers(AParts<Screen, Layer> layers) {
        super.createLayers(layers);

        Game game = layers.add("game", new GameLayerWith2DPhysicalGame()).game();

        game.systems().add("controller", new AnyControllerSystem());

        GameDeclaration gameDeclaration = IoC.resolve(
                "load game declaration",
                Gdx.files.internal("test/controllers.test.json"));

        game = new ApplyGameDeclarationVisitor().doVisit(gameDeclaration, game);

        final Entity entity = game.systems().<TaggingSystem>get("tagging").group("input").getOnly();

        final FloatProperty axisX = entity.events().event("axisX");
        final FloatProperty axisY = entity.events().event("axisY");

        final BooleanProperty button0 = entity.events().event("button0");
        final BooleanProperty button1 = entity.events().event("button1");
        final BooleanProperty button2 = entity.events().event("button2");
        final BooleanProperty button3 = entity.events().event("button3");

        scene().getPass("visualize").addDrawable(new Drawable() {
            @Override
            public void draw(DrawingContext context) {
                ShapeRenderer sr = context.state().beginLines();

                sr.setColor(Color.WHITE);

                sr.box(-100, -100, 0, 200, 200, 0);
                sr.line(0, -50, 0, 50);
                sr.line(-50, 0, 50, 0);

                sr.circle(
                        axisX.get() * 100f,
                        axisY.get() * -100f,
                        10
                );

                sr = context.state().beginFilled();

                sr.setColor(button0.get() ? Color.WHITE : Color.GRAY);
                sr.box(-150, -150, 0, 10, 10, 0);

                sr.setColor(button1.get() ? Color.WHITE : Color.GRAY);
                sr.box(-130, -150, 0, 10, 10, 0);

                sr.setColor(button2.get() ? Color.WHITE : Color.GRAY);
                sr.box(-110, -150, 0, 10, 10, 0);

                sr.setColor(button3.get() ? Color.WHITE : Color.GRAY);
                sr.box(-90, -150, 0, 10, 10, 0);
            }
        });

        input().keyEvent("R").subscribe(new EventListener<BooleanProperty>() {
            @Override
            public boolean onTriggered(BooleanProperty event) {
                if (!event.get()) return false;
                next(new Test5Screen());
                return true;
            }
        });
    }
}
