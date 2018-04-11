package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.adapter.DataObjectVisitorAdapter;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodePopulator;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

/**
 * Populates a node using declarative children description and a fixed factory.
 * <p>
 * Is best for cases when a declaration is used only once.
 * For cases when a declaration is reused it's better to cache id's/arguments.
 * </p>
 */
public class DeclarativePopulator implements NodePopulator {
    @NotNull
    private final NodeFactory<InputDataObject> factory;

    @NotNull
    private final InputDataObject itemsData;

    public DeclarativePopulator(
            @NotNull NodeFactory<InputDataObject> factory,
            @NotNull InputDataObject itemsData) {
        this.factory = factory;
        this.itemsData = itemsData;
    }

    @Override
    public void populate(@NotNull final LogicNode node) {
        final IdSet<LogicNode> idSet = node.getTreeContext().getIdSet();

        itemsData.accept(new DataObjectVisitorAdapter() {
            @Override
            public void beginVisitObject() {
            }

            @Override
            public void visitField(String field, InputDataObject value) {
                node.getOrAdd(
                        idSet.get(field),
                        factory,
                        value);
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
                throw new IllegalArgumentException("Unexpected value type.");
            }
        });
    }

    @NotNull
    @Override
    public LogicNode resolve(@NotNull Id<LogicNode> childId) {
        InputDataObject itemData;

        try {
            itemData = itemsData.getField(String.valueOf(childId.serializable()));
        } catch (UndefinedFieldException e) {
            throw new NoSuchElementException();
        }

        return factory.create(itemData);
    }
}
