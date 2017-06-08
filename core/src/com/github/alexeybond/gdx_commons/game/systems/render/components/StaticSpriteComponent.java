package com.github.alexeybond.gdx_commons.game.systems.render.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.ioc.IoC;

/**
 *
 */
public class StaticSpriteComponent extends BaseRenderComponent {
    private final TextureRegion region;
    private final float scale;

    public StaticSpriteComponent(String passName, String regionName, float scale) {
        super(passName);
        this.scale = scale;

        region = IoC.resolve("get texture region", regionName);
    }

    @Override
    public void draw(DrawingContext context) {
        Vector2 pos = position.ref();
        float rot = rotation.get();

        Batch batch = context.state().beginBatch();

        Matrix4 matrix = batch.getTransformMatrix()
                .idt()
                .translate(pos.x, pos.y, 0)
                .rotate(0,0,1,rot)
                .scale(scale,scale,1);

        batch.setTransformMatrix(matrix);

        batch.draw(region, -0.5f * (float) region.getRegionWidth(), -0.5f * region.getRegionHeight());

        matrix.idt();

        batch.setTransformMatrix(matrix);
    }
}
