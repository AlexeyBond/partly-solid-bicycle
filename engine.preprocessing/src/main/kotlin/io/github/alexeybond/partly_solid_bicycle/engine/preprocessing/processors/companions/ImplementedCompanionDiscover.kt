package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions

import com.squareup.javapoet.ClassName
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.ComponentCompanion
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.getAnnotationMirror
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.getListValue
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.getValue
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.process
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic

class ImplementedCompanionDiscover : ItemProcessor {
    override fun getPriority(): Int {
        return Int.MAX_VALUE
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "round" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val roundEnv: RoundEnvironment = context["roundEnv"]
        val pCtx = context.context
        val pEnv = pCtx.env

        roundEnv.getElementsAnnotatedWith(ComponentCompanion::class.java)
                .forEach { elem ->
                    val annotation: AnnotationMirror = elem
                            .getAnnotationMirror(pEnv, ComponentCompanion::class)!!
                    val type: String = annotation
                            .getValue(pEnv.elementUtils, "companionType")
                            .value as String
                    val componentTM: DeclaredType = annotation
                            .getValue(pEnv.elementUtils, "component")
                            .value as DeclaredType
                    val envs: List<String> = annotation
                            .getValue(pEnv.elementUtils, "env")
                            .getListValue()
                    val componentCNS = (componentTM.asElement() as TypeElement).qualifiedName

                    val componentCtx = try {
                        pCtx.getItem("component:$componentCNS")
                    } catch (e: NoSuchElementException) {
                        pEnv.messager.printMessage(
                                Diagnostic.Kind.ERROR,
                                "Class passed as component is not a component",
                                elem, annotation, annotation.getValue(pEnv.elementUtils, "component")
                        )

                        return@forEach
                    }

                    val componentCN = componentCtx.get<ClassName>("className")
                    val companionCN = ClassName.get(elem as TypeElement)

                    pCtx.process("companion:$type:$componentCN:preImplemented:$companionCN") {
                        set("kind", "component-companion")

                        set("companionType", type)
                        set("component", componentCtx)
                        set("envs", envs)
                        set("className", companionCN)
                        set("implClassName", companionCN)
                    }
                }

        return false
    }
}
