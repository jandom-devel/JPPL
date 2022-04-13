package it.unich.jppl;

import it.unich.jppl.Property.ComplexityClass;
/**
 * The interface for abstract domains.
 *
 * <p>
 * This interface is implemented by all classes representing an abstract domain.
 * A {@code Domain<T>} is a factory for properties (semantic geomtric
 * descriptors) of type {@code T}.
 * </p>
 */
public interface Domain<T extends Property<T>> {

    /**
     * Creates and returns an empty d-dimensional abstract object.
     */
    T createEmpty(long d);

    /**
     * Creates and returns an universe d-dimensional abstract object, i.e., the full
     * vector space \(\mathbb{R}^d\).
     */
    T createUniverse(long d);

    /**
     * Creates and returns an abstract object from the constraints in cs. The
     * abstract object inherits the space dimension of cs.
     */
    T createFrom(ConstraintSystem cs);

    /**
     * Creates and returns an abstract object from the constraints in cs, possibly
     * recycling their internal structure. The abstract object inherits the space
     * dimension of cs.
     */
    T createRecycledFrom(ConstraintSystem cs);

    /**
     * Creates and returns an abstract object from the congruences in cs. The
     * abstract object inherits the space dimension of cs.
     */
    T createFrom(CongruenceSystem cs);

    /**
     * Creates and returns an abstract object from the congruences in cs, possibly
     * recycling their internal structure. The abstract object inherits the space
     * dimension of cs.
     */
    T createRecycledFrom(CongruenceSystem cs);

    /**
     * Creates and returns an abstract object from the generators in gs. The
     * abstract object inherits the space dimension of gs.
     */
    T createFrom(GeneratorSystem gs);

    /**
     * Creates and returns an abstract object from the generators in gs, possibly
     * recycling their internal structure. The abstract object inherits the space
     * dimension of gs.
     */
    T createRecycledFrom(GeneratorSystem gs);

    /**
     * Creates and returns a copy of the abstract object p.
     */
    T createFrom(T p);

    /**
     * Creates and returns a copy of the abstract object p, using an algorithm whose
     * complexity does not exceed the one specified.
     */
    T createFrom(T p, ComplexityClass complexity);

    /**
     * Creates and returns an abstract object which over-approximates p.
     */
    T createFrom(CPolyhedron p);

    /**
     * Creates and returns an abstract object which over-approximates p, using an
     * algorithm whose complexity does not exceed the one specified.
     */
    T createFrom(CPolyhedron p, ComplexityClass complexity);

    /**
     * Creates and returns an abstract object which over-approximates p.
     */
    T createFrom(NNCPolyhedron p);

    /**
     * Creates and returns an abstract object which over-approximates p, using an algorithm whose
     * complexity does not exceed the one specified.
     */
    T createFrom(NNCPolyhedron p, ComplexityClass complexity);

    /**
     * Creates and returns an abstract object which over-approximates p.
     */
    T createFrom(DoubleBox p);

    /**
     * Creates and returns an abstract object which over-approximates p, using an algorithm whose
     * complexity does not exceed the one specified.
     */
    T createFrom(DoubleBox p, ComplexityClass complexity);

}
