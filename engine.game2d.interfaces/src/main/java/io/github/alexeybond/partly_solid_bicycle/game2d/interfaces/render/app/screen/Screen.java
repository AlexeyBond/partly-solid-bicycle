package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget;
import org.jetbrains.annotations.NotNull;

/**
 * Screen is a state of interactive application.
 * <p>
 * In comparison to {@link ApplicationState application states} screens provide more convenient and safe
 * way to manage state changes typical for interactive applications.
 * <p>
 * All exist screens form a stack.
 * The screen placed on the top of stack is called active.
 * The active screen determines current behavior of interactive application receiving
 * {@link #render(RenderTarget)} call on every frame.
 * Current screen can also modify screens stack using operations provided by
 * {@link ScreenContext screen context}.
 * <p>
 * All screens have their own {@link LogicNode nodes} called screen roots and available through
 * {@link ScreenContext screen context}.
 * All screen roots are stored as children of a single node.
 * Identifier of screen root within it's parent is called screen label.
 * Screen label represented as string is also available through {@link ScreenContext screen context}.
 * There may be only one screen exist with the same label at the same moment of time.
 */
public interface Screen {
    /**
     * @param context  {@link ScreenContext instance for this screen}
     * @param runState the state representing state of the application with this screen being active
     * @return the state activation of which causes activation of {@code runState} when screen is ready
     * to be current screen
     */
    @NotNull
    ApplicationState create(@NotNull ScreenContext context, @NotNull ApplicationState runState);

    void dispose();

    void pause();

    void resume();

    void render(@NotNull RenderTarget target);
}
