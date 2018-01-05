package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.field_processing_generators

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.FieldLoadGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.localVarName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.ArrayType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class ArrayFieldLoadGenerator : FieldLoadGenerator {
    override fun generateRead(
            processingEnvironment: ProcessingEnvironment,
            targetType: TypeMirror,
            lvalueExpr: String,
            rvalueExpr: String,
            rootGenerator: FieldLoadGenerator): String? {
        if (targetType.kind != TypeKind.ARRAY) return null

        val elemType = (targetType as ArrayType).componentType

        val counterVar = localVarName("counter")
        val listVar = localVarName("list")

        val elemAssignment = rootGenerator.generateRead(
                processingEnvironment, elemType,
                "$lvalueExpr[$counterVar]", "$listVar.get($counterVar)",
                rootGenerator
        )

        if (null == elemAssignment) return null

        return """
            |${java.util.List::class.java.canonicalName}<? extends ${InputDataObject::class.java.canonicalName}>
            |$listVar = $rvalueExpr.getList();
            |$lvalueExpr = ${arrayInstantiationExpression(targetType, "$listVar.size()")};
            |for (int $counterVar = 0; $counterVar < $listVar.size(); ++$counterVar) {
            |   $elemAssignment
            |}
        """.trimMargin()
    }

    private fun arrayInstantiationExpression(type: ArrayType, sizeExpression: String): String {
        var cType = type.componentType
        var nestedLevels = 0

        while (cType.kind == TypeKind.ARRAY) {
            cType = (cType as ArrayType).componentType
            ++nestedLevels
        }

        return "new $cType[$sizeExpression]${"[]".repeat(nestedLevels)}"
    }
}
