package io.github.alexeybond.partly_solid_bicycle.core.impl.util;

import java.io.PrintStream;

public class ExceptionAccumulator {
    private static class AccumulatedException extends RuntimeException {
        private final Throwable item1, item2;

        private AccumulatedException(Throwable item1, Throwable item2) {
            super(item1.getMessage() + "; " + item2.getMessage());
            this.item1 = item1;
            this.item2 = item2;
        }

        @Override
        public void printStackTrace(PrintStream s) {
            item1.printStackTrace(s);
            s.println("With suppressed:");
            item2.printStackTrace(s);
        }
    }

    public static Throwable init() {
        return null;
    }

    public static Throwable add(Throwable acc, Throwable err) {
        if (null == acc) {
            return err;
        }

        // suppressed exceptions are supported since Java 1.7 :(
        return new AccumulatedException(acc, err);
    }

    public static <T extends Throwable> void flush(Throwable acc) throws T {
        if (null == acc) return;
        throw (T) acc;
    }
}
