package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors

import com.squareup.javapoet.JavaFile
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.CompanionTypeCreator
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import kotlin.collections.HashMap

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ComponentCompanionProcessor : AbstractProcessor() {
    private val companionCreators: HashMap<String, CompanionTypeCreator> = HashMap()

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv!!)

        ServiceLoader.load(CompanionTypeCreator::class.java, javaClass.classLoader).forEach { creator ->
            creator.companionTypes.forEach { type ->
                companionCreators[type]?.let { conflicting ->
                    processingEnv.messager.printMessage(Diagnostic.Kind.WARNING,
                            "Component companion creators $conflicting and $creator " +
                                    "have a conflict for companion type '$type'; " +
                                    "creator classes are: " +
                                    "" + "${conflicting.javaClass.canonicalName} and " +
                                    "" + "${creator.javaClass.canonicalName}.")
                }

                processingEnv.messager.printMessage(Diagnostic.Kind.NOTE,
                        "Using $creator to create companion classes of type $type; " +
                                "FQN is ${creator.javaClass.canonicalName}.")
                companionCreators[type] = creator
            }
        }
    }

    override fun process(
            annotations: MutableSet<out TypeElement>?,
            roundEnv: RoundEnvironment?
    ): Boolean {
        roundEnv!!.getElementsAnnotatedWith(Component::class.java).forEach { componentType ->
            componentType as TypeElement
            companionCreators.forEach { (type, creator) ->
                val name = companionClassName(componentType, type)

                creator.generateCompanion(processingEnv, type, name, componentType)?.let { source ->
                    JavaFile.builder(name.packageName(), source).build()
                            .writeTo(processingEnv.filer)
                }
            }
        }

        return false
    }
}
