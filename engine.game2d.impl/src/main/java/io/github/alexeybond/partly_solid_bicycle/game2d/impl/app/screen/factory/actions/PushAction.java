package io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.factory.actions;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.Screen;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenEventAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class PushAction implements ScreenEventAction {
    @Nullable
    private final String afterLabel;

    @NotNull
    private final LogicNode factoryNode;

    @NotNull
    private final String newLabel;

    private PushAction(
            @Nullable String afterLabel,
            @NotNull LogicNode factoryNode,
            @NotNull String newLabel) {
        this.afterLabel = afterLabel;
        this.factoryNode = factoryNode;
        this.newLabel = newLabel;
    }

    @Override
    public void act(@NotNull ScreenContext context) {
        context.push(
                afterLabel == null ? context.getLabel() : afterLabel,
                newLabel,
                factoryNode.<GenericFactory<Screen, ScreenContext>>getComponent());
    }

    public static final IoCStrategy PARSE_STRATEGY = new IoCStrategy() {
        @Override
        public Object resolve(Object... args) throws StrategyException {
            InputDataObject data = (InputDataObject) args[0];
            LogicNode factoriesNode = (LogicNode) args[1];

            String factoryName = data.getField("screen").getString();
            String afterLabel, newLabel;

            try {
                afterLabel = data.getField("label").getString();
            } catch (UndefinedFieldException e) {
                afterLabel = null;
            }

            try {
                newLabel = data.getField("as").getString();
            } catch (UndefinedFieldException e) {
                newLabel = factoryName;
            }

            LogicNode factoryNode;

            try {
                factoryNode = factoriesNode.get(
                        factoriesNode.getTreeContext().getIdSet().get(factoryName)
                );
            } catch (NoSuchElementException e) {
                throw new IllegalArgumentException("No such screen factory: '" + factoryName + "'.");
            }

            return new PushAction(afterLabel, factoryNode, newLabel);
        }
    };
}
