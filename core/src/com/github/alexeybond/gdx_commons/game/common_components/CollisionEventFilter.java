package com.github.alexeybond.gdx_commons.game.common_components;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.gdx_commons.game.systems.tagging.TagGroup;
import com.github.alexeybond.gdx_commons.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.helpers.Subscription;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;

/**
 * Component that triggers some event when occurs collision with another entity that does (not) have a specified
 * tag.
 */
public class CollisionEventFilter implements Component {
    private final String rawEventName, filteredEventName;
    private final String filterTag;
    private final boolean invert;

    private final Subscription<Component, ObjectProperty<CollisionData, Component>> eventSub
            = new Subscription<Component, ObjectProperty<CollisionData, Component>>() {
        @Override
        public boolean onTriggered(Component component, ObjectProperty<CollisionData, Component> event) {
            if (filterTagGroup.contains(event.get().that.entity()) != invert) {
                return filteredEvent.trigger(CollisionEventFilter.this);
            }

            return false;
        }
    };

    private Event<Component> filteredEvent;
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
        eventSub.set(entity.events().event(rawEventName, ObjectProperty.<CollisionData, Component>make()));

        filteredEvent = entity.events().event(filteredEventName, Event.<Component>make());
        filterTagGroup = entity.game().systems().<TaggingSystem>get("tagging").group(filterTag);
    }

    @Override
    public void onDisconnect(Entity entity) {
        eventSub.clear();
    }
}
