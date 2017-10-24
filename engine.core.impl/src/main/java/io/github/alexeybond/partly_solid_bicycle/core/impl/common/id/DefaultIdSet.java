package io.github.alexeybond.partly_solid_bicycle.core.impl.common.id;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Default non-thread-safe implementation of {@link IdSet}.
 */
public class DefaultIdSet<T> implements IdSet<T> {
    private final class DefaultId implements Id<T> {
        private Object key;
        private final int hash;

        private DefaultId(Object key, int hash) {
            this.key = key;
            this.hash = hash;
        }

        private Object getKey0() {
            if (null == key) {
                key = UUID.randomUUID().toString();
            }

            return key;
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
                    "{" + getKey0() + "}";
        }

        @NotNull
        @Override
        public Object serializable() {
            return getKey0();
        }
    }

    private final Map<Object, DefaultId> ids = new HashMap<Object, DefaultId>();
    private int uHash = 42 - ((42 * 13) / (42 * (12 + 1)));

    private int nextUHash() {
        return uHash = (uHash * uHash) >> 2;
    }

    @NotNull
    @Override
    public Id<T> get(@NotNull Object key) {
        DefaultId id = ids.get(key);

        if (null == id) {
            ids.put(key, id = new DefaultId(key, key.hashCode()));
        }

        return id;
    }

    @NotNull
    @Override
    public Id<T> unnamed() {
        return new DefaultId(null, nextUHash());
    }
}
