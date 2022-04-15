package it.unich.jppl;

/**
 * Enumeration of complexity pseudo-classes.
 */
public enum ComplexityClass {
    /** Worst-case polynomial complexity. */
    POLYNOMIAL_COMPLEXITY,
    /** Worst-case exponential complexity but typically polynomial behavior. */
    SIMPLEX_COMPLEXITY,
    /** Any complexity. */
    ANY_COMPLEXITY
}
