package io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.InvalidScopeMemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.InvalidScopeMemberReferenceStateException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.visitor.MemberReferenceVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class LazyReferenceState {
    abstract <T> T resolve(LazyMemberReference<T> reference);

    abstract <T, A> LazyMemberReference<T> replace(
            @NotNull LazyMemberReference<T> reference,
            @NotNull Factory<? extends T, A> factory,
            @Nullable A arg);

    abstract <T> void referenceRemoved(@NotNull LazyMemberReference<T> reference);

    abstract <T> void referenceVisited(@NotNull LazyMemberReference<T> reference, @NotNull MemberReferenceVisitor<T> visitor);

    static final LazyReferenceState FORWARDING = new LazyReferenceState() {
        @Override
        <T> T resolve(LazyMemberReference<T> reference) {
            return reference.forwarding.get();
        }

        @Override
        <T, A> LazyMemberReference<T> replace(
                @NotNull LazyMemberReference<T> reference,
                @NotNull Factory<? extends T, A> factory,
                @Nullable A arg) {
            reference.factory = factory;
            reference.factoryArg = arg;
            reference.state = PRE_INIT;
            return reference;
        }

        @Override
        <T> void referenceRemoved(@NotNull LazyMemberReference<T> reference) {
            reference.state = REMOVED;
        }

        @Override
        <T> void referenceVisited(@NotNull LazyMemberReference<T> reference, @NotNull MemberReferenceVisitor<T> visitor) {
            visitor.visitForwardedReference(reference.forwarding);
        }
    };

    static final LazyReferenceState PRE_INIT = new LazyReferenceState() {
        @Override
        <T> T resolve(LazyMemberReference<T> reference) {
            try {
                reference.state = INITIALIZING;
                @SuppressWarnings({"unchecked"})
                T member = (T) reference.factory.create(reference.factoryArg);
                reference.object = member;
                reference.factory = null;
                reference.factoryArg = null;
                reference.state = READY;
                return member;
            } finally {
                if (reference.state == INITIALIZING) {
                    reference.state = PRE_INIT;
                    reference.object = null;
                }
            }
        }

        @Override
        <T, A> LazyMemberReference<T> replace(
                @NotNull LazyMemberReference<T> reference,
                @NotNull Factory<? extends T, A> factory,
                @Nullable A arg) {
            return reference;
        }

        @Override
        <T> void referenceRemoved(@NotNull LazyMemberReference<T> reference) {
            // do nothing
        }

        @Override
        <T> void referenceVisited(@NotNull LazyMemberReference<T> reference, @NotNull MemberReferenceVisitor<T> visitor) {
            visitor.visitUnresolvedMember(reference.factory, reference.factoryArg);
        }
    };

    private static final LazyReferenceState INITIALIZING = new LazyReferenceState() {
        @Override
        <T> T resolve(LazyMemberReference<T> reference) {
            throw new InvalidScopeMemberReferenceStateException(
                    "Member required while being initialized. Looks like dependency loop between scope members.");
        }

        @Override
        <T, A> LazyMemberReference<T> replace(
                @NotNull LazyMemberReference<T> reference,
                @NotNull Factory<? extends T, A> factory,
                @Nullable A arg) {
            return reference;
        }

        @Override
        <T> void referenceRemoved(@NotNull LazyMemberReference<T> reference) {
            throw new InvalidScopeMemberReferenceStateException(
                    "Member removed while being initialized.");
        }

        @Override
        <T> void referenceVisited(@NotNull LazyMemberReference<T> reference, @NotNull MemberReferenceVisitor<T> visitor) {
            visitor.visitUnresolvedMember(reference.factory, reference.factoryArg);
        }
    };

    private static final LazyReferenceState READY = new LazyReferenceState() {
        @Override
        <T> T resolve(LazyMemberReference<T> reference) {
            return reference.object;
        }

        @Override
        <T, A> LazyMemberReference<T> replace(
                @NotNull LazyMemberReference<T> reference,
                @NotNull Factory<? extends T, A> factory,
                @Nullable A arg) {
            return reference;
        }

        @Override
        <T> void referenceRemoved(@NotNull LazyMemberReference<T> reference) {
            reference.object = null;
            reference.state = REMOVED;
        }

        @Override
        <T> void referenceVisited(@NotNull LazyMemberReference<T> reference, @NotNull MemberReferenceVisitor<T> visitor) {
            visitor.visitResolvedMember(reference.object);
        }
    };

    private static final LazyReferenceState REMOVED = new LazyReferenceState() {
        @Override
        <T> T resolve(LazyMemberReference<T> reference) {
            throw new InvalidScopeMemberReference("Member was removed from scope.");
        }

        @Override
        <T, A> LazyMemberReference<T> replace(
                @NotNull LazyMemberReference<T> reference,
                @NotNull Factory<? extends T, A> factory,
                @Nullable A arg) {
            throw new Error("Unreachable code.");
        }

        @Override
        <T> void referenceRemoved(@NotNull LazyMemberReference<T> reference) {
            throw new Error("Unreachable code.");
        }

        @Override
        <T> void referenceVisited(@NotNull LazyMemberReference<T> reference, @NotNull MemberReferenceVisitor<T> visitor) {
            throw new InvalidScopeMemberReference("Member was removed from scope.");
        }
    };
}
