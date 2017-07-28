package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.helpers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public enum BodyAnchorsHelper {;
    public static Vector2 localAnchor(Body body, Vector2 anchor, boolean isLocal) {
        if (isLocal) return anchor;
        return body.getLocalPoint(anchor);
    }

    public static Vector2 globalAnchor(Body body, Vector2 anchor, boolean isLocal) {
        if (!isLocal) return anchor;
        return body.getWorldPoint(anchor);
    }
}
