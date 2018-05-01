package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor;

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext;
import org.jetbrains.annotations.NotNull;

public interface ItemProcessor {
    int getPriority();

    boolean acceptsItemKind(@NotNull String itemKind);

    /**
     * @param context item context
     * @return {@code true} iff item processing should be stopped
     */
    boolean processItem(@NotNull ItemContext context);
}
