package com.github.alexeybond.gdx_commons.game.common_components.decl;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.common_components.DestructibleComponent;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.utils.destruction.Destroyer;
import com.github.alexeybond.gdx_commons.game.utils.destruction.DestroyerConfig;
import com.github.alexeybond.gdx_commons.ioc.IoC;

import java.util.ArrayList;

/**
 *
 */
public class DestructibleDecl implements ComponentDeclaration {
    public static DestroyerConfig DEFAULT_DESTROYER_CONFIG = new DestroyerConfig();
    public static float[] DEFAULT_TEXTURE_PLACEMENT = {1, 0, 0.5f, 0, 1, 0.5f};

    /** Name of event that should cause destruction of object from center */
    public String centerDestructionEvent = null;

    /** Name of event to occur after destruction is complete */
    public String destructionEndEvent = "destructionEnd";

    /** Name of class of part entities */
    public String partClass;

    /** Name of polygon fixture component to create initial polygon from */
    public String fromFixture;

    /** Vertices of a polygon to use as initial polygon */
    public float[] fromShape;

    /** Name of texture (region) to use for parts of broken object */
    public String texture;

    /**
     * Texture placement: [ux, uy, u0, vx, vy, v0].
     *
     * <p>
     *     Texture coordinates for part vertices are computed as following:
     * </p>
     * <pre>
     *     u = (x * ux + y * uy) / texture_width + u0
     *     v = (x * vx + y * vy) / texture_height + v0
     * </pre>
     */
    public float[] texturePlacement = DEFAULT_TEXTURE_PLACEMENT;

    /** Configuration of the destroyer */
    public DestroyerConfig destroyerConfig = DEFAULT_DESTROYER_CONFIG;

    // Part fixture parameters
    public float density = 0.01f;
    public float restitution = 0;
    public float friction = 0.2f;

    @Override
    public Component create(GameDeclaration gameDeclaration) {
        return new DestructibleComponent(
                destructionEndEvent,
                centerDestructionEvent,
                initShapeSource(),
                gameDeclaration.getEntityClass(partClass),
                gameDeclaration,
                density, restitution, friction,
                IoC.<TextureRegion>resolve("get texture region", texture),
                texturePlacement,
                destroyerConfig,
                IoC.<Pool<Destroyer>>resolve("destroyers pool")
        );
    }

    private transient DestructibleComponent.ShapeSource shapeSource = null;

    private DestructibleComponent.ShapeSource initShapeSource() {
        if (null != shapeSource) return shapeSource;

        if (null == fromShape && null == fromFixture) {
            throw new IllegalArgumentException("One of fromShape and fromFixture should be defined.");
        }

        if (null != fromShape) {
            if (fromShape.length % 2 != 0 || fromShape.length < 6) {
                throw new IllegalArgumentException("Illegal fromShape item amount - " + fromShape.length);
            }

            ArrayList<Vector2> vertices = new ArrayList<Vector2>(fromShape.length / 2);

            for (int i = 0; i < fromShape.length; i += 2) {
                vertices.add(new Vector2(fromShape[i], fromShape[i + 1]));
            }

            return shapeSource = new DestructibleComponent.FixedShapeSource(vertices);
        }

        if (null != fromFixture) {
            return shapeSource = new DestructibleComponent.FixtureShapeSource(fromFixture);
        }

        return shapeSource;
    }
}
