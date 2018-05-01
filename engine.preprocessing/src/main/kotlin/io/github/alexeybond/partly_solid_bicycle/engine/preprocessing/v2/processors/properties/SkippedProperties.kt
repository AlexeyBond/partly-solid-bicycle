package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.properties

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.TypeProperty
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.SkipProperty
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import javax.lang.model.element.TypeElement

class SkippedProperties : ItemProcessor {
    private val ANNOTATION_CN = SkipProperty::class.java.canonicalName

    override fun getPriority(): Int {
        return Int.MIN_VALUE
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component-property" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val propertyInfo: TypeProperty = context["info"]

        if (propertyInfo.annotations.any {
                    (it.annotationType.asElement() as TypeElement)
                            .qualifiedName.toString() == ANNOTATION_CN
                }) {
            return true
        }

        return false
    }
}
