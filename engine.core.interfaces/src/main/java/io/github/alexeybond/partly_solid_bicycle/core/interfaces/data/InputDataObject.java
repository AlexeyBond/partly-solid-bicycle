package io.github.alexeybond.partly_solid_bicycle.core.interfaces.data;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.visitor.Visitable;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;

import java.util.List;

/**
 * Object representing node of DOM of read-only document.
 */
public interface InputDataObject extends Visitable<DataObjectVisitor> {
    /**
     * Get value of field of this object.
     *
     * @param field field name
     * @return value of the required field represented as {@link InputDataObject}
     * @throws InvalidInputDataTypeException if this object does not represent collection of objects with named keys
     * @throws UndefinedFieldException if required field is not set in this object
     */
    InputDataObject getField(String field) throws InvalidInputDataTypeException, UndefinedFieldException;

    /**
     * @return string value represented by this object
     * @throws InvalidInputDataTypeException if this object does not represent string value
     */
    String getString() throws InvalidInputDataTypeException;

    /**
     * @return boolean value represented by this object
     * @throws InvalidInputDataTypeException if this object does not represent boolean value
     */
    boolean getBoolean() throws InvalidInputDataTypeException;

    /**
     * @return double value represented by this object
     * @throws InvalidInputDataTypeException if this object does not represent double value
     */
    double getDouble() throws InvalidInputDataTypeException;

    /**
     * @return long value represented by this object
     * @throws InvalidInputDataTypeException if this object does not represent long value
     */
    long getLong() throws InvalidInputDataTypeException;

    /**
     * Get array value represented by this object.
     *
     * <p>
     *  Modification of contents of returned array will cause undefined behaviour of any future calls to this object.
     * </p>
     *
     * @return array value represented by this object
     * @throws InvalidInputDataTypeException if this object does not represent array value
     */
    List<? extends InputDataObject> getList() throws InvalidInputDataTypeException;

}
