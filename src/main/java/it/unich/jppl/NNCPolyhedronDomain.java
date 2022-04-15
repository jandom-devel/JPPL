package it.unich.jppl;

import java.util.Arrays;
import java.util.List;

/**
 * The abstract doman of not-necessarily closed polyhedra.
 */
public class NNCPolyhedronDomain implements Domain<NNCPolyhedron> {

    private static List<WideningSpecification<NNCPolyhedron>> widenings = Arrays.asList(
            new WideningSpecification<>("H79", Polyhedron::H79Widening, Polyhedron::H79Widening),
            new WideningSpecification<>("BHRZ03", Polyhedron::BHRZ03Widening, Polyhedron::BHRZ03Widening));

    private static List<NarrowingSpecification<NNCPolyhedron>> narrowings = List.of();

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
     * {@inheritDoc}
     * <p>
     * The complexity argument is ignored.
     * </p>
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
     * {@inheritDoc}
     * <p>
     * The complexity argument is ignored.
     * </p>
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
     * {@inheritDoc}
     * <p>
     * The complexity argument is ignored.
     * </p>
     */
    @Override
    public NNCPolyhedron createFrom(DoubleBox box, ComplexityClass complexity) {
        return NNCPolyhedron.from(box, complexity);
    }

    @Override
    public List<WideningSpecification<NNCPolyhedron>> getWidenings() {
        return widenings;
    }

    @Override
    public List<NarrowingSpecification<NNCPolyhedron>> getNarrowings() {
        return narrowings;
    }

}
