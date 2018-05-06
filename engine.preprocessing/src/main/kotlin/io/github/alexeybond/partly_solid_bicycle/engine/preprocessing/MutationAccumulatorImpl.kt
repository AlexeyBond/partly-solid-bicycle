package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.Mutation
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.MutationAccumulator

class MutationAccumulatorImpl<T>(
        val factory: () -> T
) : MutationAccumulator<T> {
    private val mutations = ArrayList<Mutation<T>>()

    override fun addMutation(mutation: Mutation<T>) {
        mutations.add(mutation)
    }

    fun applyAll(): T {
        val instance = factory()
        mutations.forEach { it.apply(instance) }
        return instance
    }
}
