package io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils;

import io.github.alexeybond.partly_solid_bicycle.core.modules.ioc.MultiApplicationIoCHolderModule;
import io.github.alexeybond.partly_solid_bicycle.core.modules.ioc.ThreadSafeIoCContainerModule;

public class DefaultIntegrationTest extends IntegrationTestBase {
    {
        env("default");

        modules(
                new MultiApplicationIoCHolderModule(),
                new ThreadSafeIoCContainerModule()
        );
    }
}
