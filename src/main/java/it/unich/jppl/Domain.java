package it.unich.jppl;

import it.unich.jppl.Property.ComplexityClass;

/**
 * An abstract domain, i.e., a factory for abstract objects.
 *
 * <p>
 * This interface is implemented by all classes representing an abstract domain.
 * A {@code Domain<T>} is a factory for abstract objects of type {@code T}.
 * </p>
 *
 * @see Property
 */
public interface Domain<T extends Property<T>> {

    /**
     * Creates and returns an empty {@code d}-dimensional abstract object.
     *
     * @throws PPLRuntimeException with code {@code LENGTH_ERROR} if {@code d}
     *                             exceeds the maximum allowed space dimension.
     */
    T createEmpty(long d);

    /**
     * Creates and returns an universe {@code d}-dimensional abstract object, i.e.,
     * the full vector space \(\mathbb{R}^d\).
     *
     * @throws PPLRuntimeException with code {@code LENGTH_ERROR} if {@code d}
     *                             exceeds the maximum allowed space dimension.
     */
    T createUniverse(long d);

    /**
     * Creates and returns an abstract object from the constraints in {@code cs}.
     * The abstract object inherits the space dimension of {@code cs}.
     *
     * @throws PPLRuntimeException with code {@code INVALID_ARGUMENT} if any of the
     *                             constraints in {@code cs} is not optimally
     *                             supported.
     */
    T createFrom(ConstraintSystem cs);

    /**
     * Similar to {@link #createFrom(ConstraintSystem) createFrom} but after calling
     * this method there is no guarantee on the content of {@code cs}. For
     * increasing performance, its internal data structure might have been reused.
     */
    T createRecycledFrom(ConstraintSystem cs);

    /**
     * Creates and returns an abstract object from the congruences in {@code cs}.
     * The abstract object inherits the space dimension of {@code cs}.
     *
     * @throws PPLRuntimeException with code {@code INVALID_ARGUMENT} if any of the
     *                             congruences in {@code cs} is not optimally
     *                             supported.
     */
    T createFrom(CongruenceSystem cs);

    /**
     * Similar to {@link #createFrom(CongruenceSystem) createFrom}, but after
     * calling this method there is no guarantee on the content of {@code cs}. For
     * increasing performance, its internal data structure might have been reused.
     */
    T createRecycledFrom(CongruenceSystem cs);

    /**
     * Creates and returns an abstract object from the generators in {@code gs}. The
     * abstract object inherits the space dimension of {@code gs}.
     *
     * @throws PPLRuntimeException with code {@code INVALID_ARGUMENT} if any of the
     *                             generators in {@code gs} is not optimally
     *                             supported.
     */
    T createFrom(GeneratorSystem gs);

    /**
     * Similar to {@link #createFrom(GeneratorSystem) createFrom}, but after calling
     * this method there is no guarantee on the content of {@code gs}. For
     * increasing performance, its internal data structure might have been reused.
     */
    T createRecycledFrom(GeneratorSystem gs);

    /**
     * Creates and returns a copy of the abstract object {@code p}.
     */
    T createFrom(T p);

    /**
     * Creates and returns a copy of the abstract object {@code p}, using an
     * algorithm whose complexity does not exceed the one specified in
     * {@code complexity}.
     */
    T createFrom(T p, ComplexityClass complexity);

    /**
     * Creates and returns an abstract object which over-approximates {@code p}.
     */
    T createFrom(CPolyhedron p);

    /**
     * Creates and returns an abstract object which over-approximates {@code p},
     * using an algorithm whose complexity does not exceed the one specified in
     * {@code complexity}.
     */
    T createFrom(CPolyhedron p, ComplexityClass complexity);

    /**
     * Creates and returns an abstract object which over-approximates {@code p}.
     */
    T createFrom(NNCPolyhedron p);

    /**
     * Creates and returns an abstract object which over-approximates {@code p},
     * using an algorithm whose complexity does not exceed the one specified in
     * {@code complexity}.
     */
    T createFrom(NNCPolyhedron p, ComplexityClass complexity);

    /**
     * Creates and returns an abstract object which over-approximates {@code p}.
     */
    T createFrom(DoubleBox p);

    /**
     * Creates and returns an abstract object which over-approximates {@code p},
     * using an algorithm whose complexity does not exceed the one specified in
     * {@code complexity}.
     */
    T createFrom(DoubleBox p, ComplexityClass complexity);

}
