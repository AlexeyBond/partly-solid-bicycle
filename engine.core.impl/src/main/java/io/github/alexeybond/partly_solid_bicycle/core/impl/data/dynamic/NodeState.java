package io.github.alexeybond.partly_solid_bicycle.core.impl.data.dynamic;

import com.badlogic.gdx.utils.ObjectMap;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.DuplicateFieldException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidOutputDataTypeException;

import java.util.ArrayList;
import java.util.List;

abstract class NodeState {
    abstract String stateType();

    NodeState init(DynamicNode node) {
        node.state = this;
        return this;
    }

    private <T> T failRead(String type) {
        throw new InvalidInputDataTypeException(
                "Data node of type '" + stateType() + "' cannot be represented as " + type + ".");
    }

    private <T> T failWrite(String type) {
        throw new InvalidOutputDataTypeException(
                "Data node of type '" + stateType() + "' cannot be written as " + type + ".");
    }

    DynamicNode getFld(DynamicNode node, String name) {
        return failRead("object");
    }

    long getLong(DynamicNode node) {
        return failRead("long");
    }

    double getDouble(DynamicNode node) {
        return failRead("double");
    }

    List<? extends DynamicNode> getArray(DynamicNode node) {
        return failRead("array");
    }

    boolean getBoolean(DynamicNode node) {
        return failRead("boolean");
    }

    String getString(DynamicNode node) {
        return failRead("string");
    }

    DynamicNode addFld(DynamicNode node, String field) {
        return failWrite("object");
    }

    DynamicNode addItem(DynamicNode node) {
        return failWrite("array");
    }

    void setString(DynamicNode node, String value) {
        failWrite("string");
    }

    void setBool(DynamicNode node, boolean value) {
        failWrite("boolean");
    }

    void setLong(DynamicNode node, long value) {
        failWrite("long");
    }

    void setDouble(DynamicNode node, double value) {
        failWrite("double");
    }

    public abstract void acceptVisitor(DynamicNode node, DataObjectVisitor visitor);

    static final NodeState NULL = new NodeState() {
        @Override
        NodeState init(DynamicNode node) {
            node.mObject = null;
            return super.init(node);
        }

        @Override
        String stateType() {
            return "null";
        }

        @Override
        DynamicNode addFld(DynamicNode node, String field) {
            return OBJECT.init(node).addFld(node, field);
        }

        @Override
        DynamicNode addItem(DynamicNode node) {
            return ARRAY.init(node).addItem(node);
        }

        @Override
        void setBool(DynamicNode node, boolean value) {
            BOOL.init(node).setBool(node, value);
        }

        @Override
        void setString(DynamicNode node, String value) {
            STRING.init(node).setString(node, value);
        }

        @Override
        void setDouble(DynamicNode node, double value) {
            DOUBLE.init(node).setDouble(node, value);
        }

        @Override
        public void acceptVisitor(DynamicNode node, DataObjectVisitor visitor) {
            visitor.visitNull();
        }

        @Override
        void setLong(DynamicNode node, long value) {
            LONG.init(node).setLong(node, value);
        }
    };

    private static final NodeState OBJECT = new NodeState() {
        @Override
        String stateType() {
            return "object";
        }

        @Override
        NodeState init(DynamicNode node) {
            node.mObject = new ObjectMap();
            return super.init(node);
        }

        @Override
        DynamicNode getFld(DynamicNode node, String name) {
            return (DynamicNode) ((ObjectMap) node.mObject).get(name);
        }

        @Override
        DynamicNode addFld(DynamicNode node, String field) {
            DynamicNode fNode = new DynamicNode();
            ObjectMap map = ((ObjectMap) node.mObject);
            if (map.containsKey(field)) {
                throw new DuplicateFieldException(field);
            }
            map.put(field, fNode);
            return fNode;
        }

        @Override
        public void acceptVisitor(DynamicNode node, DataObjectVisitor visitor) {
            visitor.beginVisitObject();

            for (ObjectMap.Entry<String, DynamicNode> entry
                    : ((ObjectMap<String, DynamicNode>) node.mObject)) {
                visitor.visitField(entry.key, entry.value);
            }

            visitor.endVisitObject();
        }
    };

    private static final NodeState ARRAY = new NodeState() {
        @Override
        String stateType() {
            return "array";
        }

        @Override
        NodeState init(DynamicNode node) {
            node.mObject = new ArrayList();
            return super.init(node);
        }

        @Override
        List<? extends DynamicNode> getArray(DynamicNode node) {
            return (List<? extends DynamicNode>) node.mObject;
        }

        @Override
        DynamicNode addItem(DynamicNode node) {
            DynamicNode iNode = new DynamicNode();
            ((List) node.mObject).add(iNode);
            return iNode;
        }

        @Override
        public void acceptVisitor(DynamicNode node, DataObjectVisitor visitor) {
            visitor.beginVisitArray();

            ArrayList list = (ArrayList) node.mObject;

            for (int i = 0; i < list.size(); i++) {
                visitor.visitItem((InputDataObject) list.get(i));
            }

            visitor.endVisitArray();
        }
    };

    private static final NodeState STRING = new NodeState() {
        @Override
        String stateType() {
            return "string";
        }

        @Override
        String getString(DynamicNode node) {
            return (String) node.mObject;
        }

        @Override
        void setString(DynamicNode node, String value) {
            node.mObject = value;
        }

        @Override
        public void acceptVisitor(DynamicNode node, DataObjectVisitor visitor) {
            visitor.visitValue(getString(node));
        }
    };

    private static final NodeState BOOL = new NodeState() {
        @Override
        String stateType() {
            return "boolean";
        }

        @Override
        boolean getBoolean(DynamicNode node) {
            return node.mObject == Boolean.TRUE;
        }

        @Override
        void setBool(DynamicNode node, boolean value) {
            node.mObject = value;
        }

        @Override
        public void acceptVisitor(DynamicNode node, DataObjectVisitor visitor) {
            visitor.visitValue(getBoolean(node));
        }
    };

    private static final NodeState DOUBLE = new NodeState() {
        @Override
        String stateType() {
            return "double";
        }

        @Override
        void setLong(DynamicNode node, long value) {
            NULL.setLong(node, value);
        }

        @Override
        void setDouble(DynamicNode node, double value) {
            node.mLong = Double.doubleToLongBits(value);
        }

        @Override
        public void acceptVisitor(DynamicNode node, DataObjectVisitor visitor) {
            visitor.visitValue(getDouble(node));
        }

        @Override
        double getDouble(DynamicNode node) {
            return Double.longBitsToDouble(node.mLong);
        }

        @Override
        long getLong(DynamicNode node) {
            return (long) getDouble(node);
        }
    };

    private static final NodeState LONG = new NodeState() {
        @Override
        String stateType() {
            return "long";
        }

        @Override
        void setDouble(DynamicNode node, double value) {
            NULL.setDouble(node, value);
        }

        @Override
        public void acceptVisitor(DynamicNode node, DataObjectVisitor visitor) {
            visitor.visitValue(getLong(node));
        }

        @Override
        void setLong(DynamicNode node, long value) {
            node.mLong = value;
        }

        @Override
        long getLong(DynamicNode node) {
            return node.mLong;
        }

        @Override
        double getDouble(DynamicNode node) {
            return (double) getLong(node);
        }
    };
}
