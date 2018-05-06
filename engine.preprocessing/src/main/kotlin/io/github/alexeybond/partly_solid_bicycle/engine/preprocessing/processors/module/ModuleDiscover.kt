package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.module

import com.squareup.javapoet.ClassName
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.GeneratedModule
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.process
import javax.annotation.processing.RoundEnvironment

class ModuleDiscover : ItemProcessor {
    override fun acceptsItemKind(itemKind: String): Boolean {
        return itemKind == "round"
    }

    override fun processItem(context: ItemContext) {
        val roundEnv: RoundEnvironment = context["roundEnv"]
        val pCtx = context.context

        roundEnv.getElementsAnnotatedWith(GeneratedModule::class.java)
                .forEach({ moduleElement ->
                    val moduleAnnotation = moduleElement.getAnnotation(GeneratedModule::class.java)
                    val className = ClassName.get(moduleElement.asType())

                    val moduleItem = pCtx.process("module:$className") {
                        set("kind", "module")
                        set("className", className)
                        set("annotation", moduleAnnotation)
                        set("element", moduleElement)
                        set("typeMirror", moduleElement.asType())
                    }

                    if (moduleAnnotation.useAsDefault) {
                        pCtx.process("module::default") {
                            set("kind", "defaultModule")
                            set("className", className)
                            set("module", moduleItem)
                        }
                    }
                })
    }

    override fun getPriority(): Int {
        return 0
    }
}
