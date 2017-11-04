package io.github.alexeybond.partly_solid_bicycle.core.impl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopologicalSort<TK, TV> {
    private static final int WHITE = 0;
    private static final int GRAY = 1;
    private static final int BLACK = 42;

    private class Node {
        Node(TV value) {
            this.value = value;
        }

        TV value;
        final ArrayList<Node> deps = new ArrayList<Node>();
        int mark = WHITE;

        void visit(List<TV> dst) {
            switch (mark) {
                case GRAY:
                    throw new IllegalStateException("Topology contains a loop.");
                case WHITE:
                    mark = GRAY;
                    for (int i = 0; i < deps.size(); i++) {
                        deps.get(i).visit(dst);
                    }
                    if (null != this.value) {
                        dst.add(this.value);
                    }
                    mark = BLACK;
                    return;
                default:
                    return;
            }
        }
    }

    private final Map<TK, Node> nodes = new HashMap<TK, Node>();

    public void node(TK key, TV value) {
        if (null == value) throw new IllegalArgumentException("Node value must not be null");

        Node node = nodes.get(key);

        if (null != node) {
            node.value = value;
        } else {
            nodes.put(key, new Node(value));
        }
    }

    public void edge(TK first, TK second) {
        Node fN = nodes.get(first), sN = nodes.get(second);
        if (null == fN) {
            nodes.put(first, fN = new Node(null));
        }
        if (null == sN) {
            nodes.put(second, sN = new Node(null));
        }

        sN.deps.add(fN);
    }

    public List<TV> ordered(List<TV> lst) {
        for (Map.Entry<TK, Node> entry : nodes.entrySet()) {
            entry.getValue().visit(lst);
        }

        return lst;
    }
}
