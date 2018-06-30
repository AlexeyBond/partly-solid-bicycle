package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.generateAssignment
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.operations.PropertyOperationGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.properties.PropertyInfo

class PropertyLoadGenerator : PropertyOperationGenerator {
    override fun generateOperation(
            operation: String,
            propertyInfo: PropertyInfo,
            processingContext: ProcessingContext,
            root: PropertyOperationGenerator,
            args: MutableMap<String, Any>): CodeBlock? {
        if ("load-property" != operation) return null

        val tmpLocal = makeLocalName("tmp")
        val valLocal = makeLocalName("val")

        val code = root.generateOperation(
                "load", propertyInfo, processingContext, root,
                mapOf(
                        "type" to propertyInfo.type,
                        "lvalue" to tmpLocal,
                        "rvalue" to valLocal
                )
        ) ?: return null

        val isOptional = propertyInfo.metadata["property.isOptional"] == "true"

        val cb = CodeBlock.builder()

        cb.beginControlFlow("do")
        cb.add("\$T $valLocal;\n", InputDataObject::class.java)

        writeRead(cb, isOptional) { cb.add("$valLocal = ${args["rvalue"]};\n") }

        cb.add("\$T $tmpLocal;\n", propertyInfo.type)
        cb.add(code)
        cb.add("${propertyInfo.generateAssignment(args["lvalue"] as String, tmpLocal)}\n")
        cb.endControlFlow("while(false)")

        return cb.build()
    }

    private
    inline fun writeRead(cb: CodeBlock.Builder, optional: Boolean, fn: CodeBlock.Builder.() -> Unit) {
        if (optional) {
            cb.run {
                beginControlFlow("try")
                run(fn)
                endControlFlow("catch (\$T ignore) {break;}",
                        UndefinedFieldException::class.java)
            }
        } else {
            cb.run(fn)
        }
    }
}
