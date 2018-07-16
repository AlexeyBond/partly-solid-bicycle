package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.saver

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.escapeStringLiteral
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.PropertyInfo
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations.RootOperationGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.serializedName

class PropertySave : ItemProcessor {
    override fun getPriority(): Int {
        return 0
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component-property" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val propertyInfo: PropertyInfo = context["info"]

        if (!(propertyInfo.isReadable && propertyInfo.isWritable)) {
            return
        }

        val componentContext: ItemContext = context["componentContext"]
        val saverCompanionContext: ItemContext = componentContext["companion:saver"]
        val saveMethodMutations: Mutations<CodeBlock.Builder> =
                saverCompanionContext["codeMutations:method:save"]

        val code = RootOperationGenerator.generateOperation(
                "save-property",
                propertyInfo,
                context.context,
                RootOperationGenerator,
                mapOf(
                        "rvalue" to "src",
                        "lvalue" to "data.addField(\"${escapeStringLiteral(propertyInfo.serializedName())}\")"
                )
        )

        code?.let {
            saveMethodMutations.add {
                add(code)
            }

            context["processed"] = true
        }
    }
}
