package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component

@Component(name = ["component-7"], kind = "any")
open class Component7 {
    lateinit var prop1: String

    lateinit var prop2: List<String>
}
