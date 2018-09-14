package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.operations.PropertyOperationGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.PropertyInfo
import java.util.*

private val LV_COUNTER_MASK = 0xFFFFFFF;
private var lvCounter = 0

fun makeLocalName(prefix: String): String {
    // Avoid negative values when amount of local variables causes
    // lvCounter overflow (why not?)
    lvCounter = LV_COUNTER_MASK and (lvCounter + 1);
    return "$prefix${lvCounter}"
}

object RootOperationGenerator : PropertyOperationGenerator {
    private val generators by lazy {
        ServiceLoader.load(PropertyOperationGenerator::class.java, javaClass.classLoader).toList()
    }

    override fun generateOperation(
            operation: String,
            propertyInfo: PropertyInfo,
            processingContext: ProcessingContext,
            root: PropertyOperationGenerator,
            args: Map<String, Any>): CodeBlock? {
        generators.forEach {
            it.generateOperation(operation, propertyInfo, processingContext, root, args)?.let { return it }
        }

        return null
    }
}
