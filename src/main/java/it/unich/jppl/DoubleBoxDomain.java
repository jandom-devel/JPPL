package it.unich.jppl;

import java.util.Arrays;
import java.util.List;

/**
 * The abstract domain of boxes, with bounds implemented using native doubles.
 */
public class DoubleBoxDomain implements Domain<DoubleBox> {

    private static List<WideningSpecification<DoubleBox>> widenings = Arrays.asList(
        new WideningSpecification<>("CC76", DoubleBox::CC76Widening, DoubleBox::CC76Widening));


    private static List<NarrowingSpecification<DoubleBox>> narrowings = Arrays.asList(
        new NarrowingSpecification<>("CC76", DoubleBox::CC76Narrowing));

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
     * {@inheritDoc}}
     * <p>
     * The complexity argument is ignored.
     * </p>
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
     * {@inheritDoc}
     * <p>
     * If {@code complexity} is {@code ANY_COMPLEXITY}, then the built box is the
     * smallest one containing {@code ph}.
     * </p>
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
     * {@inheritDoc}
     * <p>
     * If {@code complexity} is {@code ANY_COMPLEXITY}, then the built box is the
     * smallest one containing {@code ph}.
     * </p>
     */
    @Override
    public DoubleBox createFrom(NNCPolyhedron ph, ComplexityClass complexity) {
        return DoubleBox.from(ph, complexity);
    }

    @Override
    public List<WideningSpecification<DoubleBox>> getWidenings() {
        return widenings;
    }

    @Override
    public List<NarrowingSpecification<DoubleBox>> getNarrowings() {
        return narrowings;
    }

}
