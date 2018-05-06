package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.operations.PropertyOperationGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.properties.PropertyInfo
import javax.lang.model.type.ArrayType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class LoadArrayGenerator : PropertyOperationGenerator {
    /*
    lvalue: T[]
    rvalue: IDO
    {
        List<IDO> list = rvalue.getList();
        int size = list.size();
        lvalue = new T[size];
        // Assume it's an RandomAccess-list
        for (int counter = 0; counter < size; ++counter) {
            {load lvalue[counter] from list.get(counter)}
        }
    }
     */

    override fun generateOperation(
            operation: String,
            propertyInfo: PropertyInfo,
            processingContext: ProcessingContext,
            root: PropertyOperationGenerator,
            args: MutableMap<String, Any>): CodeBlock? {
        if ("load" != operation) return null

        val type = args["type"] as TypeMirror

        if (type.kind != TypeKind.ARRAY) return null

        val elemType = (type as ArrayType).componentType

        val dstArray = args["lvalue"] as String
        val counterLocal = makeLocalName("counter")
        val listLocal = makeLocalName("list")

        val elemLoad = root.generateOperation(
                "load", propertyInfo, processingContext, root,
                mapOf(
                        "lvalue" to "$dstArray[$counterLocal]",
                        "rvalue" to "$listLocal.get($counterLocal)",
                        "type" to elemType
                )
        ) ?: return null

        val srcIDO = args["rvalue"] as String
        val sizeLocal = makeLocalName("size")

        val cb = CodeBlock.builder()

        cb.run {
            beginControlFlow("")
            add("\$T<? extends \$T> $listLocal = $srcIDO.getList();\n",
                    List::class.java, InputDataObject::class.java)
            add("int $sizeLocal = $listLocal.size();\n")
            add("$dstArray = ${arrayInstantiationExpression(type, sizeLocal)};\n")
            beginControlFlow(
                    "for (int $counterLocal = 0; $counterLocal < $sizeLocal; ++$counterLocal)")
            add(elemLoad)
            endControlFlow()
            endControlFlow()
        }

        return cb.build()
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
