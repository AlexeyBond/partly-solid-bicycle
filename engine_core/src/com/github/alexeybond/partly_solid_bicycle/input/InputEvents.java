package com.github.alexeybond.partly_solid_bicycle.input;

import com.badlogic.gdx.InputProcessor;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventsOwner;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.BooleanProperty;

/**
 *
 */
public interface InputEvents extends EventsOwner {
    InputProcessor inputProcessor();

    BooleanProperty keyEvent(int code);

    BooleanProperty keyEvent(String name);

    void enable();

    void disable();

    void addSlaveProcessor(InputProcessor processor, boolean front);

    void removeSlaveProcessor(InputProcessor processor);

    InputEvents makeChild();
}
