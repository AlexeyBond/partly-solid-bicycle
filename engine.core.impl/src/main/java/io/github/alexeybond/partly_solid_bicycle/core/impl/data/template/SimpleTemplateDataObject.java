package io.github.alexeybond.partly_solid_bicycle.core.impl.data.template;

import io.github.alexeybond.partly_solid_bicycle.core.impl.data.visitors.VisitorWrapper;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple template data object.
 * <p>
 * Substitutes values of fields of arguments object instead of some fields of template object.
 * </p>
 * <p>
 * <i>Note: This class doesn't implement any optimizations and is optimal only for cases when a
 * rendered template is being read just once.</i>
 * </p>
 * <h2>Example</h2>
 * Template object with the following template
 * <pre>
 * {
 *     "a": "b",
 *     "c": { "$set": "arg_c" },
 *     "d": {
 *         "d1": { "$set": "arg_d1" }
 *     },
 *     "e": [{ "$set": "e1"}]
 * }
 * </pre>
 * and the following arguments
 * <pre>
 * {
 *     "arg_c": "foo",
 *     "arg_d1": ["bar", 2],
 *     "e1": { "baz": 3.14 }
 * }
 * </pre>
 * will behave just like a object representing the following data
 * <pre>
 * {
 *     "a": "b",
 *     "c": "foo",
 *     "d": {
 *         "d1": ["bar", 2]
 *     },
 *     "e": [{"baz": 3.14}]
 * }
 * </pre>
 */
public class SimpleTemplateDataObject implements InputDataObject {
    @NotNull
    private final InputDataObject template, arguments;

    public SimpleTemplateDataObject(
            @NotNull InputDataObject template,
            @NotNull InputDataObject arguments
    ) {
        this.template = template;
        this.arguments = arguments;
    }

    @NotNull
    private InputDataObject mapTemplateValue(@NotNull final InputDataObject value) {
        InputDataObject argNameDO;

        try {
            argNameDO = value.getField("$set");
        } catch (UndefinedFieldException e) {
            return new SimpleTemplateDataObject(value, arguments);
        } catch (InvalidInputDataTypeException e) {
            return new SimpleTemplateDataObject(value, arguments);
        }

        String argName = argNameDO.getString();

        return arguments.getField(argName);
    }

    @Override
    public InputDataObject getField(String field)
            throws InvalidInputDataTypeException, UndefinedFieldException {
        return mapTemplateValue(template.getField(field));
    }

    @Override
    public String getString() throws InvalidInputDataTypeException {
        return template.getString();
    }

    @Override
    public boolean getBoolean() throws InvalidInputDataTypeException {
        return template.getBoolean();
    }

    @Override
    public double getDouble() throws InvalidInputDataTypeException {
        return template.getDouble();
    }

    @Override
    public long getLong() throws InvalidInputDataTypeException {
        return template.getLong();
    }

    @Override
    public List<? extends InputDataObject> getList() throws InvalidInputDataTypeException {
        List<? extends InputDataObject> srcList = template.getList();
        List<InputDataObject> dstList = new ArrayList<InputDataObject>(srcList.size());

        for (InputDataObject object : srcList) {
            dstList.add(mapTemplateValue(object));
        }

        return dstList;
    }

    @Override
    public void accept(@NotNull DataObjectVisitor visitor) {
        template.accept(new VisitorWrapper(visitor) {
            @Override
            public void visitField(String field, InputDataObject value) {
                super.visitField(field, mapTemplateValue(value));
            }

            @Override
            public void visitItem(InputDataObject object) {
                super.visitItem(mapTemplateValue(object));
            }
        });
    }
}
