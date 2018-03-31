package io.github.alexeybond.partly_solid_bicycle.core.modules.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.DefaultContainer;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import static io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.ModuleUtils.makeDependencyInfo;

public class NonThreadSafeIoCContainerModule implements Module {
    @Override
    public void init(Collection<Object> env) {
        IoC.use(new DefaultContainer(new HashMap<Object, IoCStrategy>()));
    }

    @Override
    public void shutdown() {

    }

    private static Iterable<Iterable<String>> dependencyInfo = makeDependencyInfo(
            Collections.singletonList("ioc"),
            Collections.singletonList("ioc_holder"),
            Collections.<String>emptyList());

    @Override
    public Iterable<Iterable<String>> dependencyInfo() {
        return dependencyInfo;
    }
}
