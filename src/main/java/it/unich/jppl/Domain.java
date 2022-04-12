package it.unich.jppl;

/**
 * The interface for abstract domains.
 * 
 * <p>
 * The interface Domain should be implemented by all classes which represents
 * an abstract domain. A {@code Domain<T>} is a factory for geometric objects of
 * type {@code T}.
 * </p>
 */
public interface Domain<T extends Property<T>> {

    /**
     * Enumerates the kinds of degenerate abstract elements.
     */
    public static enum DegenerateElement {
        /** Referes to the universe element, i.e., the whole vector space. */
        UNIVERSE,
        /** Rgers to the empty element, i.e., the empty set. */
        EMPTY
    }

    /**
     * Complexity pseudo-classes.
     */
    public static enum ComplexityClass {
        /** Worst-case polynomial complexity. */
        POLYNOMIAL_COMPLEXITY,
        /** Worst-case exponential complexity but typically polynomial behavior. */
        SIMPLEX_COMPLEXITY,
        /** Any complexity. */
        ANY_COMPLEXITY
    }

    /**
     * A tag enumeration.
     * 
     * <p>
     * Tag enumeration used to distinguish those constructors that recycle the data
     * structures of their arguments, instead of taking a copy.
     * </p>
     */
    public static enum RecycleInput {
        RECYCLE
    }

    /**
     * Builds an abstract object with d space dimensions of the special type
     * prescribed by kind.
     */
    public T createProperty(long d, DegenerateElement kind);

    /**
     * Builds an abstract object from the constraints in cs. The abstract object
     * inherits the space dimension of cs.
     */
    public T createProperty(ConstraintSystem cs);

    /**
     * Builds an abstract object from the constraints in cs, possibly recycling its
     * internal structure. The abstract object inherits the space dimension of cs.
     */
    public T createProperty(ConstraintSystem cs, RecycleInput dummy);

    /**
     * Builds an abstract object from the congruences in cs. The abstract object
     * inherits the space dimension of cs.
     */
    public T createProperty(CongruenceSystem cs);

    /**
     * Builds an abstract object from the congruences in cs, possibly recycling its
     * internal structure. The abstract object inherits the space dimension of cs.
     */
    public T createProperty(CongruenceSystem cs, RecycleInput dummy);

    /**
     * Builds an abstract object from the generators in gs. The abstract object
     * inherits the space dimension of gs.
     */
    public T createProperty(GeneratorSystem gs);

    /**
     * Builds an abstract object from the generators in gs, possibly recycling its
     * internal structure. The abstract object inherits the space dimension of gs.
     */
    public T createProperty(GeneratorSystem gs, RecycleInput dummy);

    /**
     * Builds a copy of the abstract object p.
     */
    public T createProperty(T p);

    /**
     * Builds a copy of the abstract object p, using an algorithm whose complexity
     * does not exceed the one specified.
     */
    public T createProperty(T p, ComplexityClass complexity);

    /**
     * Builds an abstract object which over-approximates p.
     */
    public T createProperty(CPolyhedron p);

    /**
     * Builds an abstract object which over-approximates p, using an algorithm whose
     * complexity does not exceed the one specified.
     */
    public T createProperty(CPolyhedron p, ComplexityClass complexity);

    /**
     * Builds an abstract object which over-approximates p.
     */
    public T createProperty(NNCPolyhedron p);

    /**
     * Builds an abstract object which over-approximates p, using an algorithm whose
     * complexity does not exceed the one specified.
     */
    public T createProperty(NNCPolyhedron p, ComplexityClass complexity);

    /**
     * Builds an abstract object which over-approximates p.
     */
    public T createProperty(DoubleBox p);

    /**
     * Builds an abstract object which over-approximates p, using an algorithm whose
     * complexity does not exceed the one specified.
     */
    public T createProperty(DoubleBox p, ComplexityClass complexity);

}
