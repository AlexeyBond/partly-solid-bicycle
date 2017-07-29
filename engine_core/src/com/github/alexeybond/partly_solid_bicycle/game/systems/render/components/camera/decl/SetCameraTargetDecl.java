package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.decl;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.SetCameraControllerTarget;

/**
 *
 */
public class SetCameraTargetDecl implements ComponentDeclaration {
    /** Name of event on which to act. */
    public String event;

    /** Name of target entity tag, if null component's owner entity will be used as target */
    public String targetTag = null;

    /** Name of camera entity (containing "camera controller" component), if null component's owner entity
     * is assumed to be a camera.
     */
    public String cameraTag = null;

    /** Time in seconds to move between current target and new target. */
    public float time = 1f;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new SetCameraControllerTarget(event, targetTag, cameraTag, time);
    }
}
