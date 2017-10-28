package io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson;

import com.badlogic.gdx.utils.ObjectMap;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

abstract class InputNode implements InputDataObject {
    void putField(String field, InputNode node) {
        throw new InvalidInputDataTypeException("Not a object node");
    }

    void addItem(InputNode node) {
        throw new InvalidInputDataTypeException("Not an array node");
    }

    @Override
    public InputDataObject getField(String field) throws InvalidInputDataTypeException {
        throw new InvalidInputDataTypeException("Not a object");
    }

    @Override
    public String getString() throws InvalidInputDataTypeException {
        throw new InvalidInputDataTypeException("Not a string");
    }

    @Override
    public boolean getBoolean() throws InvalidInputDataTypeException {
        throw new InvalidInputDataTypeException("Not a boolean");
    }

    @Override
    public double getDouble() throws InvalidInputDataTypeException {
        throw new InvalidInputDataTypeException("Not a double");
    }

    @Override
    public long getLong() throws InvalidInputDataTypeException {
        throw new InvalidInputDataTypeException("Not a long");
    }

    @Override
    public List<? extends InputDataObject> getList() throws InvalidInputDataTypeException {
        throw new InvalidInputDataTypeException("Not an array");
    }

    static final InputNode NULL = new InputNode() {
        @Override
        public void accept(@NotNull DataObjectVisitor visitor) {
            visitor.visitNull();
        }
    };

    private static class BooleanNode extends InputNode {
        private final boolean val;

        BooleanNode(boolean val) {
            this.val = val;
        }

        @Override
        public boolean getBoolean() throws InvalidInputDataTypeException {
            return val;
        }

        static final InputNode TRUE = new BooleanNode(true), FALSE = new BooleanNode(false);

        @Override
        public void accept(@NotNull DataObjectVisitor visitor) {
            visitor.visitValue(val);
        }
    }

    static class RootNode extends InputNode {
        private InputNode child = NULL;

        @Override
        void addItem(InputNode node) {
            child = node;
        }

        InputNode getChild() {
            return child;
        }

        @Override
        public void accept(@NotNull DataObjectVisitor visitor) {
            child.accept(visitor);
        }
    }

    static InputNode newObject() {
        return new InputNode() {
            private final ObjectMap<String, InputNode> map = new ObjectMap<String, InputNode>();

            @Override
            public void accept(@NotNull DataObjectVisitor visitor) {
                visitor.beginVisitObject();
                for (ObjectMap.Entry<String, InputNode> entry : map) {
                    visitor.visitField(entry.key, entry.value);
                }
                visitor.endVisitObject();
            }

            @Override
            void putField(String field, InputNode node) {
                map.put(field, node);
            }

            @Override
            public InputDataObject getField(String field) throws InvalidInputDataTypeException {
                return map.get(field, NULL);
            }
        };
    }

    static InputNode newArray() {
        return new InputNode() {
            @Override
            public void accept(@NotNull DataObjectVisitor visitor) {
                visitor.beginVisitArray();

                for (InputDataObject obj : getList())
                    visitor.visitItem(obj);

                visitor.endVisitArray();
            }

            private ArrayList<InputNode> list = new ArrayList<InputNode>(1);

            @Override
            void addItem(InputNode node) {
                list.add(node);
            }

            @Override
            public List<? extends InputDataObject> getList() {
                return list;
            }
        };
    }

    static InputNode newNumber(final double num) {
        return new InputNode() {
            @Override
            public void accept(@NotNull DataObjectVisitor visitor) {
                visitor.visitValue(num);
            }

            @Override
            public double getDouble() throws InvalidInputDataTypeException {
                return num;
            }

            @Override
            public long getLong() throws InvalidInputDataTypeException {
                return (long) num;
            }
        };
    }

    static InputNode newNumber(final long num) {
        return new InputNode() {
            @Override
            public void accept(@NotNull DataObjectVisitor visitor) {
                visitor.visitValue(num);
            }

            @Override
            public double getDouble() throws InvalidInputDataTypeException {
                return (double) num;
            }

            @Override
            public long getLong() throws InvalidInputDataTypeException {
                return num;
            }
        };
    }

    static InputNode newBoolean(boolean b) {
        return b ? BooleanNode.TRUE : BooleanNode.FALSE;
    }

    static InputNode newString(final String str) {
        return new InputNode() {
            @Override
            public void accept(@NotNull DataObjectVisitor visitor) {
                visitor.visitValue(str);
            }

            @Override
            public String getString() throws InvalidInputDataTypeException {
                return str;
            }
        };
    }
}
