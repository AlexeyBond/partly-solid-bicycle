package io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen;

import io.github.alexeybond.partly_solid_bicycle.core.impl.data.NullInputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.adapter.DataObjectVisitorAdapter;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeAttachmentListener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.TreeContext;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional;
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules.ScreenFactoryModule;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.Screen;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Application-level component that serves as a factory creating instances of {@link Screen}.
 * <p>
 * <pre>
 * "pauseScreen": {
 *     "class": "defaultScreen",
 *     "events": {
 *         "resume": {
 *             "action": "pop",
 *             "toLabel": "game"
 *         },
 *         "leaveGame": {
 *             "action": "pop",
 *             "toLabel": "mainMenu"
 *         },
 *         "exitGame": {
 *             "action": "terminate"
 *         }
 *     }
 * }
 * </pre>
 */
@Component(
        name = "defaultScreen",
        kind = "applicationComponent",
        modules = {ScreenFactoryModule.class})
public class ScreenFactory implements NodeAttachmentListener, GenericFactory<Screen, ScreenContext> {
    private Map<String, ScreenEventAction> eventReactions;

    public InputDataObject events;

    @Optional
    public String screen = "default screen";

    @Optional
    public InputDataObject screenConfig = NullInputDataObject.INSTANCE;

    @Override
    public void onAttached(@NotNull LogicNode node) {
        LogicNode parent = node.getParent();
        TreeContext treeContext = parent.getTreeContext();

        events.accept(new DataObjectVisitorAdapter() {
            @Override
            protected void visitAnyValue(Object value) {
                throw new IllegalArgumentException(
                        "'events' property of screen factory is expected to be a object but is an atomic value: " + value);
            }

            @Override
            public void beginVisitArray() {
                throw new IllegalArgumentException(
                        "'events' property of screen factory is expected to be a object but is an array");
            }

            @Override
            public void visitField(String field, InputDataObject value) {
                // TODO:: Prepare event reaction for name from `field`
            }
        });
    }

    @Override
    public void onDetached(@NotNull LogicNode node) {

    }

    @NotNull
    @Override
    public Screen create(@Nullable ScreenContext context) {
        GenericFactory<Screen, ScreenContext> factory = IoC.resolve(screen, screenConfig);
        // TODO:: Create a screen with prepared event reactions
        return factory.create(context);
    }
}
