package it.unich.jppl;

public class NNCPolyhedronDomain implements Domain<NNCPolyhedron> {

    public NNCPolyhedron createProperty(long d, DegenerateElement kind) {
        return new NNCPolyhedron(d, kind);
    }

    public NNCPolyhedron createProperty(NNCPolyhedron ph) {
        return new NNCPolyhedron(ph);
    }

    public NNCPolyhedron createProperty(NNCPolyhedron ph, ComplexityClass complexity) {
        return new NNCPolyhedron(ph, complexity);
    }

    public NNCPolyhedron createProperty(ConstraintSystem cs) {
        return new NNCPolyhedron(cs);
    }

    public NNCPolyhedron createProperty(ConstraintSystem cs, RecycleInput dummy) {
        return new NNCPolyhedron(cs, dummy);
    }

    public NNCPolyhedron createProperty(CPolyhedron ph) {
        return new NNCPolyhedron(ph);
    }
}
