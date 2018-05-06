package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.Mutation
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.MutationAccumulator
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

fun ProcessingContext.process(id: String, cb: ItemContext.() -> Unit): ItemContext {
    val itemContext = newItem(id)
    cb(itemContext)
    processItem(itemContext)
    return itemContext
}

typealias Mutations<T> = MutationAccumulator<T>

fun <T> Mutations<T>.add(mutation: T.() -> Unit) {
    this.addMutation(mutation)
}

fun <T> T.applyMutation(mutation: Mutation<T>): T {
    mutation.apply(this)
    return this
}

fun <T> T.applyMutation(mutation: T.() -> Unit): T {
    mutation(this)
    return this
}

fun isSubclass(type: TypeElement, clazz: Class<*>, pe: ProcessingEnvironment): Boolean {
    return pe.typeUtils.isSubtype(
            type.asType(),
            pe.elementUtils.getTypeElement(clazz.canonicalName).asType());
}
