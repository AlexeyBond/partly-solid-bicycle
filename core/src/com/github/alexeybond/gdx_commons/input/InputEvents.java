package com.github.alexeybond.gdx_commons.input;

import com.badlogic.gdx.InputProcessor;
import com.github.alexeybond.gdx_commons.util.event.EventsOwner;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;

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
