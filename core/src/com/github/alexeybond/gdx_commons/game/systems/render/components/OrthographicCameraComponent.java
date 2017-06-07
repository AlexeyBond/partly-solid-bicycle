package com.github.alexeybond.gdx_commons.game.systems.render.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.drawing.RenderTarget;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

/**
 *
 */
public class OrthographicCameraComponent extends BaseRenderComponent {
    private final String globalAlias;
    private final OrthographicCamera camera;

    private FloatProperty<Component> zoomProp;
    private Vec2Property<Component> axisScaleProp;
    private ObjectProperty<Camera, GameSystem> aliasProp;

    public OrthographicCameraComponent(String passName, String globalAlias) {
        super(passName);
        this.globalAlias = globalAlias;

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

        aliasProp = entity.game().events()
                .event(globalAlias, ObjectProperty.<Camera, GameSystem>make());
        aliasProp.set(system, camera);
    }

    @Override
    public void onDisconnect(Entity entity) {
        super.onDisconnect(entity);

        if (aliasProp.get() == this.camera) {
            aliasProp.set(system, null);
        }
    }
}
