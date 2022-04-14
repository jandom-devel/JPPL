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
     */
    T createEmpty(long d);

    /**
     * Creates and returns an universe {@code d}-dimensional abstract object, i.e., the
     * full vector space \(\mathbb{R}^d\).
     */
    T createUniverse(long d);

    /**
     * Creates and returns an abstract object from the constraints in {@code cs}.
     * The abstract object inherits the space dimension of {@code cs}.
     */
    T createFrom(ConstraintSystem cs);

    /**
     * Creates and returns an abstract object from the constraints in {@code cs},
     * possibly recycling their internal structure. After calling this methods,
     * there is no guarantee on the content of {@code cs}. The abstract object
     * inherits the space dimension of {@code cs}.
     */
    T createRecycledFrom(ConstraintSystem cs);

    /**
     * Creates and returns an abstract object from the congruences in {@code cs}.
     * The abstract object inherits the space dimension of {@code cs}.
     */
    T createFrom(CongruenceSystem cs);

    /**
     * Creates and returns an abstract object from the congruences in {@code cs}, possibly
     * recycling their internal structure. After calling this methods, there is no
     * guarantee on the content of {@code cs}. The abstract object inherits the
     * space dimension of cs.
     */
    T createRecycledFrom(CongruenceSystem cs);

    /**
     * Creates and returns an abstract object from the generators in {@code gs}. The
     * abstract object inherits the space dimension of {@code gs}.
     */
    T createFrom(GeneratorSystem gs);

    /**
     * Creates and returns an abstract object from the generators in {@code gs},
     * possibly recycling their internal structure. After calling this methods,
     * there is no guarantee on the contents of {@code gs}. The abstract object
     * inherits the space dimension of [@code gs}.
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
