package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.operations.PropertyOperationGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.properties.PropertyInfo
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.nodePathCCN
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class SaveNodePathGenerator : PropertyOperationGenerator {
    override fun generateOperation(
            operation: String,
            propertyInfo: PropertyInfo,
            processingContext: ProcessingContext,
            root: PropertyOperationGenerator,
            args: MutableMap<String, Any>): CodeBlock? {
        if ("save" != operation) return null

        val pt = args["type"] as TypeMirror

        if (pt.kind != TypeKind.DECLARED || pt.toString() != nodePathCCN)
            return null

        return CodeBlock.of(
                "${args["lvalue"]}.setString(${args["rvalue"]}.toString());\n"
        )
    }
}
