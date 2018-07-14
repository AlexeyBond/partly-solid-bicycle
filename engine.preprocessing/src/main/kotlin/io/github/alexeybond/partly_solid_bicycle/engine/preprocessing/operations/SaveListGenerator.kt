package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.OutputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.operations.PropertyOperationGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.PropertyInfo
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

class SaveListGenerator : PropertyOperationGenerator {
    /*
    lvalue: ODO
    rvalue: Iterable<T> | T[]

    for (T val: rvalue) {
        ODO data = lvalue.addItem();

        {save val to data}
    }
     */
    override fun generateOperation(
            operation: String,
            propertyInfo: PropertyInfo,
            processingContext: ProcessingContext,
            root: PropertyOperationGenerator,
            args: MutableMap<String, Any>): CodeBlock? {
        if ("save" != operation) return null

        val type = args["type"] as TypeMirror

        val penv = processingContext.env
        val tu = penv.typeUtils
        val eu = penv.elementUtils

        val elemType: TypeMirror

        when {
            tu.isAssignable(tu.erasure(type), eu.getTypeElement("java.lang.Iterable").asType()) -> {
                val typeArgs = (type as DeclaredType).typeArguments
                if (typeArgs.size != 1) {
                    penv.messager.printMessage(
                            Diagnostic.Kind.WARNING,
                            "Type $type seems to be a list but has no type parameters.",
                            propertyInfo.declaringElements[0]
                    )
                    return null
                }
                elemType = typeArgs[0]
            }
            type.kind == TypeKind.ARRAY -> elemType = (type as ArrayType).componentType
            else -> return null
        }

        val valLocal = makeLocalName("val")
        val dataLocal = makeLocalName("data")

        val code = root.generateOperation(
                "save", propertyInfo, processingContext, root,
                mapOf(
                        "type" to elemType,
                        "lvalue" to dataLocal,
                        "rvalue" to valLocal
                )
        ) ?: return null

        val cb = CodeBlock.builder()

        cb.run {
            beginControlFlow("for (\$T $valLocal : ${args["rvalue"]})", elemType)
            add("\$T $dataLocal = ${args["lvalue"]}.addItem();\n", OutputDataObject::class.java)
            add(code)
            endControlFlow()
        }

        return cb.build()
    }
}
