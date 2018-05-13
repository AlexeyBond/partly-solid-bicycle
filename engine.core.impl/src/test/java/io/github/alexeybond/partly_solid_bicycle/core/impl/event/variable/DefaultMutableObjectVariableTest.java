package io.github.alexeybond.partly_solid_bicycle.core.impl.event.variable;

import com.badlogic.gdx.math.Vector2;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.ObjectVariable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public class DefaultMutableObjectVariableTest {
    private DefaultMutableObjectVariable<Vector2> makeVar() {
        return new DefaultMutableObjectVariable<Vector2>(
                0, new Vector2(), new Vector2());
    }

    private Listener<ObjectVariable<Vector2>> listener;
    private DefaultMutableObjectVariable<Vector2> var;
    private int state;

    @Before
    public void setUp() {
        listener = mock(Listener.class);
    }

    @Test
    public void testImmediateNotification() {
        var = makeVar();

        var.subscribe(listener);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                assertSame(var, invocation.getArgument(0));
                assertSame(var, invocation.getArgument(1));
                assertEquals(new Vector2(100, -200), var.get());
                return null;
            }
        }).when(listener).receive(
                ArgumentMatchers.<ObjectVariable<Vector2>>any(),
                ArgumentMatchers.<ObjectVariable<Vector2>>any());

        var.set(var.mutable().set(100, -200));

        verify(listener).receive(same(var), same(var));
    }

    @Test
    public void testPull() {
        var = makeVar();

        var.pull(listener);

        verify(listener).receive(same(var), same(var));
    }

    @Test
    public void testMutation() {
        var = makeVar();
        state = 0;

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                switch (state) {
                    case 0:
                        var.set(var.mutable().set(200, 300));
                        assertEquals(new Vector2(100, 200), var.get());
                        state = 1;
                        break;
                    case 1:
                        assertEquals(new Vector2(200, 300), var.get());
                        state = 2;
                        break;
                }
                return null;
            }
        }).when(listener).receive(
                ArgumentMatchers.<ObjectVariable<Vector2>>any(),
                ArgumentMatchers.<ObjectVariable<Vector2>>any());

        var.subscribe(listener);

        var.set(var.mutable().set(100, 200));

        assertEquals(2, state);
    }
}
