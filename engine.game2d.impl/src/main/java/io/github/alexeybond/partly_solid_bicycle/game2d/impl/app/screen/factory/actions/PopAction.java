package io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.factory.actions;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyException;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenEventAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PopAction implements ScreenEventAction {
    @Nullable
    private final String label;

    private PopAction(@Nullable String label) {
        this.label = label;
    }

    @Override
    public void act(@NotNull ScreenContext context) {
        if (label == null) {
            context.pop();
        } else {
            context.pop(label);
        }
    }

    public static final IoCStrategy PARSE_STRATEGY = new IoCStrategy() {
        @Override
        public Object resolve(Object... args) throws StrategyException {
            InputDataObject data = (InputDataObject) args[0];

            String label;

            try {
                label = data.getField("to").getString();
            } catch (UndefinedFieldException e) {
                label = null;
            }

            return new PopAction(label);
        }
    };
}
