package io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils;

import io.github.alexeybond.partly_solid_bicycle.core.impl.common.id.DefaultIdSet;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson.GdxJsonDataReader;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver.NullChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.GroupNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.SuperRootNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.NullPopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;

import java.io.InputStream;

public enum TestUtils {
    ;

    public static InputDataObject parseJSON(String str) {
        return new GdxJsonDataReader().parseData(str);
    }

    public static InputDataObject parseJSON(Class<?> clz, String resource) {
        InputStream is = clz.getResourceAsStream(resource);

        try {
            return new GdxJsonDataReader().parseData(is);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static LogicNode createRoot() {
        return new SuperRootNode(
                new DefaultIdSet<LogicNode>(),
                new GroupNode(NullChildResolver.INSTANCE, NullPopulator.INSTANCE)
        ).getRoot();
    }
}
