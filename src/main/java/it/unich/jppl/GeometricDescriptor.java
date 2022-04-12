package it.unich.jppl;

/**
 * A geometric descriptor.
 *
 * <p>
 * Common abstract class for all (syntactic) geometric descriptors. Each
 * geometric descriptor has a space dimension and is associated with a linear
 * expression. Some geometric descriptors ignore the inhomogeneous term of the
 * linear expression.
 * </p>
 *
 * <p>
 * If using only public methods, the geometric descriptors may be considered
 * immutable. Almost all methods throw PPLError when the underlying PPL library
 * generates an error.
 * </p>
 *
 * @see LinearExpression
 */
public abstract class GeometricDescriptor<GD extends GeometricDescriptor<GD>> extends PPLObject<GD> {

    /**
     * Returns the coefficient for the variable \(x_i\) of the associated linear
     * form.
     */
    public abstract Coefficient getCoefficient(long i);

    /**
     * Returns the space dimension.
     */
    public abstract long getSpaceDimension();


}
