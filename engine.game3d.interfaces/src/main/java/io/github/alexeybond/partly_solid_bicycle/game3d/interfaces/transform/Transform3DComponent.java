package io.github.alexeybond.partly_solid_bicycle.game3d.interfaces.transform;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Component;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.ObjectProperty;

public interface Transform3DComponent
        extends Component,
                ObjectProperty<Transform3D, Transform3DComponent> {
}
