package io.github.alexeybond.partly_solid_bicycle.core.modules;

import io.github.alexeybond.partly_solid_bicycle.core.impl.common.id.DefaultIdSet;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies.LazyNamedItems;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies.Singleton;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.CompositeDeclarativeNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.DeclarativeComponentNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.DeclarativeCustomNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.DeclarativeGroupNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.ClassCompanionResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.exceptions.StrategyException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.CompositeNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;

import java.util.Collection;
import java.util.Collections;

public class DeclarativeNodeFactories implements Module {
    @Override
    public void init(Collection<Object> env) {
        if (!env.contains("default")) {
            return;
        }

        // TODO: Move to another module initialized before this one
        // (kind: String) -> IdSet<LogicNode>
        IoC.register("id set for node kind", new Singleton(new DefaultIdSet<LogicNode>()));

        // (kind: String) -> CompositeNodeFactory
        IoC.register("node factory for node kind", new LazyNamedItems(new IoCStrategy() {
            @Override
            public Object resolve(Object... args) throws StrategyException {
                Object kind = args[0];

                CompositeNodeFactory<InputDataObject, String> factory
                        = new CompositeDeclarativeNodeFactory("class");

                IdSet<LogicNode> idSet = IoC.resolve("id set for node kind", kind);

                factory.put("group", new DeclarativeGroupNodeFactory(factory, "items"));

                return new Singleton(factory);
            }
        }));

        // (componentFactory: GenericFactory<T, InputDataObject>,
        //  companionResolver: ClassCompanionResolver<T>)
        //      -> NodeFactory<inputDataObject>
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

        // (componentFactory: GenericFactory<T extends LogicNode, InputDataObject>,
        //  companionResolver: ClassCompanionResolver<T>)
        //      -> NodeFactory<inputDataObject>
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

        // (factory: NodeFactory<InputDataObject>, type: String, kind: String)
        //      -> Void
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
