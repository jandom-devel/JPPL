package it.unich.jppl;

import it.unich.jppl.Property.ComplexityClass;

/**
 * The abstract domain of boxes, with bounds specified by doubles.
 */
public class DoubleBoxDomain implements Domain<DoubleBox> {

    @Override
    public DoubleBox createEmpty(long d) {
        return DoubleBox.empty(d);
    }

    @Override
    public DoubleBox createUniverse(long d) {
        return DoubleBox.universe(d);
    }

    @Override
    public DoubleBox createFrom(ConstraintSystem cs) {
        return DoubleBox.from(cs);
    }

    @Override
    public DoubleBox createRecycledFrom(ConstraintSystem cs) {
        return DoubleBox.recycledFrom(cs);
    }

    @Override
    public DoubleBox createFrom(CongruenceSystem cs) {
        return DoubleBox.from(cs);
    }

    @Override
    public DoubleBox createRecycledFrom(CongruenceSystem cs) {
        return DoubleBox.recycledFrom(cs);
    }

    @Override
    public DoubleBox createFrom(GeneratorSystem gs) {
        return DoubleBox.from(gs);
    }

    @Override
    public DoubleBox createRecycledFrom(GeneratorSystem gs) {
        return DoubleBox.recycledFrom(gs);
    }

    @Override
    public DoubleBox createFrom(DoubleBox box) {
        return DoubleBox.from(box);
    }

    /**
     * @param complexity is ignored
     */
    @Override
    public DoubleBox createFrom(DoubleBox box, ComplexityClass complexity) {
        return DoubleBox.from(box, complexity);
    }

    @Override
    public DoubleBox createFrom(CPolyhedron ph) {
        return DoubleBox.from(ph);
    }

    /**
     * @param complexity is ignored
     */
    @Override
    public DoubleBox createFrom(CPolyhedron ph, ComplexityClass complexity) {
        return DoubleBox.from(ph, complexity);
    }

    @Override
    public DoubleBox createFrom(NNCPolyhedron ph) {
        return DoubleBox.from(ph);
    }

    /**
     * @param complexity is ignored
     */
    @Override
    public DoubleBox createFrom(NNCPolyhedron ph, ComplexityClass complexity) {
        return DoubleBox.from(ph, complexity);
    }

}
