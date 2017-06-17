package com.github.alexeybond.gdx_commons.game.utils.destruction.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

final class Vertex {
    Vertex clear() {
        markIter = -1;
        edges.clear();
        return this;
    }

    final Vector2 p = new Vector2();
    final Array<Edge> edges = new Array<Edge>(false, 8);

    int markIter;
    float markValue;
}
