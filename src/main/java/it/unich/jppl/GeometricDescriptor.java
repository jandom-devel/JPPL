package it.unich.jppl;

/**
 * A (syntactic) geometric descriptor.
 *
 * <p>
 * This is the common interface for all (syntactic) geometric descriptors. Each
 * geometric descriptor has a space dimension and is associated with a linear
 * expression. Some geometric descriptors ignore the inhomogeneous term of the
 * linear expression.
 * </p>
 *
 * <p>
 * If using only public methods, the geometric descriptors may be considered
 * immutable.
 * </p>
 *
 * @see LinearExpression
 */
public interface GeometricDescriptor<GD extends GeometricDescriptor<GD>> extends PPLObject<GD> {

    /**
     * Returns the coefficient for the variable \(x_i\) of the associated linear
     * expression.
     */
    Coefficient getCoefficient(long i);

    /**
     * Returns the space dimension of this geometric descriptor.
     */
    long getSpaceDimension();

}
