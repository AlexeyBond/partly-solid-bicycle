package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions;

import org.jetbrains.annotations.NotNull;

/**
 * Object that defines rules of resolution of companion objects of any type for any object of
 * some class.
 *
 * @param <TO> class this resolver resolves companions for
 */
public interface ClassCompanionResolver<TO> {
    /**
     * @param type companion type name
     * @param <TC> companion type
     * @return {@link CompanionResolver} that will resolve a companion of given type for any instance of {@link TO}
     * @throws IllegalArgumentException if there is no resolver for required type of companion
     */
    @NotNull
    <TC extends Companion<TO>> CompanionResolver<TO, TC> resolve(@NotNull String type);
}
