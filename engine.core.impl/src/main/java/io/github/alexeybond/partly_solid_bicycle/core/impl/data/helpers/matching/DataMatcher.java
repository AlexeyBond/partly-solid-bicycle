package io.github.alexeybond.partly_solid_bicycle.core.impl.data.helpers.matching;

import io.github.alexeybond.partly_solid_bicycle.core.impl.util.ExceptionAccumulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;

import java.util.List;

public class DataMatcher {
    private static AssertionError typeError(final InvalidInputDataTypeException e, final String path) {
        return new AssertionError("Data type mismatch at path '"
                + path
                + "' "
                + e.getMessage());
    }

    public static void assertMatch(
            final InputDataObject object,
            final InputDataObject pattern,
            final String basePath) {
        final Throwable[] acc = new Throwable[]{ExceptionAccumulator.init()};

        pattern.accept(new DataObjectVisitor() {
            @Override
            public void beginVisitObject() {

            }

            @Override
            public void visitField(String field, InputDataObject patternValue) {
                InputDataObject fieldValue;

                try {
                    fieldValue = object.getField(field);
                } catch (InvalidInputDataTypeException e) {
                    throw typeError(e, basePath);
                } catch (UndefinedFieldException e) {
                    acc[0] = ExceptionAccumulator.add(acc[0], new AssertionError(
                            "Missing field at path '" + basePath + "." + field + "'"
                    ));
                    return;
                }

                try {
                    assertMatch(fieldValue, patternValue, basePath + "." + field);
                } catch (AssertionError e) {
                    acc[0] = ExceptionAccumulator.add(acc[0], e);
                }
            }

            @Override
            public void endVisitObject() {

            }

            @Override
            public void beginVisitArray() {
                List<? extends InputDataObject> value;

                try {
                    value = object.getList();
                } catch (InvalidInputDataTypeException e) {
                    throw typeError(e, basePath);
                }

                List<? extends InputDataObject> patternValue = pattern.getList();

                if (value.size() != patternValue.size()) {
                    acc[0] = ExceptionAccumulator.add(acc[0], new AssertionError(
                            "Array length mismatch at path '"
                                    + basePath
                                    + "'; expected "
                                    + patternValue.size()
                                    + ", actual "
                                    + value.size()
                    ));
                }

                for (int i = 0; i < Math.min(patternValue.size(), value.size()); i++) {
                    try {
                        assertMatch(
                                value.get(i),
                                patternValue.get(i),
                                basePath + "[" + i + "]"
                        );
                    } catch (AssertionError e) {
                        acc[0] = ExceptionAccumulator.add(acc[0], e);
                    }
                }
            }

            @Override
            public void visitItem(InputDataObject object) {
                // already checked in beginVisitArray
            }

            @Override
            public void endVisitArray() {

            }

            @Override
            public void visitValue(double patternValue) {
                double value;

                try {
                    value = object.getDouble();
                } catch (InvalidInputDataTypeException e) {
                    throw typeError(e, basePath);
                }

                if (Math.abs(value - patternValue) > Double.MIN_VALUE) {
                    throw new AssertionError("Value mismatch at path '"
                            + basePath
                            + "' expected = "
                            + patternValue + ", actual = " + value);
                }
            }

            @Override
            public void visitValue(long patternValue) {
                long value;

                try {
                    value = object.getLong();
                } catch (InvalidInputDataTypeException e) {
                    throw typeError(e, basePath);
                }

                if (value != patternValue) {
                    throw new AssertionError("Value mismatch at path '"
                            + basePath
                            + "' expected = "
                            + patternValue + ", actual = " + value);
                }
            }

            @Override
            public void visitValue(boolean patternValue) {
                boolean value;

                try {
                    value = object.getBoolean();
                } catch (InvalidInputDataTypeException e) {
                    throw typeError(e, basePath);
                }

                if (value != patternValue) {
                    throw new AssertionError("Value mismatch at path '"
                            + basePath
                            + "' expected = "
                            + patternValue + ", actual = " + value);
                }
            }

            @Override
            public void visitValue(String patternValue) {
                String value;

                try {
                    value = object.getString();
                } catch (InvalidInputDataTypeException e) {
                    throw typeError(e, basePath);
                }

                if (!patternValue.equals(value)) {
                    throw new AssertionError("Value mismatch at path '"
                            + basePath
                            + "' expected = \""
                            + patternValue + "\", actual = \"" + value + "\"");
                }
            }

            @Override
            public void visitNull() {
                // TODO:: ??
            }
        });

        ExceptionAccumulator.<AssertionError>flush(acc[0]);
    }

    public static void assertMatch(
            final InputDataObject object,
            final InputDataObject pattern) {
        assertMatch(object, pattern, "root");
    }
}
