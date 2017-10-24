package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.impl;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DefaultIdSet<T> implements IdSet<T> {
    private final class DefaultId implements Id<T> {
        private final Object key;
        private final int hash;

        private DefaultId(Object key) {
            this.key = key;
            this.hash = key.hashCode();
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public String toString() {
            return "DefaultId" +
                    "@" + System.identityHashCode(this) +
                    "@" + System.identityHashCode(DefaultIdSet.this) +
                    "{" + key + "}";
        }
    }

    private final Map<Object, DefaultId> ids = new HashMap<Object, DefaultId>();

    @NotNull
    @Override
    public Id<T> get(Object key) {
        if (null == key) {
            return new DefaultId(new Object());
        }

        DefaultId id = ids.get(key);

        if (null == id) {
            ids.put(key, id = new DefaultId(key));
        }

        return id;
    }
}
