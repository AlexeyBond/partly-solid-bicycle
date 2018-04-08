package io.github.alexeybond.partly_solid_bicycle.core.modules.application;

import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.BaseModule;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies.Singleton;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver.NullChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.GroupNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.SuperRootNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.NullPopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;

import java.util.Collection;

public class AppRootModule extends BaseModule {
    {
        depend("ioc");
        depend("node_id_sets");

        provide("application_root");
    }

    @Override
    public void init(Collection<Object> env) {
        IdSet<LogicNode> idSet = IoC.resolve("id set for node kind", "application");
        IoC.register("application root node", new Singleton(
                new SuperRootNode(
                        idSet,
                        new GroupNode(
                                NullChildResolver.INSTANCE,
                                NullPopulator.INSTANCE
                        )
                ).getRoot()
        ));
    }

    @Override
    public void shutdown() {

    }
}
