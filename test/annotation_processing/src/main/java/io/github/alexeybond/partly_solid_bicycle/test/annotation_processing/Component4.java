package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.impl.SingletonCompanionResolver;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.ComponentCompanion;

@Component(name = "component-4", kind = "any")
public class Component4 {
    @ComponentCompanion(component = Component4.class, companionType = "companion")
    public static class ACompanion implements Companion {
        public static CompanionResolver RESOLVER = new SingletonCompanionResolver(new ACompanion());
    }
}
