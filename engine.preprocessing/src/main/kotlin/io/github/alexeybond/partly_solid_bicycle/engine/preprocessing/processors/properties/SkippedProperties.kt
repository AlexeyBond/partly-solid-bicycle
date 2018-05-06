package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.properties

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.SkipProperty
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.getAnnotationMirror
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
        val env = context.context.env
        val propertyInfo: PropertyInfo = context["info"]

        if (propertyInfo.getAnnotationMirror(env, SkipProperty::class) != null) {
            throw ProcessingInterruptException.INSTANCE
        }
    }
}
