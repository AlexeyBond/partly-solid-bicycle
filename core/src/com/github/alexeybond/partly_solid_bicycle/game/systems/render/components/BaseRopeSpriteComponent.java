package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.alexeybond.partly_solid_bicycle.drawing.DrawingContext;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.RenderSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.RenderComponent;

/**
 *
 */
public abstract class BaseRopeSpriteComponent implements Component, RenderComponent {
    private final float[] buf = new float[5 * 4];

    private final String passName;
    private final TextureRegion region;
    private final float vPerSegment;

    private RenderSystem renderSystem;

    protected BaseRopeSpriteComponent(String passName, TextureRegion region, float vPerSegment) {
        this.passName = passName;
        this.region = region;
        this.vPerSegment = vPerSegment;
    }

    @Override
    public void draw(DrawingContext context) {
        Batch batch = context.state().beginBatch();
        float[] points = getPoints();
        float[] buf = this.buf;

        int i = 0;

        float x0 = points[i++], y0 = points[i++];
        float sx0 = points[i++], sy0 = points[i++];
        float v0 = 0;

        while (i < points.length) {
            float x = points[i++], y = points[i++];
            float sx = points[i++], sy = points[i++];
            float v = v0 + vPerSegment;

            if (v > 1f) v = 1;

            int j = 0;

            buf[j++] = x0 + sx0;
            buf[j++] = y0 + sy0;
            buf[j++] = Color.WHITE.toFloatBits();
            buf[j++] = region.getU2();
            buf[j++] = v0;

            buf[j++] = x + sx;
            buf[j++] = y + sy;
            buf[j++] = Color.WHITE.toFloatBits();
            buf[j++] = region.getU2();
            buf[j++] = v;

            buf[j++] = x - sx;
            buf[j++] = y - sy;
            buf[j++] = Color.WHITE.toFloatBits();
            buf[j++] = region.getU();
            buf[j++] = v;

            buf[j++] = x0 - sx0;
            buf[j++] = y0 - sy0;
            buf[j++] = Color.WHITE.toFloatBits();
            buf[j++] = region.getU();
            buf[j++] = v0;

            batch.draw(region.getTexture(), buf, 0, buf.length);

            x0 = x; y0 = y;
            sx0 = sx; sy0 = sy;
            v0 = (v >= 1) ? 0 : v;
        }
    }

    @Override
    public void onConnect(Entity entity) {
        renderSystem = entity.game().systems().get("render");
        renderSystem.addToPass(passName, this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        renderSystem.removeFromPass(passName, this);
    }

    /**
     * Rope points in format `[x, y, sx, sy, ...]` where `(x,y)` is point location and
     * `(sx,sy)` is a vector perpendicular to average rope direction in that point.
     */
    public abstract float[] getPoints();
}
