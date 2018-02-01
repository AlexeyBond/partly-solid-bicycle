package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.companion_creators

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Loader
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.impl.SingletonCompanionResolver
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ComponentConnector
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors.COMPANION_RESOLVER_FIELD_NAME
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors.isNodeClass
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.adaptor.CompanionTypeCreatorAdaptor
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class ConnectorCompanionCreator : CompanionTypeCreatorAdaptor() {
    private val COMPONENT_PARAM_NAME = "component"
    private val NODE_PARAM_NAME = "node"

    override fun getCompanionTypes(): MutableIterable<String> {
        return Collections.singleton("connector")
    }

    override fun generateCompanion(
            environment: String,
            processingEnvironment: ProcessingEnvironment,
            companionType: String,
            className: ClassName,
            componentClass: TypeElement): TypeSpec? {
        if (isNodeClass(componentClass, processingEnvironment)) {
            return null
        }

        val resolverField = FieldSpec.builder(
                ParameterizedTypeName.get(ClassName.get(CompanionResolver::class.java),
                        TypeName.get(componentClass.asType()),
                        ParameterizedTypeName.get(ClassName.get(Loader::class.java),
                                TypeName.get(componentClass.asType()))),
                COMPANION_RESOLVER_FIELD_NAME)
                .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                .initializer("new \$T(new \$T())",
                        SingletonCompanionResolver::class.java, className)
                .build()

        val connectCBB = CodeBlock.builder()
        val disconnectCBB = CodeBlock.builder()

        val connectMethod = MethodSpec.methodBuilder("onConnected")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override::class.java)
                .addParameter(ClassName.get(componentClass), COMPONENT_PARAM_NAME, Modifier.FINAL)
                .addParameter(LogicNode::class.java, NODE_PARAM_NAME, Modifier.FINAL)
                .addCode(connectCBB.build())
                .build()

        val disconnectMethod = MethodSpec.methodBuilder("onDisconnected")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override::class.java)
                .addParameter(ClassName.get(componentClass), COMPONENT_PARAM_NAME, Modifier.FINAL)
                .addParameter(LogicNode::class.java, NODE_PARAM_NAME, Modifier.FINAL)
                .addCode(disconnectCBB.build())
                .build()

        val classBuilder = TypeSpec.classBuilder(className)

        buildCode(processingEnvironment, classBuilder, connectCBB, disconnectCBB)

        return classBuilder
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(ComponentConnector::class.java), ClassName.get(componentClass)))
                .addField(resolverField)
                .addMethod(connectMethod)
                .addMethod(disconnectMethod)
                .build()
    }

    private fun buildCode(
            processingEnvironment: ProcessingEnvironment,
            classBuilder: TypeSpec.Builder,
            connectCodeBuilder: CodeBlock.Builder,
            disconnectCodeBuilder: CodeBlock.Builder
    ) {
        // TODO:: Implement
    }
}
