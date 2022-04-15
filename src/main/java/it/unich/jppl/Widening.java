package it.unich.jppl;

/**
 * Functional interface for a widening.
 *
 * <p>
 * A widening is a function that takes two abstract objects {@code x} and
 * {@code y} and returns a new abstract object. Is is requird for {@code y} to
 * be contained in (or equal to) {@code x}.
 * </p>
 */
@FunctionalInterface
public interface Widening<T extends Property<T>> {
    T apply(T x, T y);
}
