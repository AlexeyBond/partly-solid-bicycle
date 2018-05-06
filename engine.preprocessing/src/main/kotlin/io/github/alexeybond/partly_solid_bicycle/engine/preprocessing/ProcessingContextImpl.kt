package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.MutationAccumulator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class ProcessingContextImpl(
        private val processingEnvironment: ProcessingEnvironment,
        private val processors: List<ItemProcessor>
) : ProcessingContext {
    private val generatedClasses = HashMap<ClassName, MutationAccumulatorImpl<TypeSpec.Builder>>()
    private val processorsByKind = HashMap<String, List<ItemProcessor>>()
    private val items = HashMap<String, ItemContext>()

    override fun getEnv(): ProcessingEnvironment {
        return processingEnvironment
    }

    override fun addGenerated(className: ClassName, builder: TypeSpec.Builder)
            : MutationAccumulator<TypeSpec.Builder> {
        if (null != generatedClasses[className]) {
            processingEnvironment.messager.printMessage(
                    Diagnostic.Kind.WARNING,
                    "Generated class '$className' added twice"
            )
        }

        val ma = MutationAccumulatorImpl { builder.build().toBuilder() }

        generatedClasses[className] = ma

        return ma
    }

    private fun processorsForKind(kind: String): List<ItemProcessor> {
        return processorsByKind.getOrElse(kind, {
            val filtered = processors.filter { it.acceptsItemKind(kind) }
            processorsByKind[kind] = filtered
            filtered
        })
    }

    override fun processItem(item: ItemContext) {
        processorsForKind(item["kind"]).forEach {
            if (it.processItem(item)) return
        }
    }

    override fun newItem(id: String): ItemContext {
        if (id in items) throw IllegalArgumentException("Id '$id' already in use")

        val ctx = ItemContextImpl(this)
        items[id] = ctx
        ctx["_id_"] = id
        return ctx
    }

    override fun getItem(id: String): ItemContext {
        return items[id] ?: throw NoSuchElementException("Item: '$id'")
    }

    fun writeOutClasses() {
        generatedClasses.forEach { (cn, ma) ->
            JavaFile.builder(cn.packageName(), ma.applyAll().build())
                    .build()
                    .writeTo(processingEnvironment.filer)
        }
    }
}
