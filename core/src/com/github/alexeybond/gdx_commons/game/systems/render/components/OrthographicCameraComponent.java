package com.github.alexeybond.gdx_commons.game.systems.render.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.drawing.RenderTarget;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.game.event.props.Vec2Property;

/**
 *
 */
public class OrthographicCameraComponent extends BaseRenderComponent {
    private OrthographicCamera camera;

    private FloatProperty<Component> zoomProp;
    private Vec2Property<Component> axisScaleProp;

    public OrthographicCameraComponent(String passName) {
        super(passName);

        camera = new OrthographicCamera();
    }

    public OrthographicCamera camera() {
        return camera;
    }

    @Override
    public void draw(DrawingContext context) {
        float zoomInv = 1f / zoomProp.get();
        Vector2 axisScale = axisScaleProp.ref();
        RenderTarget target = context.getCurrentRenderTarget();

        camera.viewportWidth = target.width() * zoomInv * axisScale.x;
        camera.viewportHeight = target.height() * zoomInv * axisScale.y;

        Vector2 pos = position.ref();
        camera.position.set(pos.x, pos.y, 0);

        camera.up.set(0,1,0);
        camera.up.rotate(camera.direction, -rotation.get());

        camera.update();

        context.state().setProjection(camera.combined);
    }

    @Override
    public void onConnect(Entity entity) {
        super.onConnect(entity);

        // Properties "zoom" and "axisScale" allow other components to control camera zoom
        zoomProp = entity.events().event("zoom", FloatProperty.<Component>make(1));
        axisScaleProp = entity.events().event("axisScale", Vec2Property.<Component>make(1,1));
    }

    @Override
    public void onDisconnect(Entity entity) {
        super.onDisconnect(entity);
    }
}