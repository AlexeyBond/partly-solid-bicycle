package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.*
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.metadata.Metadata
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

fun emitFieldProperty(
        componentContext: ItemContext,
        propertyName: String,
        baseMetadata: Metadata,
        propertyType: TypeMirror,
        declaringElements: List<Element>,
        metaModifications: List<String>
): Mutations<FieldSpec.Builder> {
    val componentClassName: ClassName = componentContext["className"]
    val metadata = MetadataImpl(componentContext.context.env, baseMetadata)

    metaModifications.forEach { metadata.addData(it) }

    val propertyInfo = SyntheticPropertyInfo(
            name = propertyName,
            type = propertyType,
            readable = true,
            writable = true,
            hasField = true,
            setter = null,
            getter = null,
            declaringElements = declaringElements,
            metadata = metadata
    )

    // Let loader/saver generators generate all the stuff
    componentContext.context.process("component-property:$componentClassName:${propertyInfo.name}") {
        set("kind", "component-property")
        set("info", propertyInfo)
        set("componentContext", componentContext)
    }

    val implMutations: Mutations<TypeSpec.Builder> = componentContext["implMutations"]

    val fieldMutations = MutationAccumulatorImpl<FieldSpec.Builder>({
        FieldSpec.builder(TypeName.get(propertyType), propertyName)
    })

    implMutations.add {
        addField(fieldMutations.applyAll().build())
    }

    return fieldMutations
}
