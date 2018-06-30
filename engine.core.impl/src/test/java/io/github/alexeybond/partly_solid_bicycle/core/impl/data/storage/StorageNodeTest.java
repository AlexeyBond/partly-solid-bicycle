package io.github.alexeybond.partly_solid_bicycle.core.impl.data.storage;

import io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils.TestUtils;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.ObjectVariable;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ChildLogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StorageNodeTest {
    private final static NodeFactory<InputDataObject> STORAGE_FACTORY
            = new NodeFactory<InputDataObject>() {
        @NotNull
        @Override
        public ChildLogicNode create(@Nullable InputDataObject arg) {
            if (null == arg) throw new NullPointerException("arg");
            return new StorageNode(arg);
        }
    };

    private LogicNode root;
    private Id<LogicNode> storageId;
    private IdSet<LogicNode> is;

    @Before
    public void setUp() {
        root = TestUtils.createRoot();
        is = root.getTreeContext().getIdSet();
        storageId = is.get("storage");
    }

    private LogicNode storageFromString(@NotNull final String string) {
        return root.getOrAdd(
                storageId, STORAGE_FACTORY, TestUtils.parseJSON(string));
    }

    @Test
    public void testDefaultNodes() {
        LogicNode storage = storageFromString("{}");

        LogicNode item = storage.get(is.unnamed());

        assertNotNull(item);
        assertTrue(item.getComponent() instanceof ObjectVariable);

        ObjectVariable<InputDataObject> variable = item.getComponent();

        assertNotNull(variable.get());

        DataObjectVisitor visitor = mock(DataObjectVisitor.class);

        variable.get().accept(visitor);

        verify(visitor, times(1)).visitNull();
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testLoadValues() {
        LogicNode storage = storageFromString("{\"theKey\":\"theValue\"}");

        LogicNode valueNode = storage.get(is.get("theKey"));

        assertNotNull(valueNode);
        assertTrue(valueNode.getComponent() instanceof ObjectVariable);

        ObjectVariable<InputDataObject> variable = valueNode.getComponent();

        assertEquals("theValue", variable.get().getString());

        assertNotNull(storage.getComponent());
        assertTrue(storage.getComponent() instanceof ObjectVariable);

        ObjectVariable<InputDataObject> storageDataVariable = storage.getComponent();
        InputDataObject storageData = storageDataVariable.get();

        assertEquals("theValue", storageData.getField("theKey").getString());
    }

    @Test
    public void testUpdateValues() {
        LogicNode storage = storageFromString("{\"theKey\":\"theValue\"}");

        ObjectVariable<InputDataObject> variable = storage.get(is.get("theKey")).getComponent();

        variable.set(TestUtils.parseJSON("\"theNewValue\""));

        ObjectVariable<InputDataObject> storageDataVariable = storage.getComponent();
        assertEquals("theNewValue", storageDataVariable.get().getField("theKey").getString());
    }

    @Test
    public void testNotifyListeners() {
        LogicNode storage = storageFromString("{\"theKey\":\"theValue\"}");
        Listener<ObjectVariable<InputDataObject>> listener = mock(Listener.class);

        ObjectVariable<InputDataObject> storageDataVariable = storage.getComponent();
        ObjectVariable<InputDataObject> variable = storage.get(is.get("theKey")).getComponent();

        storageDataVariable.subscribe(listener);

        variable.set(TestUtils.parseJSON("\"theNewValue\""));

        verify(listener).receive(same(storageDataVariable), same(storageDataVariable));
    }

    @Test
    public void testDataVisitor() {
        LogicNode storage = storageFromString("{\"theKey\":\"theValue\"}");

        ObjectVariable<InputDataObject> storageDataVariable = storage.getComponent();
        ObjectVariable<InputDataObject> variable = storage.get(is.get("theKey")).getComponent();
        ObjectVariable<InputDataObject> variable1 = storage.get(is.get("newKey")).getComponent();

        variable1.set(TestUtils.parseJSON("\"theNewValue\""));

        ArgumentCaptor<InputDataObject> dataObjectArgumentCaptor
                = ArgumentCaptor.forClass(InputDataObject.class);
        DataObjectVisitor visitor = mock(DataObjectVisitor.class);

        storageDataVariable.get().accept(visitor);

        verify(visitor).beginVisitObject();

        verify(visitor).visitField(eq("theKey"), dataObjectArgumentCaptor.capture());
        assertEquals("theValue", dataObjectArgumentCaptor.getValue().getString());

        verify(visitor).visitField(eq("newKey"), dataObjectArgumentCaptor.capture());
        assertEquals("theNewValue", dataObjectArgumentCaptor.getValue().getString());

        verify(visitor).endVisitObject();

        verifyNoMoreInteractions(visitor);
    }
}
