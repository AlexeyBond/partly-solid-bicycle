package com.github.alexeybond.partly_solid_bicycle.ext.spriter_animation.components;

import com.github.alexeybond.partly_solid_bicycle.drawing.DrawingContext;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.RenderSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.RenderComponent;
import com.github.alexeybond.partly_solid_bicycle.ext.spriter_animation.impl.DrawerImpl;
import com.github.alexeybond.partly_solid_bicycle.ext.spriter_animation.impl.LoaderImpl;

/**
 * Component that draws a Spriter animation synchronized with some other {@link SpriterAnimationComponent Spriter
 * animation component} of owner entity.
 */
public class SlaveSpriterAnimationComponent implements RenderComponent {
    private final String passName;
    private final String masterName;
    private final String regionPrefix, regionSuffix;

    private SpriterAnimationComponent master;

    private RenderSystem renderSystem;

    private LoaderImpl loader;
    private DrawerImpl drawer;

    public SlaveSpriterAnimationComponent(
            String passName,
            String masterName,
            String regionPrefix,
            String regionSuffix) {
        this.passName = passName;
        this.masterName = masterName;
        this.regionPrefix = regionPrefix;
        this.regionSuffix = regionSuffix;
    }

    @Override
    public void draw(DrawingContext context) {
        drawer.draw(master.player(), context.state());
    }

    @Override
    public void onConnect(Entity entity) {
        master = entity.components().get(masterName);

        loader = new LoaderImpl(master.data(), regionPrefix, regionSuffix);
        loader.load(master.loader().root());
        drawer = new DrawerImpl(loader);

        renderSystem = entity.game().systems().get("render");
        renderSystem.addToPass(passName, this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        renderSystem.removeFromPass(passName, this);

        loader.dispose();
    }

    public static class Decl implements ComponentDeclaration {
        public String pass;

        public String master = "spriter animation";

        public String regionPrefix = "";
        public String regionSuffix = "";

        @Override
        public Component create(GameDeclaration gameDeclaration, Game game) {
            return new SlaveSpriterAnimationComponent(pass, master, regionPrefix, regionSuffix);
        }
    }
}
