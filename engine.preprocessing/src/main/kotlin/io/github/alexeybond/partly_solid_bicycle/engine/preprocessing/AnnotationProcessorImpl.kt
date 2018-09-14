package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import java.io.PrintWriter
import java.io.StringWriter
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
    private val itemProcessors: List<ItemProcessor>
    private var roundCounter = 0

    init {
        try {
            itemProcessors = ServiceLoader.load(ItemProcessor::class.java, javaClass.classLoader)
                    .sortedBy { it.priority }
        } catch (e: Throwable) {
            // Java compiler itself is not verbose enough in this case
            System.err.println("Fatal error in annotation processor initializer: $e")
            // Gradle daemons are really the goddamn daemons...
            System.err.println("THIS MIGHT BE CAUSED BY PREPROCESSING CODE CHANGES WITH RUNNING GRADLE DAEMON")
            e.printStackTrace(System.err)
            throw e
        }
    }

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
            val sw = StringWriter()

            PrintWriter(sw).use { e.printStackTrace(it) }
            processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Fatal error: exception occurred in PSB annotation processor:\n${sw.buffer}"
            )
            // Do not rethrow exception to compiler as exception thrown to compiler
            // causes some strange behavior sometimes
        }

        return false
    }
}
