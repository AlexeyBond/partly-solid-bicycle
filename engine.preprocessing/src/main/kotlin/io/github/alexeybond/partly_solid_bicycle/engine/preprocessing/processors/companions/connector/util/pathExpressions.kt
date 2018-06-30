package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util

import java.util.regex.Pattern

/*
/path/to/node
@propertyName
@propertyName|/path/to/default/node
 */
val pathExpressionPattern: Pattern = Pattern.compile(
        "^(?:@(?<propertyName>[\\w&&[^\\d]][\\w]+)(?:\\||$))?(?<path>.+)?$"
)

val phaseExpressionPattern: Pattern = Pattern.compile(
        "^!(?<phaseName>[\\w]+)$"
)

data class PathExpression(
        val property: String?,
        val path: String?
)

fun parsePathExpression(expression: String): PathExpression? {
    val matcher = pathExpressionPattern.matcher(expression)

    if (!matcher.matches()) return null

    return PathExpression(
            matcher.group("propertyName"),
            matcher.group("path"))
}
