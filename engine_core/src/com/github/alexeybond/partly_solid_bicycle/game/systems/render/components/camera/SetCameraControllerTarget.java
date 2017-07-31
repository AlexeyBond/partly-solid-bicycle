package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera;

import com.badlogic.gdx.Gdx;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.state.EntityCameraState;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.CameraState;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TagGroup;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.partly_solid_bicycle.util.event.Event;
import com.github.alexeybond.partly_solid_bicycle.util.event.exception.NoSuchEventException;
import com.github.alexeybond.partly_solid_bicycle.util.event.helpers.Subscription;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.ObjectProperty;

/**
 *
 */
public class SetCameraControllerTarget implements Component {
    private final String eventName;
    private final String targetTag, cameraTag;
    private final float time;

    private Entity entity;
    private TagGroup targetTagGroup, cameraTagGroup;

    private final Subscription<Event> eventSubscription
            = new Subscription<Event>() {
        @Override
        public boolean onTriggered(Event event) {
            Entity camera = (cameraTagGroup == null) ? entity : cameraTagGroup.getOnly();
            Entity target = (targetTagGroup == null) ? entity : targetTagGroup.getOnly();

            CameraState targetState;

            try {
                targetState = target.events().<ObjectProperty<CameraState>>event("cameraState").get();
            } catch (NoSuchEventException e) {
                Gdx.app.log("DEBUG",
                        "Entity with tag '" + targetTag + "' has no camera target component but used as camera target.",
                        e);
                targetState = new EntityCameraState(target);
            }

            CameraController cameraController = camera.components().get("camera controller");

            cameraController.setTargetState(targetState, time);

            return true;
        }
    };

    public SetCameraControllerTarget(
            String eventName, String targetTag, String cameraTag, float time) {
        this.eventName = eventName;
        this.targetTag = targetTag;
        this.cameraTag = cameraTag;
        this.time = time;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        eventSubscription.set(entity.events().event(eventName, Event.makeEvent()));
        TaggingSystem taggingSystem = entity.game().systems().get("tagging");
        if (null != targetTag) targetTagGroup = taggingSystem.group(targetTag);
        if (null != cameraTag) cameraTagGroup = taggingSystem.group(cameraTag);
    }

    @Override
    public void onDisconnect(Entity entity) {
        eventSubscription.clear();
    }
}
