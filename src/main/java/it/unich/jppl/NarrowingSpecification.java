package it.unich.jppl;

/**
 * A specification for a widening operator.
 */
public class NarrowingSpecification<T extends Property<T>> {
    private String name;
    private Narrowing<T> narrowing;

    /**
     * Creates a widening specification.
     */
    public NarrowingSpecification(String name, Narrowing<T> narrowing) {
        this.name = name;
        this.narrowing = narrowing;
    }

    /**
     * Returns the name of this widening.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the widening.
     */
    public Narrowing<T> getNarrowing() {
        return narrowing;
    }

}
