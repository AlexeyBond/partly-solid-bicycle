package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.Component4$_impl;
import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.Component8$_impl;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.DefaultContainer;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.MultiApplicationHolder;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.modules.DeclarativeNodeFactories;
import io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.test_utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ModuleTest {
    private NodeFactory<InputDataObject> rootFactory;

    @Before
    public void setUp() {
        try {
            IoC.use(new MultiApplicationHolder());
        } catch (IllegalStateException e) { /* ok */ }

        IoC.use(new DefaultContainer(new HashMap<Object, IoCStrategy>()));

        new DeclarativeNodeFactories().init(Collections.<Object>singletonList("default"));

        rootFactory = IoC.resolve("node factory for node kind", "any");
    }

    @Test
    public void doTestInstantiation() {
        new Module1();
    }

    @Test
    public void doTestSetupCompanions() {
        new Module1().init(Arrays.<Object>asList("default", "special1"));
        Companion companion = new Component4$_impl().getCompanionObject("companion");

        assertTrue(companion instanceof Component4.ASpecialCompanion1);

        new Module1().init(Arrays.<Object>asList("default", "special2"));
        companion = new Component4$_impl().getCompanionObject("companion");

        assertTrue(companion instanceof Component4.ASpecialCompanion2);

        new Module1().init(Collections.<Object>singletonList("default"));
        companion = new Component4$_impl().getCompanionObject("companion");

        assertTrue(companion instanceof Component4.ACompanion);
    }

    @Test
    public void doTestRegisterComponentsGlobally() {
        new Module1().init(Collections.<Object>singletonList("default"));

        LogicNode node = rootFactory.create(TestUtils
                .parseJSON("{class:component-8, field: Hello!}"));

        assertTrue(node.getComponent() instanceof Component8);
        assertTrue(node.getComponent() instanceof Component8$_impl);

        assertEquals("Hello!", node.<Component8>getComponent().field);
    }
}
