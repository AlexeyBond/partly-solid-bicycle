package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.Mutation
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.MutationAccumulator
import java.util.*

private class OrderedMutation<T>(
        val mutation: Mutation<T>,
        val order: Comparable<Any>
) : Mutation<T>, Comparable<OrderedMutation<T>> {
    override fun apply(instance: T) {
        mutation.apply(instance)
    }

    override fun compareTo(other: OrderedMutation<T>): Int {
        return order.compareTo(other.order)
    }
}

class MutationAccumulatorImpl<T>(
        val factory: () -> T
) : MutationAccumulator<T> {
    private val mutations = PriorityQueue<OrderedMutation<T>>()

    override fun addMutation(mutation: Mutation<T>, order: Comparable<Any>) {
        mutations.add(OrderedMutation(mutation, order))
    }

    fun applyAll(): T {
        val instance = factory()
        mutations.forEach { it.apply(instance) }
        return instance
    }
}
