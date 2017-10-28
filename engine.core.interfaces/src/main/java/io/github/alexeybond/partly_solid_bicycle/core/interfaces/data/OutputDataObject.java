package io.github.alexeybond.partly_solid_bicycle.core.interfaces.data;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.DuplicateFieldException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidOutputDataTypeException;

/**
 * Object representing a node of DOM of write-only document.
 *
 * <p>
 *  When a node is created (by {@link #addField(String)}, {@link #addItem()} call or by creating a root node)
 *  it has a null-type. The actual type is determined on first write access.
 * </p>
 */
public interface OutputDataObject {
    /**
     * Add named field to this node.
     *
     * @param field field name
     * @return node representing field value
     * @throws InvalidOutputDataTypeException if this node is not a collection of values with named keys
     * @throws DuplicateFieldException if field with this name was added to this object before
     */
    OutputDataObject addField(String field) throws InvalidOutputDataTypeException, DuplicateFieldException;

    /**
     * Add item to this node.
     *
     * @return node representing added value
     * @throws InvalidOutputDataTypeException if this node is not a collection of values without named keys (array)
     */
    OutputDataObject addItem() throws InvalidOutputDataTypeException;

    /**
     * Set this node to contain a string value.
     *
     * @param value the value
     * @throws InvalidOutputDataTypeException if this node is initialized with another type
     */
    void setString(String value) throws InvalidOutputDataTypeException;

    /**
     * Set this node to contain a double value.
     *
     * @param value the value
     * @throws InvalidOutputDataTypeException if this node is initialized with another type
     */
    void setDouble(double value) throws InvalidOutputDataTypeException;

    /**
     * Set this node to contain a long value.
     *
     * @param value the value
     * @throws InvalidOutputDataTypeException if this node is initialized with another type
     */
    void setLong(long value) throws InvalidOutputDataTypeException;

    /**
     * Set this node to contain a boolean value.
     *
     * @param value the value
     * @throws InvalidOutputDataTypeException if this node is initialized with another type
     */
    void setBoolean(boolean value) throws InvalidOutputDataTypeException;
}
