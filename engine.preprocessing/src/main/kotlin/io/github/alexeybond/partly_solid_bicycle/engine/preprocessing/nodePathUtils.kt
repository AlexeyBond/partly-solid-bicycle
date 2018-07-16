package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath

val nodePathCCN = LogicNodePath::class.java.canonicalName
val nodePathCN = ClassName.get(LogicNodePath::class.java)!!

val nodePathParserCN = ClassName.get(
        "io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.path",
        "PathParser")!!
const val nodePathParserMethod = "parseString"

fun generatePathLiteral(path: String): CodeBlock {
    return CodeBlock.of("\$T.${nodePathParserMethod}(\$S)", nodePathParserCN, path)
}

fun generatePathParse(expression: String): CodeBlock {
    return CodeBlock.of("\$T.${nodePathParserMethod}($expression)", nodePathParserCN)
}
