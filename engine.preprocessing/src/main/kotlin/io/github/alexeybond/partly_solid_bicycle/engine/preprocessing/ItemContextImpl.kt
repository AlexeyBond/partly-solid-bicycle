package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext

class ItemContextImpl(private val processingContext: ProcessingContext) : ItemContext {
    private val map = HashMap<String, Any>()

    override fun <T : Any?> get(id: String): T {
        return map[id] as T;
    }

    override fun set(id: String, value: Any) {
        map[id] = value;
    }

    override fun getContext(): ProcessingContext {
        return processingContext
    }
}
