package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.Component4$$_companionOwner;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class ModuleTest {
    @Test
    public void doTestInstantiation() {
        new Module1();
    }

    @Test
    public void doTestSetupCompanions() {
        new Module1().init(Arrays.<Object>asList("default", "special1"));
        Companion companion = new Component4$$_companionOwner().getCompanionObject("companion");

        assertTrue(companion instanceof Component4.ASpecialCompanion1);

        new Module1().init(Arrays.<Object>asList("default", "special2"));
        companion = new Component4$$_companionOwner().getCompanionObject("companion");

        assertTrue(companion instanceof Component4.ASpecialCompanion2);

        new Module1().init(Collections.<Object>singletonList("default"));
        companion = new Component4$$_companionOwner().getCompanionObject("companion");

        assertTrue(companion instanceof Component4.ACompanion);
    }
}
