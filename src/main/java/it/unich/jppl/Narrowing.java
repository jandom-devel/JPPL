package it.unich.jppl;

/**
 * Functional interface for a narrowing.
 *
 * <p>
 * A narrowing is a function that takes two abstract objects {@code x} and
 * {@code y} and returns a new abstract object. Is is requird for {@code y} to
 * contains (or be equal to) {@code x}.
 * </p>
 */
@FunctionalInterface
public interface Narrowing<T extends Property<T>> {
    T apply(T x, T y);
}
