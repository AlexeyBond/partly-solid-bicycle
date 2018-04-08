package io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.manager;

import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.PrevStateDisposeStateWrapper;
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.QueueState;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.NodeFactories;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.Screen;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class DefaultScreenContext implements ScreenContext {
    @NotNull
    private final ScreenContext prevContext;

    /**
     * {@link ApplicationState State} corresponding to the previous screen.
     */
    @NotNull
    private final ApplicationState prevState;

    /**
     * Label ot the current screen.
     */
    @NotNull
    private final String label;

    @NotNull
    private final QueueState state;

    @NotNull
    private final LogicNode rootNode;

    @NotNull
    private final RenderTarget target;

    private DefaultScreenContext(
            @NotNull GenericFactory<Screen, ScreenContext> factory,
            @NotNull ScreenContext prevContext,
            @NotNull ApplicationState prevState,
            @NotNull String label,
            @NotNull LogicNode rootNode, @NotNull RenderTarget target) {
        this.prevContext = prevContext;
        this.prevState = prevState;
        this.label = label;
        this.rootNode = rootNode;
        this.target = target;

        Screen screen = factory.create(this);

        state = new QueueState(
                screen.create(this, new RunningScreenState(screen, target, rootNode))
        );
    }

    @NotNull
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void push(@NotNull String label, @NotNull GenericFactory<Screen, ScreenContext> factory) {
        if (label.equals(this.label)) {
            state.enqueue(
                    constructNewScreen(
                            label,
                            factory,
                            state,
                            this,
                            rootNode,
                            target)
            );
        } else {
            prevContext.push(label, factory);
            enqueueDispose(prevState);
        }
    }

    @Override
    public void replace(@NotNull String label, @NotNull GenericFactory<Screen, ScreenContext> factory) {
        ApplicationState next = prevState;

        if (label.equals(this.label)) {
            next = constructNewScreen(
                    label,
                    factory,
                    prevState,
                    prevContext,
                    rootNode,
                    target
            );
        } else {
            prevContext.replace(label, factory);
        }

        enqueueDispose(next);
    }

    @Override
    public void pop() {
        enqueueDispose(prevState);
    }

    @Override
    public void pop(@NotNull String toLabel) {
        if (!toLabel.equals(this.label)) {
            prevContext.pop(toLabel);
            // If previous line throws then this screen will not be disposed.
            enqueueDispose(prevState);
        }
    }

    @NotNull
    @Override
    public LogicNode getScreenRoot() {
        return rootNode;
    }

    private void enqueueDispose(@NotNull ApplicationState nextActive) {
        state.enqueue(
                new PrevStateDisposeStateWrapper(nextActive, state)
        );
    }

    /**
     * @param label       label of new screen
     * @param factory     factory that will create the new screen
     * @param prevState   the state that will become active after created screen is disposed
     * @param prevContext context of previous screen
     * @param screenRoot  root node of some screen in the same screen collection
     * @param target      render target the screen should be rendered to
     * @return application state that will activate the created screen
     */
    @NotNull
    private static ApplicationState constructNewScreen(
            @NotNull String label,
            @NotNull GenericFactory<Screen, ScreenContext> factory,
            @NotNull ApplicationState prevState,
            @NotNull ScreenContext prevContext,
            @NotNull LogicNode screenRoot,
            @NotNull RenderTarget target
    ) {
        LogicNode screensRoot = screenRoot.getParent();
        Id<LogicNode> newId = screensRoot.getTreeContext().getIdSet().get(label);

        try {
            screensRoot.get(newId);
            throw new IllegalArgumentException("Screen with label '" + label + "' already exists.");
        } catch (NoSuchElementException e) {
            // There is no screen node with id=label, that's what we want
        }

        LogicNode newNode = screensRoot.getOrAdd(newId, NodeFactories.EMPTY_GROUP, null);

        try {
            DefaultScreenContext newContext = new DefaultScreenContext(
                    factory,
                    prevContext,
                    prevState,
                    label,
                    newNode,
                    target);

            return newContext.state;
        } catch (RuntimeException e) {
            screensRoot.remove(newId);
            throw e;
        }
    }

    public static ApplicationState init(
            @NotNull String initialLabel,
            @NotNull GenericFactory<Screen, ScreenContext> factory,
            @NotNull LogicNode screensRoot,
            @NotNull RenderTarget target,
            @NotNull ApplicationState terminalState
    ) {
        IdSet<LogicNode> idSet = screensRoot.getTreeContext().getIdSet();
        ScreenContext nullContext = new NullScreenContext();

        Id<LogicNode> nullId = idSet.get(nullContext.getLabel());

        LogicNode nullContextNode = screensRoot.getOrAdd(nullId, NodeFactories.NULL, null);

        return constructNewScreen(
                initialLabel,
                factory,
                terminalState,
                nullContext,
                nullContextNode,
                target
        );
    }
}
