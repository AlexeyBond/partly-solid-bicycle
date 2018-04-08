package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.companion_creators

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.impl.SingletonCompanionResolver
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ComponentConnector
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeAttachmentListener
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors.COMPANION_RESOLVER_FIELD_NAME
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors.isNodeClass
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors.isSubclass
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.adaptor.CompanionTypeCreatorAdaptor
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class ConnectorCompanionCreator : CompanionTypeCreatorAdaptor() {
    private val COMPONENT_PARAM_NAME = "component"
    private val NODE_PARAM_NAME = "node"
    private val ID_PARAM_NAME = "id"

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
            processingEnvironment.messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    "Skipping creation of 'connector' companion for ${componentClass.qualifiedName.toString()}",
                    componentClass)
            return null
        }

        val resolverField = FieldSpec.builder(
                ParameterizedTypeName.get(ClassName.get(CompanionResolver::class.java),
                        TypeName.get(componentClass.asType()),
                        ParameterizedTypeName.get(ClassName.get(ComponentConnector::class.java),
                                TypeName.get(componentClass.asType()))),
                COMPANION_RESOLVER_FIELD_NAME)
                .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                .initializer("new \$T(new \$T())",
                        SingletonCompanionResolver::class.java, className)
                .build()

        val connectCBB = CodeBlock.builder()
        val disconnectCBB = CodeBlock.builder()
        val classBuilder = TypeSpec.classBuilder(className)

        buildCode(componentClass, processingEnvironment, classBuilder, connectCBB, disconnectCBB)

        val connectMethod = MethodSpec.methodBuilder("onConnected")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override::class.java)
                .addParameter(ClassName.get(componentClass), COMPONENT_PARAM_NAME, Modifier.FINAL)
                .addParameter(LogicNode::class.java, NODE_PARAM_NAME, Modifier.FINAL)
                .addParameter(ParameterizedTypeName.get(Id::class.java, LogicNode::class.java),
                        ID_PARAM_NAME, Modifier.FINAL)
                .addCode(connectCBB.build())
                .build()

        val disconnectMethod = MethodSpec.methodBuilder("onDisconnected")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override::class.java)
                .addParameter(ClassName.get(componentClass), COMPONENT_PARAM_NAME, Modifier.FINAL)
                .addParameter(LogicNode::class.java, NODE_PARAM_NAME, Modifier.FINAL)
                .addCode(disconnectCBB.build())
                .build()

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
            componentClass: TypeElement,
            processingEnvironment: ProcessingEnvironment,
            classBuilder: TypeSpec.Builder,
            connectCodeBuilder: CodeBlock.Builder,
            disconnectCodeBuilder: CodeBlock.Builder
    ) {
        // TODO:: Implement some more useful code generation e.g. references to nodes automatically filled on connect, methods called on events by subscriptions initialized on connect and removed on disconnect, etc.

        if (isSubclass(componentClass, NodeAttachmentListener::class.java, processingEnvironment)) {
            connectCodeBuilder.add(
                    "$COMPONENT_PARAM_NAME.onAttached($NODE_PARAM_NAME);\n")
            disconnectCodeBuilder.add(
                    "$COMPONENT_PARAM_NAME.onDetached($NODE_PARAM_NAME);\n")
        }
    }
}
