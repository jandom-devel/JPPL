package it.unich.jppl;

public interface Domain<T extends Property<T>> {

    public static enum DegenerateElement {
        UNIVERSE, EMPTY
    }

    public static enum ComplexityClass {
        POLYNOMIAL_COMPLEXITY, SIMPLEX_COMPLEXITY, ANY_COMPLEXITY
    }

    public static enum RecycleInput {
        RECYCLE
    }

    public T createProperty(long d, DegenerateElement kind);

    public T createProperty(ConstraintSystem cs);

    public T createProperty(ConstraintSystem cs, RecycleInput dummy);

    public T createProperty(CongruenceSystem cs);

    public T createProperty(CongruenceSystem cs, RecycleInput dummy);

    public T createProperty(GeneratorSystem gs);

    public T createProperty(GeneratorSystem gs, RecycleInput dummy);

    public T createProperty(T p);

    public T createProperty(T p, ComplexityClass complexity);

    public T createProperty(CPolyhedron p);

    public T createProperty(CPolyhedron p, ComplexityClass complexity);

    public T createProperty(NNCPolyhedron p);

    public T createProperty(NNCPolyhedron p, ComplexityClass complexity);

    public T createProperty(DoubleBox p);

    public T createProperty(DoubleBox p, ComplexityClass complexity);

}
