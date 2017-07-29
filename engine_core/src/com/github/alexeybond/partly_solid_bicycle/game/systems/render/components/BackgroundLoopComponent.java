package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.github.alexeybond.partly_solid_bicycle.drawing.DrawingContext;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.FloatProperty;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.RenderComponent;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.RenderSystem;

/**
 *
 */
public abstract class BackgroundLoopComponent implements RenderComponent {
    private final String passName;
    private Texture texture;
    private final float[] vertexArray;
    private final Vector3 tmp1 = new Vector3(), tmp2 = new Vector3();

    private float invWidth, invHeight;

    private FloatProperty imageScaleProp;
    private FloatProperty positionScaleProp;

    public BackgroundLoopComponent(String passName, Texture texture) {
        this.passName = passName;
        setTexture(texture);

        float color = Color.toFloatBits(255, 255, 255, 255);

        vertexArray = new float[] {
                // x, y, c, u, v
                0, 0, color, 0, 0,
                1, 0, color, 0, 0,
                1, 1, color, 0, 0,
                0, 1, color, 0, 0,
        };
    }

    private void setTexture(Texture texture) {
        invWidth = 1f / (float) texture.getWidth();
        invHeight = 1f / (float) texture.getHeight();
        this.texture = texture;
    }

    @Override
    public void onConnect(Entity entity) {
        RenderSystem system = entity.game().systems().get("render");
        system.addToPass(passName, this);

        imageScaleProp = entity.events().event(
                "backgroundImageScale", FloatProperty.make(1f));
        positionScaleProp = entity.events().event(
                "backgroundPositionScale", FloatProperty.make(1f));
    }

    @Override
    public void onDisconnect(Entity entity) {
        RenderSystem system = entity.game().systems().get("render");
        system.removeFromPass(passName, this);
    }

    @Override
    public void draw(DrawingContext context) {
        Batch batch = context.state().beginBatch();

        float positionScale = positionScaleProp.get();
        float imageScaleInv = 1f / imageScaleProp.get();

        Camera camera = getCamera();

        tmp2.set(camera.position).scl(positionScale - 1);

        int off = 3;
        tmp1.set(-1,-1,0)
                .prj(camera.invProjectionView).add(tmp2);
        vertexArray[off++] = tmp1.x * invWidth * imageScaleInv;
        vertexArray[off++] = tmp1.y * invHeight * imageScaleInv;
        off += 3;
        tmp1.set(1,-1,0)
                .prj(camera.invProjectionView).add(tmp2);
        vertexArray[off++] = tmp1.x * invWidth * imageScaleInv;
        vertexArray[off++] = tmp1.y * invHeight * imageScaleInv;
        off += 3;
        tmp1.set(1,1,0)
                .prj(camera.invProjectionView).add(tmp2);
        vertexArray[off++] = tmp1.x * invWidth * imageScaleInv;
        vertexArray[off++] = tmp1.y * invHeight * imageScaleInv;
        off += 3;
        tmp1.set(-1,1,0)
                .prj(camera.invProjectionView).add(tmp2);
        vertexArray[off++] = tmp1.x * invWidth * imageScaleInv;
        vertexArray[off++] = tmp1.y * invHeight * imageScaleInv;

        batch.draw(texture, vertexArray, 0, vertexArray.length);
    }

    protected abstract Camera getCamera();

    @Deprecated
    public static BackgroundLoopComponent withCamera(String passName, Texture texture, final Camera camera) {
        return new BackgroundLoopComponent(passName, texture) {
            @Override
            protected Camera getCamera() {
                return camera;
            }
        };
    }
}
