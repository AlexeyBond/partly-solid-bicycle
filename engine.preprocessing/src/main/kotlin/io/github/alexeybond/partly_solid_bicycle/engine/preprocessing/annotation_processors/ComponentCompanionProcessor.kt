package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionOwner
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.ComponentCompanion
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.CompanionTypeCreator
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ComponentCompanionProcessor : AbstractProcessor() {
    // companionCreators[env][type] = creator
    private val companionCreators: HashMap<String, HashMap<String, CompanionTypeCreator>> = HashMap()

    private lateinit var companionAnnotationType: TypeMirror
    private lateinit var componentAnnotationType: TypeMirror
    private lateinit var tu: Types
    private lateinit var eu: Elements

    // companions[env][component_class][companion_type] = companion_class
    private val companions = HashMap<String, HashMap<TypeName, HashMap<String, TypeName>>>()

    private val components = ArrayList<TypeElement>()

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv!!)

        ServiceLoader.load(CompanionTypeCreator::class.java, javaClass.classLoader).forEach { creator ->
            creator.companionEnvironments.forEach { env ->
                val envCreators = companionCreators.computeIfAbsent(env, { HashMap() })
                creator.companionTypes.forEach { type ->
                    envCreators[type]?.let { conflicting ->
                        processingEnv.messager.printMessage(Diagnostic.Kind.WARNING,
                                "Component companion creators $conflicting and $creator " +
                                        "have a conflict for companion type '$type'" +
                                        " in '$env' environment; " +
                                        "creator classes are: " +
                                        "" + "${conflicting.javaClass.canonicalName} and " +
                                        "" + "${creator.javaClass.canonicalName}.")
                    }

                    processingEnv.messager.printMessage(Diagnostic.Kind.NOTE,
                            "Using $creator to create companion classes of type '$type'; " +
                                    "FQN is ${creator.javaClass.canonicalName}.")
                    envCreators[type] = creator
                }
            }
        }

        tu = processingEnv.typeUtils
        eu = processingEnv.elementUtils
        companionAnnotationType = eu.getTypeElement(ComponentCompanion::class.java.canonicalName).asType()
        componentAnnotationType = eu.getTypeElement(Component::class.java.canonicalName).asType()
    }

    override fun process(
            annotations: MutableSet<out TypeElement>?,
            roundEnv: RoundEnvironment?
    ): Boolean {
        roundEnv!!
        cleanup()

        discoverComponents(roundEnv)
        discoverPredefinedCompanions(roundEnv)

        generateCompanions()
//        logCompanions()

        generateCompanionOwners()

        return false
    }

    private fun cleanup() {
        components.clear()
        companions.clear()
    }

    private fun rememberCompanion(
            envs: Iterable<String>,
            componentClass: TypeName,
            companionType: String,
            companionClass: TypeName
    ) {
        envs.forEach({ env ->
            val m1 = companions.computeIfAbsent(env, { HashMap() })

            val m2 = m1.computeIfAbsent(componentClass, { HashMap() })

            m2.put(companionType, companionClass)
        })
    }

    private fun discoverComponents(roundEnv: RoundEnvironment) {
        roundEnv.getElementsAnnotatedWith(Component::class.java).forEach { componentType ->
            componentType as TypeElement
            components.add(componentType)
        }
    }

    private fun discoverPredefinedCompanions(roundEnv: RoundEnvironment) {
        val tu = processingEnv.typeUtils
        val eu = processingEnv.elementUtils

        roundEnv.getElementsAnnotatedWith(ComponentCompanion::class.java).forEach { companionClass ->
            companionClass as TypeElement
            val companionCN = ClassName.get(companionClass.asType())

            val annotation = companionClass.annotationMirrors
                    .find { a -> tu.isSameType(a.annotationType, companionAnnotationType) }!!

            val componentClassMirror = annotation.getValue(eu, "component").value as TypeMirror

            if (0 == eu.getTypeElement(componentClassMirror.toString()).annotationMirrors
                    .count { a -> tu.isSameType(a.annotationType, componentAnnotationType) }) {
                processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                        "Companion registered for a class not annotated as a component.\n" +
                                "Component class is $componentClassMirror;\nCompanion class is $companionClass.",
                        companionClass, annotation)
                return@forEach
            }

            val componentClassName = ClassName.get(componentClassMirror)

            val companionTypeName = annotation.getValue(eu, "companionType").value as String

            val envs = annotation.getValue(eu, "env").getListValue<String>()

            rememberCompanion(envs, componentClassName, companionTypeName, companionCN)
        }
    }

    private fun generateCompanions() {
        components.forEach { componentType ->
            companionCreators.forEach { env, map ->
                map.forEach inner@ { type, creator ->
                    val present = companions[env]
                            ?.get(ClassName.get(componentType.asType()))
                            ?.get(type)

                    if (null != present) {
                        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE,
                                "Generated companion of type '$type' in '$env' environment is overridden " +
                                        "by '$present' for '$componentType'.")
                        return@inner
                    }

                    val name = companionClassName(componentType, type)

                    creator.generateCompanion(env, processingEnv, type, name, componentType)?.let { source ->
                        JavaFile.builder(name.packageName(), source).build()
                                .writeTo(processingEnv.filer)
                        rememberCompanion(listOf(env), ClassName.get(componentType), type, name)
                    }
                }
            }
        }
    }

    private fun generateCompanionOwners() {
        val defaultCompanions = companions.remove("default") ?: HashMap()
        components.forEach { componentType ->
            val className = ClassName.get(componentType)

            val companionOwnerName = companionOwnerClassName(className)

            val companionsFieldSpec = FieldSpec.builder(
                    ParameterizedTypeName.get(
                            java.util.Map::class.java,
                            String::class.java,
                            CompanionResolver::class.java),
                    COMPANION_MAP_FIELD_NAME,
                    Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new \$T()", HashMap::class.java)
                    .build()

            val getCompanionMethodSpec = MethodSpec.methodBuilder("getCompanionObject")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(ClassName.get(Companion::class.java))
                    .addParameter(ClassName.get(String::class.java), "name")
                    .addCode("\$T resolver = $COMPANION_MAP_FIELD_NAME.get(name);\n",
                            CompanionResolver::class.java)
                    .beginControlFlow("if (null == resolver)")
                    .addCode("throw new \$T(\$S + name + \$S);\n",
                            IllegalArgumentException::class.java,
                            "No companion of type '", "' found for component of class $className")
                    .endControlFlow()
                    .addCode("return resolver.resolve(this);\n")
                    .build()

            val initializer = defaultCompanions[className]?.let { companions ->
                var builder = CodeBlock.builder()

                companions.forEach { type, cls ->
                    builder = builder.add(
                            "$COMPANION_MAP_FIELD_NAME.put(\$S, \$T.$COMPANION_RESOLVER_FIELD_NAME);\n",
                            type, cls)
                }

                builder.build()
            } ?: CodeBlock.of("")

            val typeSpec = TypeSpec.classBuilder(companionOwnerName)
                    .superclass(className)
                    .addSuperinterface(
                            ParameterizedTypeName.get(
                                    ClassName.get(CompanionOwner::class.java),
                                    className
                            ))
                    .addField(companionsFieldSpec)
                    .addMethod(getCompanionMethodSpec)
                    .addStaticBlock(initializer)
                    .build()

            JavaFile.builder(companionOwnerName.packageName(), typeSpec).build()
                    .writeTo(processingEnv.filer)
        }
    }

    private fun logCompanions() {
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "Logging companion types.")
        companions.forEach({ env, m1 ->
            m1.forEach({ componentCN, m2 ->
                m2.forEach({ companionTN, companionCN ->
                    processingEnv.messager.printMessage(Diagnostic.Kind.NOTE,
                            "[$env] Companion of type '$companionTN' for component '$componentCN' is '$companionCN'")
                })
            })
        })
    }
}
