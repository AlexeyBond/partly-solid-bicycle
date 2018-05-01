package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.module

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor

class DefaultGeneratedModuleDependencies : ItemProcessor {
    override fun getPriority(): Int {
        return -7_000
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "module" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val dependencies: MutableList<String> = context["dependencies:direct"] ?: ArrayList()

        dependencies.add("declarative_node_factory_strategies")

        context["dependencies:direct"] = dependencies

        return false
    }
}
