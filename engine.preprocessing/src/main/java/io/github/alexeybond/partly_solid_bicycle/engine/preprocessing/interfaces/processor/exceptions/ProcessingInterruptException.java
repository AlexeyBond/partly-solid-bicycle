package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.exceptions;

public class ProcessingInterruptException extends Exception {
    public static final ProcessingInterruptException INSTANCE = new ProcessingInterruptException();
}
