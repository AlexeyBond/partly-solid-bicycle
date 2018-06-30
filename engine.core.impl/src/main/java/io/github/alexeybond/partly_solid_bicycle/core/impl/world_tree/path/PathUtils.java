package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.path;

import org.jetbrains.annotations.NotNull;

public enum PathUtils {
    ;

    /**
     * Returns key object to be used for
     * {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet#get(Object) IdSet#get()}
     * call for given step text of path.
     *
     * @param step text representation of step
     * @return key object
     */
    @NotNull
    public static Object normalizeStep(@NotNull String step) {
        return step;
    }
}
