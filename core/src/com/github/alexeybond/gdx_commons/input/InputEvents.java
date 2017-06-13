package com.github.alexeybond.gdx_commons.input;

import com.badlogic.gdx.InputProcessor;
import com.github.alexeybond.gdx_commons.util.event.EventsOwner;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;

/**
 *
 */
public interface InputEvents extends EventsOwner<InputEvents> {
    InputProcessor inputProcessor();

    BooleanProperty<InputEvents> keyEvent(int code);

    BooleanProperty<InputEvents> keyEvent(String name);

    void enable();

    void disable();

    void addSlaveProcessor(InputProcessor processor, boolean front);

    void removeSlaveProcessor(InputProcessor processor);

    InputEvents makeChild();
}
