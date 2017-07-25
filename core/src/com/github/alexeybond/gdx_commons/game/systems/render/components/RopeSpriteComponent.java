package com.github.alexeybond.gdx_commons.game.systems.render.components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.FloatArray;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.helpers.Subscription;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;

/**
 *
 */
public class RopeSpriteComponent extends BaseRopeSpriteComponent {
    private final String positionsPropertyName;
    private final float width;

    private final Subscription<ObjectProperty<FloatArray>> positionsPropertySub
            = new Subscription<ObjectProperty<FloatArray>>() {
        @Override
        public boolean onTriggered(ObjectProperty<FloatArray> event) {
            pointsDirty = true;
            return true;
        }
    };

    private float[] points;
    private boolean pointsDirty;

    public RopeSpriteComponent(
            String passName,
            TextureRegion region,
            float vPerSegment,
            String positionsPropertyName,
            float width) {
        super(passName, region, vPerSegment);
        this.positionsPropertyName = positionsPropertyName;
        this.width = width;
    }

    @Override
    public void onConnect(Entity entity) {
        super.onConnect(entity);
        positionsPropertySub.set(entity.events()
                .event(positionsPropertyName, ObjectProperty.<FloatArray>make()));
        pointsDirty = true;
    }

    @Override
    public void onDisconnect(Entity entity) {
        super.onDisconnect(entity);
        positionsPropertySub.clear();
    }

    @Override
    public float[] getPoints() {
        if (pointsDirty) points = refreshPoints(points);
        return points;
    }

    private float[] refreshPoints(float[] points) {
        FloatArray origPoints = positionsPropertySub.event().get();
        if (null == points || points.length != origPoints.size * 2) {
            points = new float[origPoints.size * 2];
        }

        int i = 0, j = 0;

        float x0 = origPoints.get(0), y0 = origPoints.get(1), x ,y, x1, y1, dx, dy, ld;

        while (i < origPoints.size) {
            x = origPoints.get(i++);
            y = origPoints.get(i++);

            if (i < origPoints.size) {
                x1 = origPoints.get(i);
                y1 = origPoints.get(i + 1);
            } else {
                x1 = x;
                y1 = y;
            }

            dx = x1 - x0;
            dy = y1 - y0;
            ld = .5f * width / (float) Math.sqrt(dx * dx + dy * dy);

            dx = dx * ld; dy = dy * ld;

            points[j++] = x;
            points[j++] = y;
            points[j++] = dy;
            points[j++] = -dx;

            x0 = x; y0 = y;
        }

        return points;
    }
}
