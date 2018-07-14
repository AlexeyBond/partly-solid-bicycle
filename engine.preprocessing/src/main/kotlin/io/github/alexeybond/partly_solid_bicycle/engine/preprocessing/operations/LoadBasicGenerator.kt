package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.operations.PropertyOperationGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.PropertyInfo
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class LoadBasicGenerator : PropertyOperationGenerator {
    private val STRING_CCN = String::class.java.canonicalName
    private val IDO_CCN = InputDataObject::class.java.canonicalName

    override fun generateOperation(
            operation: String,
            propertyInfo: PropertyInfo,
            processingContext: ProcessingContext,
            root: PropertyOperationGenerator,
            args: MutableMap<String, Any>): CodeBlock? {
        if (operation != "load") return null

        val tu = processingContext.env.typeUtils
        val pt = args["type"] as TypeMirror

        fun doGenerate(fn: (String) -> String): CodeBlock {
            val cb = CodeBlock.builder()
            val dstExpr = args["lvalue"] as String
            val srcExpr = args["rvalue"] as String
            cb.add("$dstExpr = ${fn(srcExpr)};\n")
            return cb.build()
        }

        return when {
            tu.isSameType(pt, tu.getPrimitiveType(TypeKind.BOOLEAN)) ->
                doGenerate { "$it.getBoolean()" }
            tu.isSameType(pt, tu.getPrimitiveType(TypeKind.LONG)) ->
                doGenerate { "$it.getLong()" }
            tu.isSameType(pt, tu.getPrimitiveType(TypeKind.DOUBLE)) ->
                doGenerate { "$it.getDouble()" }
            tu.isSameType(pt, tu.getPrimitiveType(TypeKind.INT)) ->
                doGenerate { "(int) $it.getLong()" }
            tu.isSameType(pt, tu.getPrimitiveType(TypeKind.FLOAT)) ->
                doGenerate { "(float) $it.getDouble()" }
            pt.kind == TypeKind.DECLARED && pt.toString() == STRING_CCN ->
                doGenerate { "$it.getString()" }
            pt.kind == TypeKind.DECLARED && pt.toString() == IDO_CCN ->
                doGenerate { it }
            else -> null
        }
    }
}
