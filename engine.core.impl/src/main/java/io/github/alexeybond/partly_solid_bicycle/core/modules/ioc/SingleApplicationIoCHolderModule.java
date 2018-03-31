package io.github.alexeybond.partly_solid_bicycle.core.modules.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.SingleApplicationHolder;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;

import java.util.Collection;
import java.util.Collections;

import static io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.ModuleUtils.makeDependencyInfo;

public class SingleApplicationIoCHolderModule implements Module {
    @Override
    public void init(Collection<Object> env) {
        IoC.use(new SingleApplicationHolder());
    }

    @Override
    public void shutdown() {

    }

    private static Iterable<Iterable<String>> dependencyInfo = makeDependencyInfo(
            Collections.singletonList("ioc_holder"),
            Collections.<String>emptyList(),
            Collections.<String>emptyList());

    @Override
    public Iterable<Iterable<String>> dependencyInfo() {
        return dependencyInfo;
    }
}
