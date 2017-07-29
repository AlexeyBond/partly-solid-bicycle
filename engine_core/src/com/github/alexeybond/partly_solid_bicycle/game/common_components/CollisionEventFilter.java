package com.github.alexeybond.partly_solid_bicycle.game.common_components;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TagGroup;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.partly_solid_bicycle.util.event.Event;
import com.github.alexeybond.partly_solid_bicycle.util.event.helpers.Subscription;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.ObjectProperty;

/**
 * Component that triggers some event when occurs collision with another entity that does (not) have a specified
 * tag.
 */
public class CollisionEventFilter implements Component {
    private final String rawEventName, filteredEventName;
    private final String filterTag;
    private final boolean invert;

    private final Subscription<ObjectProperty<CollisionData>> eventSub
            = new Subscription<ObjectProperty<CollisionData>>() {
        @Override
        public boolean onTriggered(ObjectProperty<CollisionData> event) {
            if (filterTagGroup.contains(event.get().that.entity()) != invert) {
                return filteredEvent.trigger();
            }

            return false;
        }
    };

    private Event filteredEvent;
    private TagGroup filterTagGroup;

    public CollisionEventFilter(
            String rawEventName, String filteredEventName, String filterTag, boolean invert) {
        this.rawEventName = rawEventName;
        this.filteredEventName = filteredEventName;
        this.filterTag = filterTag;
        this.invert = invert;
    }

    @Override
    public void onConnect(Entity entity) {
        eventSub.set(entity.events().event(rawEventName, ObjectProperty.<CollisionData>make()));

        filteredEvent = entity.events().event(filteredEventName, Event.makeEvent());
        filterTagGroup = entity.game().systems().<TaggingSystem>get("tagging").group(filterTag);
    }

    @Override
    public void onDisconnect(Entity entity) {
        eventSub.clear();
    }
}
