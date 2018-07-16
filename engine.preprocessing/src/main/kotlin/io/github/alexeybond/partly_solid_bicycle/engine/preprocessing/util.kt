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

fun <T> Mutations<T>.add(order: Comparable<Any>, mutation: T.() -> Unit) {
    this.addMutation(mutation, order)
}

fun <T> Mutations<T>.add(mutation: T.() -> Unit) {
    this.add(DEFAULT_ORDER, mutation)
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

fun escapeStringLiteral(value: String): String {
    // WARNING: Copy-paste from private part of JavaPoet
    fun characterLiteralWithoutSingleQuotes(c: Char): String {
        // see https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6
        when (c) {
            '\b' -> return "\\b" /* \u0008: backspace (BS) */
            '\t' -> return "\\t" /* \u0009: horizontal tab (HT) */
            '\n' -> return "\\n" /* \u000a: linefeed (LF) */
            '\u000c' -> return "\\f" /* \u000c: form feed (FF) */
            '\r' -> return "\\r" /* \u000d: carriage return (CR) */
            '\"' -> return "\""  /* \u0022: double quote (") */
            '\'' -> return "\\'" /* \u0027: single quote (') */
            '\\' -> return "\\\\"  /* \u005c: backslash (\) */
            else -> return if (Character.isISOControl(c)) String.format("\\u%04x", c.toInt()) else Character.toString(c)
        }
    }

    val result = StringBuilder(value.length + 2)
    for (i in 0 until value.length) {
        val c = value[i]
        // trivial case: single quote must not be escaped
        if (c == '\'') {
            result.append("'")
            continue
        }
        // trivial case: double quotes must be escaped
        if (c == '\"') {
            result.append("\\\"")
            continue
        }
        // default case: just let character literal do its work
        result.append(characterLiteralWithoutSingleQuotes(c))
    }
    return result.toString()
}

class LinearOrder(private val id: Int, opposite_: LinearOrder? = null) : Comparable<Any> {
    override fun compareTo(other: Any): Int {
        if (other !is LinearOrder) return 0

        return Integer.compare(id, other.id)
    }

    val opposite = opposite_ ?: LinearOrder(-id, this)
}

val DEFAULT_ORDER = LinearOrder(0)
