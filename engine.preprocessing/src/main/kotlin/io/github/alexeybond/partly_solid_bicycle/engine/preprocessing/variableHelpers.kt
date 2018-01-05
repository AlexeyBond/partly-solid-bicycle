package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

private var generatedVarCounter: Int = 0

fun localVarName(marker: String): String {
    return "_${marker}_${generatedVarCounter++}"
}
