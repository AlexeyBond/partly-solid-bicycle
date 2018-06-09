package io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.pm;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class DefaultProcessManagerTest {
    private DefaultProcessManager processManager;
    private Runnable[] runnables = new Runnable[4];

    @Before public void setUp() {
        processManager = new DefaultProcessManager();
        for (int i = 0; i < runnables.length; i++) {
            runnables[i] = mock(Runnable.class);
        }
    }

    @Test public void checkItemsOrder() {
        processManager.orderHint("a", "b");
        processManager.orderHint("b", "c");
        processManager.orderHint("c", "d");

        processManager.addProcess("a", runnables[0]);
        processManager.addProcess("b", runnables[1]);
        processManager.addProcess("d", runnables[2]);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                verify(runnables[0]).run();
                return null;
            }
        }).when(runnables[1]).run();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                verify(runnables[1]).run();
                return null;
            }
        }).when(runnables[2]).run();

        processManager.run();

        verify(runnables[2]).run();

        processManager.removeProcess("a", runnables[0]);

        reset(runnables);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                verify(runnables[1]).run();
                return null;
            }
        }).when(runnables[2]).run();

        processManager.run();

        verify(runnables[2]).run();

        verify(runnables[0], times(0)).run();
    }

    @Test(expected = IllegalStateException.class)
    public void testRejectDuplicateProcesses() {
        processManager.addProcess("a", runnables[0]);
        processManager.addProcess("a", runnables[1]);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveNonExist() {
        processManager.removeProcess("a", runnables[0]);
    }
}
