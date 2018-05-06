package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.component

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import org.jetbrains.annotations.NotNull
import javax.lang.model.element.Modifier

class ComponentFactoryInit : ItemProcessor {
    private val FACTORY_FIELD = "FACTORY"
    private val FACTORY_GETTER = "getFactory"

    override fun getPriority(): Int {
        return 0
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val classMutations: Mutations<TypeSpec.Builder> = context["implMutations"]
        val className: ClassName = context["implClassName"]

        classMutations.add {
            val factoryType = ParameterizedTypeName.get(
                    ClassName.get(GenericFactory::class.java),
                    className, ClassName.VOID.box())

            addField(FieldSpec
                    .builder(
                            factoryType, FACTORY_FIELD,
                            Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .addAnnotation(NotNull::class.java)
                    .build())

            addMethod(MethodSpec
                    .methodBuilder(FACTORY_GETTER)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addAnnotation(NotNull::class.java)
                    .returns(factoryType)
                    .addCode("return $FACTORY_FIELD;\n")
                    .build())

            val T = "\$T"

            addStaticBlock(CodeBlock.of("""
                $FACTORY_FIELD = new $T() {
                    @$T
                    @Override
                    public $T create(Void v0id) {
                        return new $T();
                    }
                };

            """.trimIndent(),
                    factoryType, NotNull::class.java, className, className))
        }
    }
}
