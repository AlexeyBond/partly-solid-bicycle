package com.github.alexeybond.partly_solid_bicycle.util.event.props;

import com.github.alexeybond.partly_solid_bicycle.util.event.Event;

/**
 * Property is an event source holding a value. The event happens when the value is changed.
 *
 * Property has methods to load/store to string array. Those methods are used to load and save
 * declarative descriptions of entities.
 */
public abstract class Property extends Event {
    protected Property() {
        // By-default do not create listeners array. Array will be created lazily
        super(0);
    }

    /**
     * Create string array representing value of this property.
     *
     * @return string array or {@code null} if property value is not serializable
     */
    public abstract String[] dump();

    /**
     * Set value from saved state.
     */
    public abstract void load(String[] value);
}
