package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.operations.PropertyOperationGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.PropertyInfo
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class SaveBasicGenerator : PropertyOperationGenerator {
    private val STRING_CCN = String::class.java.canonicalName
    private val IDO_CCN = InputDataObject::class.java.canonicalName
    private val COPY_VISITOR_CN = ClassName.get("io.github.alexeybond.partly_solid_bicycle.core.impl.data.visitors", "CopyVisitor")

    override fun generateOperation(
            operation: String,
            propertyInfo: PropertyInfo,
            processingContext: ProcessingContext,
            root: PropertyOperationGenerator,
            args: MutableMap<String, Any>): CodeBlock? {
        if (operation != "save") return null

        val tu = processingContext.env.typeUtils
        val pt = args["type"] as TypeMirror

        fun doGenerate(fn: (String) -> String): CodeBlock {
            val cb = CodeBlock.builder()
            val dstExpr = args["lvalue"] as String
            val srcExpr = args["rvalue"] as String
            cb.add("$dstExpr.${fn(srcExpr)};\n")
            return cb.build()
        }
        return when {
            tu.isSameType(pt, tu.getPrimitiveType(TypeKind.BOOLEAN)) ->
                doGenerate { "setBoolean($it)" }
            tu.isSameType(pt, tu.getPrimitiveType(TypeKind.LONG)) ->
                doGenerate { "setLong($it)" }
            tu.isSameType(pt, tu.getPrimitiveType(TypeKind.DOUBLE)) ->
                doGenerate { "setDouble($it)" }
            tu.isSameType(pt, tu.getPrimitiveType(TypeKind.INT)) ->
                doGenerate { "setLong((long) $it)" }
            tu.isSameType(pt, tu.getPrimitiveType(TypeKind.FLOAT)) ->
                doGenerate { "setDouble((double) $it)" }
            pt.kind == TypeKind.DECLARED && pt.toString() == STRING_CCN ->
                doGenerate { "setString($it)" }
            pt.kind == TypeKind.DECLARED && pt.toString() == IDO_CCN ->
                CodeBlock.of("if (null != ${args["rvalue"]}) new \$T().doCopy(${args["rvalue"]}, ${args["lvalue"]});\n", COPY_VISITOR_CN)
            else -> null
        }
    }
}
