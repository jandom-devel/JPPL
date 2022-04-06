package it.unich.jppl;

public class DoubleBoxDomain implements Domain<DoubleBox> {

    public DoubleBox createProperty(long d, DegenerateElement kind) {
        return new DoubleBox(d, kind);
    }

    public DoubleBox createProperty(ConstraintSystem cs) {
        return new DoubleBox(cs);
    }

    public DoubleBox createProperty(ConstraintSystem cs, RecycleInput dummy) {
        return new DoubleBox(cs, dummy);
    }

    public DoubleBox createProperty(DoubleBox box) {
        return new DoubleBox(box);
    }

    public DoubleBox createProperty(DoubleBox box, ComplexityClass complexity) {
        return new DoubleBox(box, complexity);
    }

    public DoubleBox createProperty(CPolyhedron ph) {
        return new DoubleBox(ph);
    }

    public DoubleBox createProperty(CPolyhedron ph, ComplexityClass complexity) {
        return new DoubleBox(ph, complexity);
    }

    public DoubleBox createProperty(NNCPolyhedron ph) {
        return new DoubleBox(ph);
    }

    public DoubleBox createProperty(NNCPolyhedron ph, ComplexityClass complexity) {
        return new DoubleBox(ph, complexity);
    }

}
