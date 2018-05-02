package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class AnnotationProcessorImpl : AbstractProcessor() {
    private val itemProcessors = ServiceLoader.load(ItemProcessor::class.java, javaClass.classLoader)
            .sortedBy { it.priority }
    private var roundCounter = 0

    private fun doProcessRound(roundEnv: RoundEnvironment) {
        val pContext = ProcessingContextImpl(processingEnv, itemProcessors)

        pContext.process("round:${roundCounter++}") {
            set("kind", "round")
            set("roundEnv", roundEnv)
        }

        pContext.writeOutClasses()
    }

    override fun process(
            annotations: MutableSet<out TypeElement>?,
            roundEnv: RoundEnvironment): Boolean {
        try {
            doProcessRound(roundEnv)
        } catch (e: Exception) {
            e.printStackTrace()
            processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Fatal error: exception occurred in PSB annotation processor:\n$e"
            )
            // Do not rethrow exception to compiler as exception thrown to compiler
            // causes some strange behavior sometimes
        }

        return false
    }
}
