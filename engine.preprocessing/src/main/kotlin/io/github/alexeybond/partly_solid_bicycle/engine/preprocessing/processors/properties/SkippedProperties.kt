package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.properties

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.exceptions.ProcessingInterruptException
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.properties.PropertyInfo

class SkippedProperties : ItemProcessor {
    override fun getPriority(): Int {
        return Int.MIN_VALUE
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component-property" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val propertyInfo: PropertyInfo = context["info"]

        if (propertyInfo.metadata["property.isSkipped"] == "true") {
            throw ProcessingInterruptException.INSTANCE
        }
    }
}
