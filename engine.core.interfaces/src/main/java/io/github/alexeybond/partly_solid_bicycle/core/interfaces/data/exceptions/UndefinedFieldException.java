package io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions;

public class UndefinedFieldException extends RuntimeException {
    private final String fieldName;

    public UndefinedFieldException(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getMessage() {
        return "Field '" + fieldName + "' is not defined.";
    }
}
