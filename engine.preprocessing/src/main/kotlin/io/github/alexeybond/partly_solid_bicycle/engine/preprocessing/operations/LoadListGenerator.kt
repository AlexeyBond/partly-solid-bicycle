package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.operations.PropertyOperationGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.PropertyInfo
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

class LoadListGenerator : PropertyOperationGenerator {
    /*
    lvalue: List<T>
    rvalue: IDO
    {
        List<IDO> srcList = rvalue.getList();
        int size = srcList.size();
        ArrayList<T> dstList = new ArrayList(size);

        for (int counter = 0; counter < size; ++counter) {
            T tmp;

            {load tmp from srcList.get(counter)}

            dstList.add(tmp);
        }

        lvalue = dstList;
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

        val penv = processingContext.env
        val tu = penv.typeUtils
        val eu = penv.elementUtils

        if (!tu.isAssignable(
                        tu.erasure(type),
                        eu.getTypeElement(java.lang.Iterable::class.java.canonicalName).asType())) {
            return null
        }
        if (!tu.isSubtype(
                        eu.getTypeElement(ArrayList::class.java.canonicalName).asType(),
                        tu.erasure(type)))
            return null
        val typeArgs = (type as DeclaredType).typeArguments
        if (typeArgs.size != 1) {
            penv.messager.printMessage(
                    Diagnostic.Kind.WARNING,
                    "Type $type seems to be a list but has no type parameters.",
                    propertyInfo.declaringElements[0]
            )
            return null
        }
        val elemType = typeArgs[0]

        val tmpLocal = makeLocalName("tmp")
        val srcLocal = makeLocalName("srcList")
        val counterLocal = makeLocalName("counter")

        val code = root.generateOperation(
                "load", propertyInfo, processingContext, root,
                mapOf(
                        "lvalue" to tmpLocal,
                        "rvalue" to "$srcLocal.get($counterLocal)",
                        "type" to elemType
                )
        ) ?: return null

        val sizeLocal = makeLocalName("size")
        val dstLocal = makeLocalName("dstList")

        val cb = CodeBlock.builder()

        cb.run {
            add("\$T<? extends \$T> $srcLocal = ${args["rvalue"]}.getList();\n",
                    List::class.java, InputDataObject::class.java)
            add("int $sizeLocal = $srcLocal.size();\n")
            add("\$T<\$T> $dstLocal = new \$T<\$T>($sizeLocal);\n",
                    ArrayList::class.java, elemType, ArrayList::class.java, elemType)

            beginControlFlow(
                    "for(int $counterLocal = 0; $counterLocal < $sizeLocal; ++$counterLocal)")
            add("\$T $tmpLocal;\n", elemType)
            add(code)
            add("$dstLocal.add($tmpLocal);\n")
            endControlFlow()
            add("${args["lvalue"]} = $dstLocal;\n")
        }

        return cb.build()
    }
}
