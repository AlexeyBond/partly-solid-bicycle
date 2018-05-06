package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.loader

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.properties.PropertyInfo
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations.RootOperationGenerator

class PropertyLoad : ItemProcessor {
    override fun getPriority(): Int {
        return 0
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component-property" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val propertyInfo: PropertyInfo = context["info"]

        if (!(propertyInfo.isReadable && propertyInfo.isWritable)) {
            return false
        }

        val componentContext: ItemContext = context["componentContext"]
        val loaderCompanionContext: ItemContext = componentContext["companion:loader"]
        val loadMethodMutations: Mutations<CodeBlock.Builder> =
                loaderCompanionContext["codeMutations:method:load"]

        val code = RootOperationGenerator.generateOperation(
                "load-property",
                propertyInfo,
                context.context,
                RootOperationGenerator,
                mapOf(
                        "lvalue" to "dst",
                        "rvalue" to "data.getField(\"${propertyInfo.name}\")"
                )
        )

        code?.let {
            loadMethodMutations.add {
                add(code)
            }

            context["processed"] = true
        }

        return false
    }
}
