package io.github.alexeybond.partly_solid_bicycle.core.impl.common.reference_counted;

import org.jetbrains.annotations.NotNull;

public abstract class RCPooledBase<T extends RCPooledBase<T>> extends RCBase {
    @NotNull
    private final RCPoolBase<T> pool;

    protected RCPooledBase(@NotNull RCPoolBase<T> pool) {
        this.pool = pool;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected void dispose() {
        super.dispose();

        pool.returnItem((T) this);
    }
}
