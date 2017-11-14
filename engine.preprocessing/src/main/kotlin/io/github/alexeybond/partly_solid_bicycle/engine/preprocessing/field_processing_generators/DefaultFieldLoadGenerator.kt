package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.field_processing_generators

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.FieldLoadGenerator
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class DefaultFieldLoadGenerator : FieldLoadGenerator {
    override fun generateRead(
            processingEnvironment: ProcessingEnvironment,
            targetType: TypeMirror,
            lvalueExpr: String,
            rvalueExpr: String): String? {
        return processingEnvironment.typeUtils.run {
            when {
                isSameType(targetType, getPrimitiveType(TypeKind.BOOLEAN)) ->
                    "$lvalueExpr = $rvalueExpr.getBoolean();"
                isSameType(targetType, getPrimitiveType(TypeKind.FLOAT)) ->
                    "$lvalueExpr = (float) $rvalueExpr.getDouble();"
                isSameType(targetType, getPrimitiveType(TypeKind.DOUBLE)) ->
                    "$lvalueExpr = $rvalueExpr.getDouble();"
                isSameType(targetType, getPrimitiveType(TypeKind.INT)) ->
                    "$lvalueExpr = (int) $rvalueExpr.getLong();"
                isSameType(targetType, getPrimitiveType(TypeKind.LONG)) ->
                    "$lvalueExpr = $rvalueExpr.getLong();"
                targetType.toString().equals(String::class.java.canonicalName) ->
                    "$lvalueExpr = $rvalueExpr.getString();"
                else -> null
            }
        }
    }
}
