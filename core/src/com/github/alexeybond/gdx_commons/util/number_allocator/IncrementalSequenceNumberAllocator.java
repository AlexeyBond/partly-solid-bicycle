package com.github.alexeybond.gdx_commons.util.number_allocator;

public final class IncrementalSequenceNumberAllocator extends NamedNumberAllocator {
    private final int start, step, limit;

    /**
     * @param start first value (inclusive)
     * @param step  step, may be negative
     * @param limit maximum value (inclusive)
     */
    public IncrementalSequenceNumberAllocator(int start, int step, int limit) {
        this.start = start;
        this.step = step;
        this.limit = limit;
    }

    @Override
    protected int unused() {
        return start + (step > 0?-1:1);
    }

    @Override
    protected int first() {
        return start;
    }

    @Override
    protected int next(int last) {
        return last + step;
    }

    @Override
    protected void check(int value) {
        if ((step < 0 && value < limit) || (step > 0 && value > limit))
            throw new IllegalStateException();
    }
}
