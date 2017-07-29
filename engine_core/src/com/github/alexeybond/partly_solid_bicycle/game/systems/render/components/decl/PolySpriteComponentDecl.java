package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.decl;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.PolySpriteComponent;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;

public class PolySpriteComponentDecl implements ComponentDeclaration {
    public String pass = "game-objects";

    public float[] vertices;
    public short[] triangles;

    public String texture;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        TextureRegion region = IoC.resolve("get texture region", texture);

        return new PolySpriteComponent(
                pass,
                vertices,
                triangles,
                region.getTexture()
        );
    }
}
