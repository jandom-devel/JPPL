package it.unich.jppl;

import it.unich.jppl.Property.ComplexityClass;

/**
 * The abstract doman of not-necessarily closed polyhedra.
 */
public class NNCPolyhedronDomain implements Domain<NNCPolyhedron> {

    @Override
    public NNCPolyhedron createEmpty(long d) {
        return NNCPolyhedron.empty(d);
    }

    @Override
    public NNCPolyhedron createUniverse(long d) {
        return NNCPolyhedron.universe(d);
    }

    @Override
    public NNCPolyhedron createFrom(ConstraintSystem cs) {
        return NNCPolyhedron.from(cs);
    }

    @Override
    public NNCPolyhedron createRecycledFrom(ConstraintSystem cs) {
        return NNCPolyhedron.recycledFrom(cs);
    }

    @Override
    public NNCPolyhedron createFrom(CongruenceSystem cs) {
        return NNCPolyhedron.from(cs);
    }

    @Override
    public NNCPolyhedron createRecycledFrom(CongruenceSystem cs) {
        return NNCPolyhedron.recycledFrom(cs);
    }

    @Override
    public NNCPolyhedron createFrom(GeneratorSystem gs) {
        return NNCPolyhedron.from(gs);
    }

    @Override
    public NNCPolyhedron createRecycledFrom(GeneratorSystem gs) {
        return NNCPolyhedron.from(gs);
    }

    @Override
    public NNCPolyhedron createFrom(NNCPolyhedron ph) {
        return NNCPolyhedron.from(ph);
    }

    /**
     * @param complexity is ignored
     */
    @Override
    public NNCPolyhedron createFrom(NNCPolyhedron ph, ComplexityClass complexity) {
        return NNCPolyhedron.from(ph, complexity);
    }

    @Override
    public NNCPolyhedron createFrom(CPolyhedron ph) {
        return NNCPolyhedron.from(ph);
    }

    /**
     * @param complexity is ignored
     */
    @Override
    public NNCPolyhedron createFrom(CPolyhedron ph, ComplexityClass complexity) {
        return NNCPolyhedron.from(ph, complexity);
    }

    @Override
    public NNCPolyhedron createFrom(DoubleBox box) {
        return NNCPolyhedron.from(box);
    }

    /**
     * @param complexity is ignored
     */
    @Override
    public NNCPolyhedron createFrom(DoubleBox box, ComplexityClass complexity) {
        return NNCPolyhedron.from(box, complexity);
    }

}
