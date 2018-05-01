package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.module

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.GeneratedModule
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor

class ConfiguredGeneratedModuleDependencies : ItemProcessor {
    override fun getPriority(): Int {
        return -7_000
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "module" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val providedDependencies: ArrayList<String> = context["dependencies:provided"] ?: ArrayList()
        val dependencies: MutableList<String> = context["dependencies:direct"] ?: ArrayList()
        val reverseDependencies: ArrayList<String> = context["dependencies:reverse"] ?: ArrayList()
        val annotation: GeneratedModule = context["annotation"]

        providedDependencies.addAll(annotation.provided)
        dependencies.addAll(annotation.depends)
        reverseDependencies.addAll(annotation.reverseDepends)

        context["dependencies:provided"] = providedDependencies
        context["dependencies:direct"] = dependencies
        context["dependencies:reverse"] = reverseDependencies

        return false
    }
}
