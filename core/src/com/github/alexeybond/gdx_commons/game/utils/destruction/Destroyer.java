package com.github.alexeybond.gdx_commons.game.utils.destruction;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public interface Destroyer extends Pool.Poolable {
    DestroyerConfig config();

    /**
     * Verify configuration, create initial edges.
     *
     * The given shape is assumed to be a convex.
     */
    void prepare(List<Vector2> shape);

    /**
     * Start a crack at a center of shape.
     */
    void startCenter();

    /**
     * Start a crack at a point of a edge of a shape pointed by a ray.
     *
     * @throws IllegalArgumentException if given ray doesn't hit any edge of a shape
     */
    void startEdge(Vector2 rayStart, Vector2 rayDir);

    /**
     * Compute all cracks.
     *
     * @return list of resulting polygons (represented as lists of vertex positions).
     *          Multiple output polygons may refer to the same vectors.
     */
    ArrayList<ArrayList<Vector2>> compute();
}
