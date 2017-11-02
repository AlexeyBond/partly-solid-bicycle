package io.github.alexeybond.partly_solid_bicycle.core.impl.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeOwner;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.InvalidScopeMemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.InvalidScopeMemberReferenceStateException;
import org.jetbrains.annotations.NotNull;

/**
 * Reference to a object that is reachable from some {@link ScopeOwner} through a sequence of
 * scopes and scope members that are scope owners themselves.
 *
 * @param <T> type of referred object
 */
public class RelativeReference<T> implements MemberReference<T> {
    private final String text;
    private final String[] steps;
    private final ScopeOwner start;

    @NotNull
    public static String[] parseText(@NotNull String text) {
        return text.split("/");
    }

    public RelativeReference(@NotNull String text, @NotNull ScopeOwner start) {
        this(text, parseText(text), start);
    }

    public RelativeReference(@NotNull String text, @NotNull String[] steps, @NotNull ScopeOwner start) {
        this.text = text;
        this.steps = steps;
        this.start = start;
    }

    @NotNull
    private <X> X walkInto(@NotNull Scope<X, ?> scope, @NotNull String id, int step) {
        try {
            return scope.get(scope.getIdSet().get(id)).get();
        } catch (Exception e) {
            throw new InvalidScopeMemberReferenceStateException(
                    "Object at step " + step + " ('" + id + "')" +
                    " of relative reference '" + text + "'" +
                    " is not accessible:", e);
        }
    }

    @NotNull
    private Object walk() {
        Object cur = start;

        for (int i = 0; i < steps.length; i++) {
            String step = steps[i];
            ScopeOwner so;

            try {
                so = (ScopeOwner) cur;
            } catch (ClassCastException e) {
                throw new InvalidScopeMemberReferenceStateException(
                        "Object at step " + i + " ('" + step + "')" +
                        " of relative reference '" + text + "'" +
                        " is not a scope owner: " + e.getMessage());
            }

            cur = walkInto(so.getScope(), step, i);
        }

        return cur;
    }

    @NotNull
    @Override
    public T get()
            throws InvalidScopeMemberReference, InvalidScopeMemberReferenceStateException {
        @SuppressWarnings({"unchecked"}) T uRes = (T) walk();
        return uRes;
    }
}
