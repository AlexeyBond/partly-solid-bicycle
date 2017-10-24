package io.github.alexeybond.partly_solid_bicycle.core.impl.common.id;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultIdSetTest {
    private IdSet<?> idSet;

    @Before public void setUp() {
        idSet = new DefaultIdSet();
    }

    @Test public void shouldReturnSingleObjectForEqualKeys() {
        Id<?> a = idSet.get("key"), b = idSet.get(new String("key"));
        assertSame(a, b);
    }

    @Test public void shouldReturnKeyObjectAsSerializableIdRepresentation() {
        String key = "key";
        Id<?> id = idSet.get(key);
        assertSame(key, id.serializable());
    }

    @Test public void shouldCreateDistinctIdentifiersForNonEqualKeys() {
        String ka = "key", kb = "Key";
        Id<?> a = idSet.get(ka), b = idSet.get(kb);
        assertNotEquals(a, b);
        assertNotSame(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test public void shouldCreateDistinctUnnamedIds() {
        Id<?> a = idSet.unnamed(), b = idSet.unnamed();

        assertNotSame(a, b);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());

        assertNotNull(a.serializable());
        assertNotNull(b.serializable());
        assertNotEquals(a.serializable(), b.serializable());
    }

    @Test public void shouldMakeUnnamedIdentifierAccessibleByNameWhenSerializableRepresentationIsRead() {
        Id<?> id = idSet.unnamed();
        Object name = id.serializable();
        assertSame(id, idSet.get(name));
    }

    @Test public void idShouldHaveInformativeStringRepresentation() {
        Id<?> id = idSet.get("some-unique-key");
        String repr = id.toString();

        assertTrue(repr.contains("some-unique-key"));
    }
}
