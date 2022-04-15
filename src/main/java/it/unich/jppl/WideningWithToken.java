package it.unich.jppl;

/**
 * Functional interface for a widening with token.
 *
 * <p>
 * A widening with token is like a {@link Widening widening}, but has an
 * additional argument which contains the number of tokens which may be consumed
 * instead of extrapolating.
 * </p>
 */
@FunctionalInterface
public interface WideningWithToken<T extends Property<T>> {
    T apply(T x, T y, WideningTokens w);
}
