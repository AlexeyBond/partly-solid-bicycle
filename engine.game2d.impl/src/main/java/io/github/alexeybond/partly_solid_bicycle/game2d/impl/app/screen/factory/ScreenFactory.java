package io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.factory;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.NodeFactories;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.adapter.DataObjectVisitorAdapter;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeAttachmentListener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules.ScreenFactoryModule;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.Screen;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenEventAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Application-level component that serves as a factory creating instances of {@link Screen}.
 * <p>
 * <pre>
 * "pauseScreen": {
 *     "class": "defaultScreen",
 *     "screen": {
 *         "class": "myScreen",
 *         // ... screen attributes ... //
 *     },
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
    private Map<String, ScreenEventAction> eventReactions = new HashMap<String, ScreenEventAction>();

    public InputDataObject events;

    public InputDataObject screen;

    @Override
    public void onAttached(@NotNull LogicNode node) {
        final LogicNode parent = node.getParent();
        final IoCStrategy actionsStrategy = IoC.resolveStrategy("screen event action");

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
                ScreenEventAction action = (ScreenEventAction) actionsStrategy.resolve(
                        value.getField("action").getString(),
                        value,
                        parent
                );

                eventReactions.put(field, action);
            }
        });
    }

    @Override
    public void onDetached(@NotNull LogicNode node) {

    }

    @NotNull
    @Override
    public Screen create(@Nullable ScreenContext context) {
        if (null == context) throw new NullPointerException("context");

        NodeFactory<InputDataObject> screenNodeFactory = IoC.resolve(
                "node factory for node kind", "screen");

        LogicNode screenRoot = context.getScreenRoot();
        LogicNode screenNode = screenRoot.getOrAdd(
                screenRoot.getTreeContext().getIdSet().get("screen"),
                screenNodeFactory, screen);

        initTree(context);

        return screenNode.getComponent();
    }

    private void initTree(@NotNull ScreenContext context) {
        LogicNode screenRoot = context.getScreenRoot();
        IdSet<LogicNode> idSet = screenRoot.getTreeContext().getIdSet();

        LogicNode eventsNode = screenRoot.getOrAdd(
                idSet.get("events"),
                NodeFactories.EMPTY_GROUP,
                null
        );

        for (Map.Entry<String, ScreenEventAction> entry : eventReactions.entrySet()) {
            eventsNode.getOrAdd(
                    idSet.get(entry.getKey()),
                    NodeFactories.SIMPLE_COMPONENT,
                    new ScreenEventActionEventListener(entry.getValue(), context)
            );
        }
    }
}
