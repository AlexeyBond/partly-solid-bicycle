package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import com.squareup.javapoet.ClassName
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode

val nodeCCN = LogicNode::class.java.canonicalName
val nodeCN = ClassName.get(LogicNode::class.java)

