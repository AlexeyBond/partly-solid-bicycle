package com.github.alexeybond.partly_solid_bicycle.util.number_allocator;

public final class PowerOfTwoSequenceNumberAllocator extends NamedNumberAllocator {
    private final int limit;

    public PowerOfTwoSequenceNumberAllocator(int limit) {
        this.limit = limit;
    }

    @Override
    protected int unused() {
        return -1;
    }

    @Override
    protected int first() {
        return 1;
    }

    @Override
    protected int next(int last) {
        return last << 1;
    }

    @Override
    protected void check(int value) {
        if (limit < value) throw new IllegalStateException();
    }
}
