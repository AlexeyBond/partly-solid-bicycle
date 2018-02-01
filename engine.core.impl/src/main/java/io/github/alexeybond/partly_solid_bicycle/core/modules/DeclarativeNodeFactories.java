package io.github.alexeybond.partly_solid_bicycle.core.modules;

import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies.LazyNamedItems;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies.Singleton;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.CompositeDeclarativeNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.DeclarativeComponentNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.DeclarativeCustomNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.ClassCompanionResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.CompositeNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;

import java.util.Collection;
import java.util.Collections;

public class DeclarativeNodeFactories implements Module {
    @Override
    public void init(Collection<Object> env) {
        if (!env.contains("default")) {
            return;
        }

        IoC.register("node factory for node kind", new LazyNamedItems(new IoCStrategy() {
            @Override
            public Object resolve(Object... args) throws StrategyException {
                return new Singleton(new CompositeDeclarativeNodeFactory("class"));
            }
        }));

        IoC.register("create component node factory", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) throws StrategyException {
                GenericFactory factory = (GenericFactory) args[0];
                ClassCompanionResolver companionResolver = (ClassCompanionResolver) args[1];
                return new DeclarativeComponentNodeFactory(
                        factory,
                        companionResolver.resolve("loader"),
                        companionResolver.resolve("connector"));
            }
        });

        IoC.register("create custom node factory", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) throws StrategyException {
                GenericFactory factory = (GenericFactory) args[0];
                ClassCompanionResolver companionResolver = (ClassCompanionResolver) args[1];
                return new DeclarativeCustomNodeFactory(
                        factory,
                        companionResolver.resolve("loader"));
            }
        });

        IoC.register("register node factory", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) throws StrategyException {
                NodeFactory factory = (NodeFactory) args[0];
                Object typeKey = args[1];
                Object kindKey = args[2];

                CompositeNodeFactory compositeFactory = IoC.resolve("node factory for node kind", kindKey);

                compositeFactory.put(typeKey, factory);

                return null;
            }
        });
    }

    @Override
    public void shutdown() {

    }

    @Override
    public Iterable<Iterable<String>> dependencyInfo() {
        return Collections.emptyList();
    }
}
