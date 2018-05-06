package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.module

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.tools.Diagnostic

class ModuleImplementationInit : ItemProcessor {
    override fun acceptsItemKind(itemKind: String): Boolean {
        return "module" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val typeElement: TypeElement = context["element"]
        val className: ClassName = context["className"]

        if (typeElement.superclass.kind !== TypeKind.ERROR) {
            context.context.env.messager.printMessage(Diagnostic.Kind.ERROR,
                    "Generated module class must extend a class " +
                            "not present in project sources or dependencies.",
                    typeElement)
        }

        val implClassName: ClassName = ClassName.get(
                className.packageName(),
                (typeElement.superclass as DeclaredType).asElement().simpleName.toString()
        )

        val implMutationAccumulator = context
                .context.addGenerated(implClassName, TypeSpec.classBuilder(implClassName))

        implMutationAccumulator.add {
            addSuperinterface(Module::class.java)
        }

        context["implClassName"] = implClassName

        context["implMutations"] = implMutationAccumulator

        return false
    }

    override fun getPriority(): Int {
        return -10_000
    }
}
