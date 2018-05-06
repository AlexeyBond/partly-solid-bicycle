package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor;

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.exceptions.ProcessingInterruptException;
import org.jetbrains.annotations.NotNull;

public interface ItemProcessor {
    int getPriority();

    boolean acceptsItemKind(@NotNull String itemKind);

    /**
     * @param context item context
     * @throws ProcessingInterruptException if processing of the item should be stopped
     */
    void processItem(@NotNull ItemContext context) throws ProcessingInterruptException;
}
