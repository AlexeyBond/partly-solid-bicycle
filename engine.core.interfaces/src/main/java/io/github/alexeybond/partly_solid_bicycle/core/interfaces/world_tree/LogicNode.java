package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.visitor.Visitable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

/**
 * A node of a logical tree.
 * <p>
 * A node may be just a container for a set of child nodes and/or have a attached component.
 * </p>
 * <p>
 * A node may represent an entity, collection of entities or subset of entity components.
 * Actually there is no special "entity" class.
 * </p>
 */
public interface LogicNode extends Visitable<NodeVisitor> {
    /**
     * Get a child node with given identifier.
     *
     * @param id child identifier
     * @return child node
     * @throws NoSuchElementException if there is no child node with given identifier
     */
    @NotNull
    LogicNode get(@NotNull Id<LogicNode> id) throws NoSuchElementException;

    /**
     * Get exist node or create a new one using given factory.
     *
     * @param id      child id
     * @param factory the factory
     * @param arg     the argument to pass to factory
     * @param <A>     factory argument type
     * @return previously exist or just created child node with given identifier
     */
    @NotNull
    <A> LogicNode getOrAdd(@NotNull Id<LogicNode> id, @NotNull NodeFactory<A> factory, @Nullable A arg)
            throws RuntimeException, UnsupportedOperationException;

    /**
     * Populate this node with child nodes provided by given populator.
     */
    void populate(@NotNull NodePopulator populator);

    /**
     * Remove child with given id.
     *
     * @param id child id
     * @throws IllegalStateException         if the child cannot be removed in current state of this node
     */
    void remove(@NotNull Id<LogicNode> id) throws IllegalStateException;

    /**
     * Remove given child.
     * <p>
     * This method may be much slower than {@link #remove(Id)}. Use it carefully.
     * </p>
     *
     * @param child the child to remove
     * @throws IllegalStateException         if the child cannot be removed in current state of this node
     * @throws IllegalArgumentException      if given node is not a child of this node
     */
    void remove(@NotNull LogicNode child)
            throws IllegalStateException, IllegalArgumentException;

    /**
     * @return the parent node of this node
     * @throws IllegalStateException if this node is not connected
     */
    @NotNull
    LogicNode getParent();

    /**
     * @param <T> component type
     * @return a component attached to this node
     * @throws NoSuchElementException if there is no component attached to this node
     */
    @NotNull
    <T> T getComponent() throws NoSuchElementException;

    /**
     * @return context of a tree this node belongs to
     * @throws IllegalStateException if this node is not connected
     */
    @NotNull
    TreeContext getTreeContext();
}
