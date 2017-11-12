package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.field_processing_generators

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.FieldLoadGenerator
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeKind

class DefaultFieldLoadGenerator : FieldLoadGenerator {
    override fun generateRead(
            processingEnvironment: ProcessingEnvironment,
            field: VariableElement,
            lvalueExpr: String,
            rvalueExpr: String): String? {
        val ft = field.asType()

        return processingEnvironment.typeUtils.run {
            when {
                isSameType(ft, getPrimitiveType(TypeKind.BOOLEAN)) ->
                    "$lvalueExpr = $rvalueExpr.getBoolean();"
                isSameType(ft, getPrimitiveType(TypeKind.FLOAT)) ->
                    "$lvalueExpr = (float) $rvalueExpr.getDouble();"
                isSameType(ft, getPrimitiveType(TypeKind.DOUBLE)) ->
                    "$lvalueExpr = $rvalueExpr.getDouble();"
                isSameType(ft, getPrimitiveType(TypeKind.INT)) ->
                    "$lvalueExpr = (int) $rvalueExpr.getLong();"
                isSameType(ft, getPrimitiveType(TypeKind.LONG)) ->
                    "$lvalueExpr = $rvalueExpr.getLong();"
                ft.toString().equals(String::class.java.canonicalName) ->
                    "$lvalueExpr = $rvalueExpr.getString();"
                else -> null
            }
        }
    }
}
