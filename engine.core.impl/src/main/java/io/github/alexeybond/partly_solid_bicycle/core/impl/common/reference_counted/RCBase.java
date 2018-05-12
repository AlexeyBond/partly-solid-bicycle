package io.github.alexeybond.partly_solid_bicycle.core.impl.common.reference_counted;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.reference_counted.ReferenceCounted;

public abstract class RCBase implements ReferenceCounted {
    private int counter = 0;

    @Override
    public final void acquire() {
        int cnt = counter;

        if (cnt < 0) {
            throw new IllegalStateException("reference_count=" + cnt + " < 0; object already disposed");
        }
    }

    @Override
    public final void release() {
        int cnt = counter;

        if (cnt <= 0) {
            throw new IllegalStateException("reference_count=" + cnt + " <= 0");
        }

        if (cnt == 1) {
            counter = -1;
            dispose();
        } else {
            counter = cnt - 1;
        }
    }

    public void resetRC() {
        counter = 0;
    }

    protected void dispose() {

    }
}
