package com.github.alexeybond.gdx_commons.game.utils.destruction.impl;

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

    float intersection(Edge edge, float defaultR, Vector2 tmp) {
        if (edge.contains(start)) return defaultR;

        Vector2 R = tmp.set(edge.v1.p).sub(start.p);
        Vector2 D = dir;
        Vector2 d = edge.d;
        if (D.x != 0 && D.y != 0) {
            float v = (R.x - D.x * R.y / D.y) / (D.x * d.y / D.y - d.x);
            if (v <= 0 || v > edge.len)
                return defaultR;
            float u = (R.x + d.x * v) / D.x;
            if (u <= 0 || u > maxLen)
                return defaultR;
            return u;
        }

        return defaultR;
    }


//    float intersection(Edge edge, float defaultR, Vector2 tmp) {
//        if (edge.contains(start)) return defaultR;
//
//        Vector2 end = new Vector2(dir)
//                .scl(maxLen * 1.2f).add(start);
//        Vector2 res = new Vector2();
//        if (Intersector.intersectSegments(edge.v1, edge.v2, start, end, res)) {
//            return res.sub(start).len();
//        }
//
//        return defaultR;
//    }

    Vertex skip(float distance, Vertex dst) {
        dst.p.set(dir).scl(distance).add(start.p);
        return dst;
    }
}
