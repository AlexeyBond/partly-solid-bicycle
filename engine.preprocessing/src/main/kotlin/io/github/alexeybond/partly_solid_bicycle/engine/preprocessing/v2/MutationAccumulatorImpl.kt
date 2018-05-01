package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.Mutation
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.MutationAccumulator

class MutationAccumulatorImpl<T>(
        val instance: T
) : MutationAccumulator<T> {
    private val mutations = ArrayList<Mutation<T>>()

    override fun addMutation(mutation: Mutation<T>) {
        mutations.add(mutation)
    }

    fun applyAll(): T {
        mutations.forEach { it.apply(instance) }
        return instance
    }
}
