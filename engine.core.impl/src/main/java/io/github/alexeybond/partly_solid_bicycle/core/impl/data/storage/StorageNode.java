package io.github.alexeybond.partly_solid_bicycle.core.impl.data.storage;

import io.github.alexeybond.partly_solid_bicycle.core.impl.data.NullInputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.impl.event.variable.DefaultObjectVariable;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver.ConstantArgumentChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.GroupNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.DeclarativePopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.ObjectVariable;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

/**
 * A key-value storage.
 * <p>
 * This class is a bridge between {@link InputDataObject data objects} and
 * {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode nodes}.
 * </p>
 * <p>
 * Primary use-case for this class is a settings storage.
 * For example settings of some 2d platformer game could look like this:
 * </p>
 * <pre>
 * {
 *     "input.keyboard.move_forward": ["K_D", "K_RIGHT"],
 *     "input.keyboard.move_back": ["K_A", "K_LEFT"],
 *     "input.keyboard.move_jump": ["K_W", "K_UP"],
 *     // ... //
 * }
 * </pre>
 * <p>
 * And the following nodes will be created at runtime (assuming {@link StorageNode} instance has path
 * {@code /settings}):
 * </p>
 * <pre>{@code
 * /settings <- StateTopic<InputDataObject>
 * /settings/input.keyboard.move_forward <- ObjectVariable<InputDataObject> = ["K_D", "K_RIGHT"]
 * /settings/input.keyboard.move_back <- ObjectVariable<InputDataObject> = ["K_A", "K_LEFT"]
 * /settings/input.keyboard.move_jump <- ObjectVariable<InputDataObject> = ["K_W", "K_UP"]
 * /settings/...
 * /settings/* <- ObjectVariable<InputDataObject> = null // will be created lazily for any key
 * }</pre>
 * <p>
 * The {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic topic} attached
 * to the storage node itself notifies listeners when change is made to any child node.
 * </p>
 * <p>
 * {@link StorageNode} is just a building block for persistent storage.
 * {@link StorageNode} does not define how to load and store the data.
 * Instead the data should be loaded before creation of {@link StorageNode} and stored on update
 * (probably by a listener of the topic attached to {@link StorageNode}).
 * </p>
 * <p>
 * <b>Note: fields of nested objects in source document are not mapped to nodes</b>
 * </p>
 * <p>
 * i.e. document like
 * <pre>
 * {
 *     "input": {
 *         "move_forward": ["K_D", "K_LEFT"],
 *         // ... //
 *     }
 * }
 * </pre>
 * will produce
 * <pre>{@code
 * /settings <- Topic<InputDataObject>
 * /settings/input <- ObjectVariable<InputDataObject> = { "move_forward": ["K_D", "K_LEFT"], ... }
 * // and no more deeper nodes! //
 * // ... //
 * }</pre>
 * </p>
 */
public class StorageNode extends GroupNode {
    @NotNull
    private ObjectVariable<InputDataObject> variable;

    public StorageNode(@NotNull final InputDataObject initialValue) {
        this(initialValue, new DefaultObjectVariable<InputDataObject>(0));
    }

    private StorageNode(
            @NotNull final InputDataObject initialValue,
            @NotNull final ObjectVariable<InputDataObject> variable) {
        this(
                initialValue, variable,
                new InitialChildFactory(new Listener<ObjectVariable<InputDataObject>>() {
                    @Override
                    public void receive(
                            @NotNull ObjectVariable<InputDataObject> variable,
                            @NotNull Topic<? extends ObjectVariable<InputDataObject>> topic) {
                        variable.set(variable.get());
                    }
                })
        );
    }

    private StorageNode(
            @NotNull final InputDataObject initialValue,
            @NotNull final ObjectVariable<InputDataObject> variable,
            @NotNull final NodeFactory<InputDataObject> childFactory) {
        super(
                new ConstantArgumentChildResolver<InputDataObject>(
                        childFactory, NullInputDataObject.INSTANCE),
                new DeclarativePopulator(childFactory, initialValue)
        );

        this.variable = variable;
        this.variable.set(new DataStorageView(this));
    }

    @NotNull
    @Override
    @SuppressWarnings({"unchecked"})
    public <T> T getComponent() throws NoSuchElementException {
        return (T) variable;
    }
}
