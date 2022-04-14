package it.unich.jppl;

import it.unich.jppl.Property.ComplexityClass;

/**
 * The abstract domain of closed polyehdra.
 */
public class CPolyhedronDomain implements Domain<CPolyhedron> {

    @Override
    public CPolyhedron createEmpty(long d) {
        return CPolyhedron.empty(d);
    }

    @Override
    public CPolyhedron createUniverse(long d) {
        return CPolyhedron.universe(d);
    }

    @Override
    public CPolyhedron createFrom(ConstraintSystem cs) {
        return CPolyhedron.from(cs);
    }

    @Override
    public CPolyhedron createRecycledFrom(ConstraintSystem cs) {
        return CPolyhedron.recycledFrom(cs);
    }

    @Override
    public CPolyhedron createFrom(CongruenceSystem cs) {
        return CPolyhedron.from(cs);
    }

    @Override
    public CPolyhedron createRecycledFrom(CongruenceSystem cs) {
        return CPolyhedron.recycledFrom(cs);
    }

    @Override
    public CPolyhedron createFrom(GeneratorSystem gs) {
        return CPolyhedron.from(gs);
    }

    @Override
    public CPolyhedron createRecycledFrom(GeneratorSystem gs) {
        return CPolyhedron.recycledFrom(gs);
    }

    @Override
    public CPolyhedron createFrom(CPolyhedron ph) {
        return CPolyhedron.from(ph);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The complexity argument is ignored.
     * </p>
     */
    @Override
    public CPolyhedron createFrom(CPolyhedron ph, ComplexityClass complexity) {
        return CPolyhedron.from(ph, complexity);
    }

    @Override
    public CPolyhedron createFrom(NNCPolyhedron ph) {
        return CPolyhedron.from(ph);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The complexity argument is ignored.
     * </p>
     */
    @Override
    public CPolyhedron createFrom(NNCPolyhedron ph, ComplexityClass complexity) {
        return CPolyhedron.from(ph, complexity);
    }

    @Override
    public CPolyhedron createFrom(DoubleBox box) {
        return CPolyhedron.from(box);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The complexity argument is ignored.
     * </p>
     */
    @Override
    public CPolyhedron createFrom(DoubleBox box, ComplexityClass complexity) {
        return CPolyhedron.from(box, complexity);
    }
}
