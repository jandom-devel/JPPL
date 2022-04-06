package it.unich.jppl;

public interface Domain<T extends Property<T>> {
    enum DegenerateElement {
        UNIVERSE, EMPTY
    }

    enum ComplexityClass {
        POLYNOMIAL_COMPLEXITY, SIMPLEX_COMPLEXITY, ANY_COMPLEXITY
    }

    enum RecycleInput {
        RECYCLE
    }

    public T createProperty(long d, DegenerateElement kind);

    public T createProperty(T p);

    public T createProperty(T p, ComplexityClass complexity);

    public T createProperty(ConstraintSystem cs);

    public T createProperty(ConstraintSystem cs, RecycleInput dummy);

    // public T createProperty(CongruenceSystem cs);

    // public T createProperty(CongruenceSystem cs, RecycleInput dummy);

    public T createProperty(CPolyhedron p);

    public T createProperty(NNCPolyhedron p);
}
