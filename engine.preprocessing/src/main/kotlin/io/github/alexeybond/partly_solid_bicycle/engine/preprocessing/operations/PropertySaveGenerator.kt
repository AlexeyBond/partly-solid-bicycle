package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.OutputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.generateRead
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.operations.PropertyOperationGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.properties.PropertyInfo

class PropertySaveGenerator : PropertyOperationGenerator {
    override fun generateOperation(
            operation: String,
            propertyInfo: PropertyInfo,
            processingContext: ProcessingContext,
            root: PropertyOperationGenerator,
            args: MutableMap<String, Any>): CodeBlock? {
        if ("save-property" != operation) return null

        val tmpLocal = makeLocalName("tmp")
        val valLocal = makeLocalName("val")

        val code = root.generateOperation(
                "save", propertyInfo, processingContext, root,
                mapOf(
                        "type" to propertyInfo.type,
                        "lvalue" to valLocal,
                        "rvalue" to tmpLocal
                )
        ) ?: return null

        val cb = CodeBlock.builder()

        cb.add("\$T $tmpLocal = ${propertyInfo.generateRead(args["rvalue"] as String)};\n",
                propertyInfo.type)
        cb.add("\$T $valLocal = ${args["lvalue"]};\n", OutputDataObject::class.java)
        cb.add(code);

        return cb.build()
    }
}
