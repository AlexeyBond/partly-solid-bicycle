package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.properties

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.properties.PropertyInfo
import javax.tools.Diagnostic

class UnprocessedProperties : ItemProcessor {
    override fun getPriority(): Int {
        return Int.MAX_VALUE
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component-property" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val propertyInfo: PropertyInfo = context["info"]

        if (null != context["processed"]) {
            return
        }

        propertyInfo.declaringElements.forEach { elem ->
            context.context.env.messager.printMessage(
                    Diagnostic.Kind.WARNING,
                    "Component property '${propertyInfo.name}' was not processed by any property processor",
                    elem
            )
        }
    }
}
