package com.github.alexeybond.gdx_commons.game.utils.spriter_animation;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Loader;
import com.brashmonkey.spriter.Player;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderSystem;
import com.github.alexeybond.gdx_commons.game.systems.render.interfaces.RenderComponent;
import com.github.alexeybond.gdx_commons.game.systems.timing.TimingSystem;
import com.github.alexeybond.gdx_commons.game.utils.spriter_animation.impl.DrawerImpl;
import com.github.alexeybond.gdx_commons.game.utils.spriter_animation.impl.LoaderImpl;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.util.event.helpers.Subscription;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

import java.io.File;

/**
 * A component that displays a Spriter animation.
 *
 * <p>
 * The {@code "animation"} property of entity sets name of animation the component plays.
 * </p>
 */
public class SpriterAnimationComponent implements RenderComponent {
    private final String passName;

    private final Player player;
    private final DrawerImpl drawer;

    private final Subscription<ObjectProperty<String>> animationSub
            = new Subscription<ObjectProperty<String>>() {
        @Override
        public boolean onTriggered(ObjectProperty<String> event) {
            player.setAnimation(event.get());
            return true;
        }
    };

    private RenderSystem renderSystem;
    private Vec2Property positionProp;
    private FloatProperty rotationProp;
    private FloatProperty timeProp;
    private FloatProperty timeScaleProp;

    private float lastUpdTime;

    public SpriterAnimationComponent(String passName, Player player, DrawerImpl drawer) {
        this.passName = passName;
        this.player = player;
        this.drawer = drawer;
    }

    @Override
    public void draw(DrawingContext context) {
        float time = this.timeProp.get() * 1000f; // In millis

        if (lastUpdTime >= 0) {
            float dt = time - lastUpdTime;

            int roundSpeed = (int)(timeScaleProp.get() * dt);

            lastUpdTime = time - (dt - ((float) roundSpeed) / timeScaleProp.get());

            player.speed = roundSpeed;
        } else {
            lastUpdTime = time;
        }

        player.update();

        Vector2 pos = positionProp.ref();
        player.setPosition(pos.x, pos.y);
        player.setAngle(rotationProp.get());
        drawer.draw(player, context.state());
    }

    @Override
    public void onConnect(Entity entity) {
        renderSystem = entity.game().systems().get("render");
        renderSystem.addToPass(passName, this);
        positionProp = entity.events().event("position", Vec2Property.make());
        rotationProp = entity.events().event("rotation", FloatProperty.make());
        timeScaleProp = entity.events().event("timeScale", FloatProperty.make(1));
        animationSub.set(entity.events()
                .event("animation", ObjectProperty.<String>make())
                .use(ObjectProperty.STRING_LOADER));
        timeProp = entity.game().systems()
                .<TimingSystem>get("timing").events()
                .event("time");

        lastUpdTime = -1;
    }

    @Override
    public void onDisconnect(Entity entity) {
        renderSystem.removeFromPass(passName, this);
    }

    public static class Decl implements ComponentDeclaration {
        public String pass = "game-objects";

        public String animation;

        public String animationEntity;

        public String regionPrefix = "";
        public String regionSuffix = "";

        public float scale = 1;

        @Override
        public Component create(GameDeclaration gameDeclaration, Game game) {
            Data data = IoC.resolve("load spriter animation", animation);
            Loader<Sprite> loader = new LoaderImpl(data, regionPrefix, regionSuffix);
            loader.load(new File(animation).getParent()
                    .replace('\\', '/') // Fix for Window$, AssetManager hangs without this
                    + "/");

            Player player = new Player(data.getEntity(animationEntity));

            player.setScale(scale);

            DrawerImpl drawer = new DrawerImpl(loader);

            return new SpriterAnimationComponent(
                    pass,
                    player,
                    drawer
            );
        }
    }
}
