package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.IdentityNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.adapter.DataObjectVisitorAdapter;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DeclarativeChildResolverMaker {
    private final static class DataVisitor extends DataObjectVisitorAdapter {
        private final IdSet<LogicNode> idSet;
        private final Map<Id<LogicNode>, LogicNode> declarations;
        private final NodeFactory<InputDataObject> factory;

        private DataVisitor(
                IdSet<LogicNode> idSet,
                Map<Id<LogicNode>, LogicNode> declarations,
                NodeFactory<InputDataObject> factory) {
            this.idSet = idSet;
            this.declarations = declarations;
            this.factory = factory;
        }

        @Override
        public void beginVisitObject() {
        }

        @Override
        public void visitField(String field, InputDataObject value) {
            // It's ok to construct nodes right now as they will be actually
            // configured and initialized later
            declarations.put(idSet.get(field), factory.create(value));
        }

        @Override
        protected void visitAnyValue(Object value) {
            fail();
        }

        @Override
        protected void beginVisitAnyStructure() {
            fail();
        }

        private void fail() {
            throw new IllegalArgumentException("Illegal data type.");
        }
    }

    public static NodeChildResolver make(
            @NotNull NodeChildResolver next,
            @NotNull NodeFactory<InputDataObject> factory,
            @NotNull InputDataObject declaration,
            @NotNull IdSet<LogicNode> idSet
    ) {
        Map<Id<LogicNode>, LogicNode> map = new HashMap<Id<LogicNode>, LogicNode>();

        declaration.accept(new DataVisitor(idSet, map, factory));

        return new PredefinedChildListResolver<LogicNode>(
                next,
                map,
                IdentityNodeFactory.INSTANCE);
    }
}
