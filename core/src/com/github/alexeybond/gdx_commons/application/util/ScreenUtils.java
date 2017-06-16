package com.github.alexeybond.gdx_commons.application.util;

import com.github.alexeybond.gdx_commons.application.Screen;
import com.github.alexeybond.gdx_commons.input.InputEvents;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;

import java.util.regex.Pattern;

public enum ScreenUtils {;
    public static final Pattern DEBUG_PASS_NAME_PATTERN = Pattern.compile(".*debug.*");

    /**
     * Enables/disables debug for screen drawing when [`] key is pressed.
     *
     * When debug is enabled all passes with names containing {@code "debug"} are enabled
     * and {@code "debugEnabled"} property of screen input is set to {@code true}.
     */
    public static void enableToggleDebug(final Screen screen) {
        final BooleanProperty<InputEvents> debugEnabledProp
                = screen.input().events().event("debugEnabled", BooleanProperty.<InputEvents>make(false));

        screen.input().keyEvent("`").subscribe(new EventListener<InputEvents, BooleanProperty<InputEvents>>() {
            boolean debug = false;

            private void set() {
                screen.scene().enableMatching(DEBUG_PASS_NAME_PATTERN, debug);
                debugEnabledProp.set(screen.input(), debug);
            }

            {set();}

            @Override
            public boolean onTriggered(InputEvents inputEvents, BooleanProperty<InputEvents> event) {
                if (event.get()) return false;
                debug = !debug;
                set();
                return true;
            }
        });
    }
}
