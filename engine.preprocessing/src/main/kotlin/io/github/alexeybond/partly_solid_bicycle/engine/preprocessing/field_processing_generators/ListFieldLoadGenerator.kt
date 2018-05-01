package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.field_processing_generators

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.FieldLoadGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.localVarName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

class ListFieldLoadGenerator : FieldLoadGenerator {
    override fun generateRead(
            processingEnvironment: ProcessingEnvironment,
            targetType: TypeMirror,
            lvalueExpr: String,
            rvalueExpr: String,
            rootGenerator: FieldLoadGenerator): String? {
        val tu = processingEnvironment.typeUtils
        val eu = processingEnvironment.elementUtils
        if (!tu.isAssignable(
                tu.erasure(targetType),
                eu.getTypeElement(java.lang.Iterable::class.java.canonicalName).asType())) {
            return null
        }
        if (!tu.isSubtype(
                eu.getTypeElement(ArrayList::class.java.canonicalName).asType(),
                tu.erasure(targetType)))
            return null
        val typeArgs = (targetType as DeclaredType).typeArguments
        if (typeArgs.size != 1) {
            processingEnvironment.messager.printMessage(
                    Diagnostic.Kind.WARNING,
                    "Type $targetType seems to be a list but has no type parameters."
            )
            return null
        }
        val elemType = typeArgs[0]

        val rawElemVarName = localVarName("data")
        val loadedElemVarName = localVarName("elem")
        val listVarName = localVarName("result")

        val itemLoadStmt = rootGenerator.generateRead(
                processingEnvironment,
                elemType,
                loadedElemVarName, rawElemVarName,
                rootGenerator
        )

        if (null == itemLoadStmt) return null

        return """
            |${ArrayList::class.java.canonicalName}
            |$listVarName = new ${ArrayList::class.java.canonicalName}($rvalueExpr.getList().size());
            |for (${InputDataObject::class.java.canonicalName} $rawElemVarName: $rvalueExpr.getList()) {
            |   ${elemType} $loadedElemVarName;
            |   $itemLoadStmt
            |   $listVarName.process($loadedElemVarName);
            |}
            |$lvalueExpr = $listVarName;
            |
        """.trimMargin()
    }
}
