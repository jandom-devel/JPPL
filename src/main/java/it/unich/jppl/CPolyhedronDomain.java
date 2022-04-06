package it.unich.jppl;

public class CPolyhedronDomain implements Domain<CPolyhedron> {

    public CPolyhedron createProperty(long d, DegenerateElement kind) {
        return new CPolyhedron(d, kind);
    }

    public CPolyhedron createProperty(ConstraintSystem cs) {
        return new CPolyhedron(cs);
    }

    public CPolyhedron createProperty(ConstraintSystem cs, RecycleInput dummy) {
        return new CPolyhedron(cs, dummy);
    }

    public CPolyhedron createProperty(CPolyhedron ph) {
        return new CPolyhedron(ph);
    }

    public CPolyhedron createProperty(CPolyhedron ph, ComplexityClass complexity) {
        return new CPolyhedron(ph, complexity);
    }

    public CPolyhedron createProperty(NNCPolyhedron ph) {
        return new CPolyhedron(ph);
    }

    public CPolyhedron createProperty(NNCPolyhedron ph, ComplexityClass complexity) {
        return new CPolyhedron(ph, complexity);
    }

    public CPolyhedron createProperty(DoubleBox box) {
        return new CPolyhedron(box);
    }

    public CPolyhedron createProperty(DoubleBox box, ComplexityClass complexity) {
        return new CPolyhedron(box, complexity);
    }
}
