package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.component

import com.squareup.javapoet.ClassName
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.getAnnotationMirror
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.getListValue
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.getValue
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.process
import javax.annotation.processing.RoundEnvironment

class ComponentDiscover : ItemProcessor {
    override fun getPriority(): Int {
        return 1_000
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "round" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val pCtx = context.context
        val pEnv = pCtx.env
        val roundEnv: RoundEnvironment = context["roundEnv"]

        roundEnv.getElementsAnnotatedWith(Component::class.java)
                .forEach({ componentElem ->
                    val annotation = componentElem.getAnnotationMirror(pEnv, Component::class)!!
                    val className = ClassName.get(componentElem.asType())

                    pCtx.process("component:$className") {
                        set("kind", "component")
                        set("annotation", annotation)
                        set("className", className)
                        set("names", annotation.getValue(pEnv.elementUtils, "name")
                                .getListValue<String>())
                        set("envs", annotation.getValue(pEnv.elementUtils, "env")
                                .getListValue<String>())
                        set("componentKind", annotation.getValue(pEnv.elementUtils, "kind").value)
                        set("element", componentElem)
                        set("typeMirror", componentElem.asType())
                    }
                })

        return false
    }
}
