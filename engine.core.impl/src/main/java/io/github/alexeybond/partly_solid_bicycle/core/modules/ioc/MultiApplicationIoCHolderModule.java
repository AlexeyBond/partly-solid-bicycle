package io.github.alexeybond.partly_solid_bicycle.core.modules.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.BaseModule;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.MultiApplicationHolder;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;

import java.util.Collection;

public class MultiApplicationIoCHolderModule extends BaseModule {
    {
        provide("ioc_holder");
    }

    @Override
    public void init(Collection<Object> env) {
        try {
            IoC.use(new MultiApplicationHolder());
        } catch (IllegalStateException e) {
            // already initialized, OK
        }
    }

    @Override
    public void shutdown() {

    }
}
