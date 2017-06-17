package com.github.alexeybond.gdx_commons.game.utils.destruction.impl;

import com.badlogic.gdx.math.Vector2;

final class Edge {
    Vertex v1, v2;
    final Vector2 d = new Vector2();
    int order;

    float len;

    boolean alive;

    boolean disorder() {
        if ((--order) <= 0) {
            dispose();
            return true;
        }

        return false;
    }

    void dispose() {
        v1.edges.removeValue(this, true);
        v2.edges.removeValue(this, true);
        v1 = v2 = null;
        alive = false;
    }

    void set(Vertex v1, Vertex v2, int order) {
        v1.edges.add(this);
        v2.edges.add(this);
        this.v1 = v1;
        this.v2 = v2;
        this.order = order;
        this.d.set(v2.p).sub(v1.p);
        this.len = d.len();
        this.d.scl(1f / this.len);
        alive = true;
    }

    Vertex other(Vertex that) {
        if (that == this.v1) return v2;
        if (that == this.v2) return v1;
        throw new IllegalArgumentException();
    }

    boolean contains(Vertex vertex) {
        return v1 == vertex || v2 == vertex;
    }
}
