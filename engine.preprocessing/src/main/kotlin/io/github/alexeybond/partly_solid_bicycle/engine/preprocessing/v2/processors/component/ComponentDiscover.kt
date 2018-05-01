package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.component

import com.squareup.javapoet.ClassName
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.process
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
        val roundEnv: RoundEnvironment = context["roundEnv"]

        roundEnv.getElementsAnnotatedWith(Component::class.java)
                .forEach({ componentElem ->
                    val annotation = componentElem.getAnnotation(Component::class.java)
                    val className = ClassName.get(componentElem.asType())

                    pCtx.process("component:$className") {
                        set("kind", "component")
                        set("annotation", annotation)
                        set("className", className)
                        set("name", annotation.name)
                        set("element", componentElem)
                        set("typeMirror", componentElem.asType())
                    }
                })

        return false
    }
}
