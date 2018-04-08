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

public class ReplaceAction implements ScreenEventAction {
    @Nullable
    private final String replaceLabel;

    @NotNull
    private final LogicNode factoryNode;

    @NotNull
    private final String label;

    private ReplaceAction(
            @Nullable String replaceLabel,
            @NotNull LogicNode factoryNode,
            @NotNull String label) {
        this.replaceLabel = replaceLabel;
        this.factoryNode = factoryNode;
        this.label = label;
    }

    @Override
    public void act(@NotNull ScreenContext context) {
        context.replace(
                replaceLabel == null ? context.getLabel() : replaceLabel,
                label,
                factoryNode.<GenericFactory<Screen, ScreenContext>>getComponent());
    }

    public static final IoCStrategy PARSE_STRATEGY = new IoCStrategy() {
        @Override
        public Object resolve(Object... args) throws StrategyException {
            InputDataObject data = (InputDataObject) args[0];
            LogicNode factoriesNode = (LogicNode) args[1];

            String factoryName = data.getField("screen").getString();
            String replaceLabel, newLabel;

            try {
                replaceLabel = data.getField("label").getString();
            } catch (UndefinedFieldException e) {
                replaceLabel = null;
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

            return new ReplaceAction(replaceLabel, factoryNode, newLabel);
        }
    };
}
