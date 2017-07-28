package com.github.alexeybond.partly_solid_bicycle.ext.destruction.impl;

import com.badlogic.gdx.math.Vector2;

/**
 *
 */
final class Ray {
    Vertex start;

    float maxLen;

    final Vector2 dir = new Vector2();

    Ray set(Vertex start, Vector2 dir, float maxLen) {
        this.start = start;
        this.dir.set(dir);
        this.maxLen = maxLen;
        return this;
    }

    float intersection(Edge edge, float curMax) {
        if (edge.contains(start)) return curMax;

        Vector2 startV = start.p;
        float startX = startV.x;
        float startY = startV.y;
        float dirX = dir.x;
        float dirY = dir.y;

        float endX = dirX * curMax + startX;
        float endY = dirY * curMax + startY;

        Vector2 edge1 = edge.v1.p, edge2 = edge.v2.p;
        float e1x = edge1.x, e1y = edge1.y;
        float e2x = edge2.x, e2y = edge2.y;

        boolean hit = true;
        float d = (endY - startY) * (e2x - e1x) - (endX - startX) * (e2y - e1y);
        if (d == 0) hit = false;

        float yd = e1y - startY;
        float xd = e1x - startX;
        float ua = ((endX - startX) * yd - (endY - startY) * xd) / d;
        if (ua < 0 || ua > 1) hit = false;

        float ub = ((e2x - e1x) * yd - (e2y - e1y) * xd) / d;
        if (ub < 0 || ub > 1) hit = false;

        return hit ? ub * curMax : curMax;
    }

    Vertex skip(float distance, Vertex dst) {
        dst.p.set(dir).scl(distance).add(start.p);
        return dst;
    }
}
