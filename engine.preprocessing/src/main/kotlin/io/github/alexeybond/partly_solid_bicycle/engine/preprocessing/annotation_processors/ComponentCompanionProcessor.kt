package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionOwner
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.MutableClassCompanionResolver
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.ComponentCompanion
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.GeneratedModule
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.CompanionTypeCreator
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ComponentCompanionProcessor : AbstractProcessor() {
    private val DEPENDENCY_INFO_FIELD_NAME = "_dependencyInfo";

    // companionCreators[env][type] = creator
    private val companionCreators: HashMap<String, HashMap<String, CompanionTypeCreator>> = HashMap()

    // companions[env][component_class][companion_type] = companion_class
    private val companions = HashMap<String, HashMap<TypeName, HashMap<String, TypeName>>>()

    // moduleComponents[module_class][env][kind][type] = component
    private val moduleComponents
            = HashMap<TypeName, HashMap<String, HashMap<String, HashMap<String, TypeName>>>>()

    // modules that have useAsDefault=true
    private val defaultModules = ArrayList<TypeName>()

    private lateinit var tu: Types
    private lateinit var eu: Elements

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
    }

    override fun process(
            annotations: MutableSet<out TypeElement>?,
            roundEnv: RoundEnvironment?
    ): Boolean {
        roundEnv!!
        cleanup()

        discoverModules(roundEnv)
        discoverComponents(roundEnv)
        discoverPredefinedCompanions(roundEnv)

        generateCompanions()
//        logCompanions()

        generateCompanionOwners()

        generateModules()

        return false
    }

    private fun cleanup() {
        components.clear()
        companions.clear()
        defaultModules.clear()
        moduleComponents.clear()
    }

    private fun discoverModules(roundEnv: RoundEnvironment) {
        roundEnv.getElementsAnnotatedWith(GeneratedModule::class.java).forEach { moduleElem ->
            moduleElem as TypeElement
            val moduleName = ClassName.get(moduleElem)
            val annotation = moduleElem.getAnnotationMirror(processingEnv, GeneratedModule::class)!!

            if (moduleElem.superclass.kind != TypeKind.ERROR) {
                processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                        "Generated module class must extend a class " +
                                "not present in project sources or dependencies.",
                        moduleElem)
            }

            if (annotation.getValue(eu, "useAsDefault").value as Boolean) {
                defaultModules.add(moduleName)
            }

            moduleComponents[moduleName] = HashMap()
        }
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
            val componentCN = ClassName.get(componentType)
            val annotation = componentType.getAnnotationMirror(processingEnv, Component::class)!!

            val kind = annotation.getValue(eu, "kind").value as String
            val envs = annotation.getValue(eu, "env").getListValue<String>()
            val mods = annotation.getValue(eu, "modules").getListValue<TypeMirror>()
            val names = annotation.getValue(eu, "name").getListValue<String>()

            val moduleList: Iterable<TypeName> = if (mods.isNotEmpty()) {
                mods.map { mm -> ClassName.get(mm) }
            } else {
                if (defaultModules.size == 0) {
                    processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                            "No module defined for this component " +
                                    "and no default module is present in project.",
                            componentType)
                }
                defaultModules
            }

            moduleList.forEach forModule@ { modName ->
                val envMap = moduleComponents[modName]

                if (null == envMap) {
                    processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                            "Module class is not annotated as generated module but is required by " +
                                    "a component class.",
                            componentType)
                    return@forModule
                }

                envs.forEach { env ->
                    names.forEach { name ->
                        envMap.computeIfAbsent(env, { HashMap() })
                                .computeIfAbsent(kind, { HashMap() })
                                .put(name, componentCN)
                    }
                }
            }

            components.add(componentType)
        }
    }

    private fun discoverPredefinedCompanions(roundEnv: RoundEnvironment) {
        roundEnv.getElementsAnnotatedWith(ComponentCompanion::class.java).forEach { companionClass ->
            companionClass as TypeElement
            val companionCN = ClassName.get(companionClass.asType())

            val annotation = companionClass
                    .getAnnotationMirror(processingEnv, ComponentCompanion::class)!!

            val componentClassMirror = annotation.getValue(eu, "component").value as TypeMirror

            if (null == eu.getTypeElement(componentClassMirror.toString())
                    .getAnnotationMirror(processingEnv, Component::class)) {
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
        components.forEach { componentType ->
            val className = ClassName.get(componentType)

            val companionOwnerName = companionOwnerClassName(className)

            val companionsFieldSpec = FieldSpec.builder(
                    MutableClassCompanionResolver::class.java,
                    CLASS_COMPANION_RESOLVER_FIELD_NAME,
                    Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    // TODO:: Avoid implicit dependency on implementation class
                    .initializer("new io.github.alexeybond.partly_solid_bicycle.core.impl.common.companions.MutableClassCompanionResolverImpl()")
                    .build()

            val getCompanionMethodSpec = MethodSpec.methodBuilder("getCompanionObject")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(ClassName.get(Companion::class.java))
                    .addParameter(ClassName.get(String::class.java), "name")
                    .addCode("return $CLASS_COMPANION_RESOLVER_FIELD_NAME.resolve(name).resolve(this);\n")
                    .build()

            val typeSpec = TypeSpec.classBuilder(companionOwnerName)
                    .superclass(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(
                            ParameterizedTypeName.get(
                                    ClassName.get(CompanionOwner::class.java),
                                    className
                            ))
                    .addField(companionsFieldSpec)
                    .addMethod(getCompanionMethodSpec)
                    .build()

            JavaFile.builder(companionOwnerName.packageName(), typeSpec).build()
                    .writeTo(processingEnv.filer)
        }
    }

    private fun generateModules() {
        moduleComponents.forEach { (moduleCN, envMap) ->
            val allComponents = envMap.values
                    .flatMap { m -> m.values.flatMap { p -> p.values } }
                    .toHashSet()
            val allEnvs = (
                    envMap.keys + companions.filterValues { ccMap ->
                        !ccMap.keys.intersect(allComponents).isEmpty()
                    }.keys).toHashSet()

            val modElem = eu.getTypeElement(moduleCN.toString())

            val implCN = ClassName.get(
                    ClassName.get(modElem).packageName(),
                    (modElem.superclass as DeclaredType).asElement().simpleName.toString()
            )

            var initCodeBuilder = CodeBlock.builder()

            allEnvs.forEach { env ->
                initCodeBuilder = initCodeBuilder.add(
                        "INITIALIZERS.put(\$S, new \$T() {\n",
                        env, Runnable::class.java)
                        .add("@Override\n")
                        .add("public void run() {\n")

                companions[env]
                        ?.filterKeys { c -> allComponents.contains(c) }
                        ?.forEach { (componentCN, companionMap) ->
                            val companionOwnerCN = companionOwnerClassName(componentCN as ClassName)
                            companionMap.forEach { (cType, cClass) ->
                                initCodeBuilder = initCodeBuilder.add(
                                        "\$T.$CLASS_COMPANION_RESOLVER_FIELD_NAME.register(\$S, \$T.$COMPANION_RESOLVER_FIELD_NAME);\n",
                                        companionOwnerCN, cType, cClass)
                            }
                        }

                moduleComponents[moduleCN]
                        ?.get(env)
                        ?.forEach { (kind, typeMap) ->
                            initCodeBuilder = initCodeBuilder.add(
                                    "// begin kind \$S\n",
                                    kind)
                            typeMap.forEach { (type, cls) ->
                                initCodeBuilder = initCodeBuilder.add(
                                        "// register \$T as \$S\n",
                                        cls, type
                                )
                            }
                        }

                initCodeBuilder = initCodeBuilder.add(
                        "}\n")
                        .add(
                                "});\n")
            }

            initCodeBuilder = buildDependencyInfoInitializer(modElem, initCodeBuilder, processingEnv)

            val initCode = initCodeBuilder.build()

            val initializersMapFieldSpec = FieldSpec
                    .builder(
                            ParameterizedTypeName.get(Map::class.java, Object::class.java, Runnable::class.java),
                            "INITIALIZERS",
                            Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                    .initializer("new \$T()", HashMap::class.java)
                    .build()

            val initMethodSpec = MethodSpec
                    .methodBuilder("init")
                    .addParameter(ParameterizedTypeName.get(
                            Iterable::class.java, Object::class.java), "envs")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addAnnotation(Override::class.java)
                    .beginControlFlow("for (\$T env : envs)",
                            Object::class.java)
                    .addCode("\$T i = INITIALIZERS.get(env);\n",
                            Runnable::class.java)
                    .addCode("if (null == i) continue;\n")
                    .addCode("i.run();\n")
                    .endControlFlow()
                    .build()

            val shutdownMethodSpec = MethodSpec
                    .methodBuilder("shutdown")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addAnnotation(Override::class.java)
                    .build()

            val dependencyInfoMethodSpec = MethodSpec
                    .methodBuilder("dependencyInfo")
                    .returns(ParameterizedTypeName.get(ClassName.get(Iterable::class.java),
                            ParameterizedTypeName.get(ClassName.get(Iterable::class.java),
                                    ClassName.get(String::class.java))))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addAnnotation(Override::class.java)
                    .addCode("return $DEPENDENCY_INFO_FIELD_NAME;\n")
                    .build()

            val dependencyInfoFieldSpec = FieldSpec
                    .builder(
                            ParameterizedTypeName.get(ClassName.get(Iterable::class.java),
                                    ParameterizedTypeName.get(ClassName.get(Iterable::class.java),
                                            ClassName.get(String::class.java))),
                            DEPENDENCY_INFO_FIELD_NAME,
                            Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL
                    )
                    .build();

            val typeSpec = TypeSpec.classBuilder(implCN)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addSuperinterface(
                            io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module::class.java)
                    .addField(initializersMapFieldSpec)
                    .addField(dependencyInfoFieldSpec)
                    .addStaticBlock(initCode)
                    .addMethod(initMethodSpec)
                    .addMethod(shutdownMethodSpec)
                    .addMethod(dependencyInfoMethodSpec)
                    .build()

            JavaFile.builder(implCN.packageName(), typeSpec).build()
                    .writeTo(processingEnv.filer)
        }
    }

    private fun buildDependencyInfoInitializer(
            module: TypeElement,
            cb: CodeBlock.Builder,
            processingEnv: ProcessingEnvironment): CodeBlock.Builder {
        // TODO:: Gather dependency info from module annotations...
        return cb.add("$DEPENDENCY_INFO_FIELD_NAME = \$T.emptyList();\n", Collections::class.java)
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
