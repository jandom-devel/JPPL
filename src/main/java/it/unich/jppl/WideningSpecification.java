package it.unich.jppl;

/**
 * A specification for a widening operator.
 */
public class WideningSpecification<T extends Property<T>> {

    private String name;
    private Widening<T> widening;
    private WideningWithToken<T> wideningWithToken;

    /**
     * Creates a widening specification.
     */
    public WideningSpecification(String name, Widening<T> widening, WideningWithToken<T> wideningWithToken) {
        this.name = name;
        this.widening = widening;
        this.wideningWithToken = wideningWithToken;
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
    public Widening<T> getWidening() {
        return widening;
    }

    /**
     * Returns the variant of the widening with tokens.
     */
    public WideningWithToken<T> getWideningWithToken() {
        return wideningWithToken;
    }

}
