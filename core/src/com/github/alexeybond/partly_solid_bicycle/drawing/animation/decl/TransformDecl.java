package com.github.alexeybond.partly_solid_bicycle.drawing.animation.decl;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Object describing how transformation (origin point and scale) of animation keyframe should be computed.
 */
public class TransformDecl {
    public float[] offset;
    public boolean center = true;

    public float scale = 1f;

    public float rotate = 0f;

    public Vector2 getOffset(Vector2 dst, TextureRegion region) {
        if (center) {
            dst.set(.5f * (float)region.getRegionWidth(), .5f * (float)region.getRegionHeight());
        } else {
            dst.set(0,0);
        }

        if (null != offset && offset.length != 0) {
            dst.add(offset[0], offset[1]);
        }

        return dst;
    }

    public static final TransformDecl DEFAULT = new TransformDecl();
}
