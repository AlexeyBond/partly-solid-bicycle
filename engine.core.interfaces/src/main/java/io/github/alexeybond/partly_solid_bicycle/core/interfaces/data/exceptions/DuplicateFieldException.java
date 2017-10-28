package io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions;

public class DuplicateFieldException extends RuntimeException {
    private final String fieldName;

    public DuplicateFieldException(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getMessage() {
        return "Field '" + fieldName + "' is set twice.";
    }
}
