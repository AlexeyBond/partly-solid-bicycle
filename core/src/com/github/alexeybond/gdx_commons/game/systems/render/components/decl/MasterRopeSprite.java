package com.github.alexeybond.gdx_commons.game.systems.render.components.decl;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.components.RopeSpriteComponent;
import com.github.alexeybond.gdx_commons.ioc.IoC;

public class MasterRopeSprite implements ComponentDeclaration {
    public String pass = "game-objects";

    public String positionsProperty = "ropeSegmentPositions";

    public String sprite;

    public float segmentScale = 1f;

    public float width = 10;

    private transient TextureRegion region;

    private TextureRegion region() {
        if (null == region) region = IoC.resolve("get texture region", sprite);
        return region;
    }

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new RopeSpriteComponent(
                pass,
                region(),
                segmentScale,
                positionsProperty,
                width
        );
    }
}
