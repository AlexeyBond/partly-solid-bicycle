package com.github.alexeybond.gdx_commons.drawing.sprite.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.drawing.sprite.SpriteInstance;
import com.github.alexeybond.gdx_commons.drawing.sprite.SpriteTemplate;

/**
 *
 * TODO:: Optimize
 */
public class AccurateSpriteInstance implements SpriteInstance {
    @Override
    public void draw(Batch batch, SpriteTemplate template, Vector2 position, float scale, float rotation) {
        scale *= template.scale();

        Matrix4 matrix = batch.getTransformMatrix()
                .idt()
                .translate(position.x, position.y, 0)
                .rotate(0,0,1,rotation + template.rotation())
                .scale(scale,scale,1);

        batch.setTransformMatrix(matrix);

        batch.draw(
                template.region(),
                -template.originX(),
                -template.originY());

        matrix.idt();

        batch.setTransformMatrix(matrix);
    }
}
