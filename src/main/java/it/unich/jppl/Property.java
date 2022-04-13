package it.unich.jppl;

import it.unich.jppl.Constraint.ConstraintType;

import java.util.Optional;

/**
 * The interface for properties, i.e., elements of an abstract domain.
 *
 * <p>
 * Properties are semantic geometric descriptors, i.e., subsets of a
 * d-dimentsional vector space \(\mathbb{R}^d\). They are also called abstract
 * objects, since they are elements of an abstract domain. Refer to the <a href=
 * "https://www.bugseng.com/external/ppl/documentation/user/ppl-user-c-interface-1.2-html/">PPL
 * official documentation</a> for further information.
 * </p>
 */
public interface Property<T extends Property<T>> {

    /**
     * Class containing the result of a minimize(WithPoint) or maximize(WithPoint)
     * method.
     */
    static class ExtremalOutput {
        /**
         * The numerator of a fraction which is the result of the minimization or
         * maximization method.
         */
        public final Coefficient supN;

        /**
         * The denominator of a fraction which is the result of the minimization or
         * maximization method.
         */
        public final Coefficient supD;

        /**
         * This is true if the extremal value is a maximum or minimum, false if it only
         * a greatest lower bound or lowest upper bound.
         */
        public final boolean isMaximum;

        /**
         * This is the point in the abstract object where the extremal value is
         * obtained. May be null.
         */
        public final Generator point;

        /**
         * Creates an ExtremalOutput by providing all required fields.
         */
        ExtremalOutput(Coefficient supN, Coefficient supD, boolean isMaximum, Generator point) {
            this.supN = supN;
            this.supD = supD;
            this.isMaximum = isMaximum;
            this.point = point;
        }
    }

    /**
     * Complexity pseudo-classes.
     */
    public static enum ComplexityClass {
        /** Worst-case polynomial complexity. */
        POLYNOMIAL_COMPLEXITY,
        /** Worst-case exponential complexity but typically polynomial behavior. */
        SIMPLEX_COMPLEXITY,
        /** Any complexity. */
        ANY_COMPLEXITY
    }

    /**
     * Class of constants representing the relation between a contraint and an
     * geometric object.
     */
    public static class RelationWithConstraint {
        /**
         * The geometric oject and the set of points satisfying the constraint are
         * disjoint.
         */
        public static final int IS_DISJOINT = 1;
        /**
         * The geometric object intersects the set of points satisfying the constraint,
         * but it is not included in it.
         */
        public static final int STRICTLY_INTERSECTS = 2;
        /**
         * The geometric object is included in the set of points satisfying the
         * constraint.
         */
        public static final int IS_INCLUDED = 4;
        /**
         * The geometric object is included in the set of points saturating the
         * constraint.
         */
        public static final int SATURATES = 8;
    }

    /**
     * Class of constants representing the relation between a generator and a
     * geometric object. At the moment, there is a single valid relation which is
     * {@code SUBSUMES}.
     */
    public static class RelationWithGenerator {

        /**
         * It mans that adding the generator would not change the geometric object.
         */
        public static final int SUBSUMES = 1;
    }

    /**
     * A class which holds an integer value which is the number of available tokens
     * for a widening with tokens.
     */
    public static class WideningToken {
        /**
         * Number of abailable tokens.
         */
        int tokens;

        /**
         * Returns the current number of tokens.
         */
        int getTokens() {
            return tokens;
        }

        /**
         * Set the number of available tokens.
         */
        void setTokens(int n) {
            tokens =  n;
        }

        /**
         * Creates a WideningToken object by specifying the number of initial tokens.
         */
        WideningToken(int tokens) {
            this.tokens = tokens;
        }
    }

    long getSpaceDimension();

    long getAffineDimension();

    int getRelationWithConstraint(Constraint c);

    int getRelationWithGenerator(Generator g);

    ConstraintSystem getConstraints();

    CongruenceSystem getCongruences();

    ConstraintSystem getMinimizedConstraints();

    CongruenceSystem getMinimizedCongruences();

    boolean isEmpty();

    boolean isUniverse();

    boolean isBounded();

    boolean containsIntegerPoint();

    boolean isTopologicallyClosed();

    boolean isDiscrete();

    boolean constraints(long var);

    boolean boundsFromAbove(LinearExpression le);

    boolean boundsFromBelow(LinearExpression le);

    Optional<ExtremalOutput> maximize(LinearExpression le);

    Optional<ExtremalOutput> maximizeWithPoint(LinearExpression le);

    Optional<ExtremalOutput> minimize(LinearExpression le);

    Optional<ExtremalOutput> minimizeWithPoint(LinearExpression le);

    boolean contains(T p);

    boolean strictlyContains(T p);

    boolean isDisjointFrom(T p);

    boolean isOK();

    long getExternalMemoryInBytes();

    long getTotalMemoryInBytes();

    T addConstraint(Constraint c);

    T addCongruence(Congruence c);

    T addConstraints(ConstraintSystem cs);

    T addCongruences(CongruenceSystem cs);

    T addReycledConstraints(ConstraintSystem cs);

    T addRecycledCongruences(CongruenceSystem cs);

    T refineWithConstraint(Constraint c);

    T refineWithCongruence(Congruence c);

    T refineWithConstraints(ConstraintSystem c);

    T refineWithCongruences(CongruenceSystem c);

    T intersectionAssign(T p);

    T upperBoundAssign(T p);

    T differenceAssign(T p);

    T simplifyUsingContextAssign(T p);

    T timeElapseAssign(T p);

    T topologicalClosureAssign();

    T unconstrainSpaceDimension(long var);

    T unconstrainSpaceDimensions(long[] ds);

    T affineImage(long var, LinearExpression le, Coefficient d);

    default T affineImage(long var, LinearExpression le) {
        return affineImage(var, le, Coefficient.ONE);
    }

    T affinePreImage(long var, LinearExpression le, Coefficient d);

    default T affinePreImage(long var, LinearExpression le) {
        return affinePreImage(var, le, Coefficient.ONE);
    }

    T boundedAffineImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d);

    default T boundedAffineImage(long var, LinearExpression lb, LinearExpression ub) {
        return boundedAffineImage(var, lb, ub, Coefficient.ONE);
    }

    T boundedAffinePreImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d);

    default T boundedAffinePreImage(long var, LinearExpression lb, LinearExpression ub) {
        return boundedAffinePreImage(var, lb, ub, Coefficient.ONE);
    }

    T generalizedAffineImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d);

    default T generalizedAffineImage(long var, ConstraintType relsym, LinearExpression le) {
        return generalizedAffineImage(var, relsym, le, Coefficient.ONE);
    }

    T generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d);

    default T generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le) {
        return generalizedAffinePreImage(var, relsym, le, Coefficient.ONE);
    }

    T generalizedAffineImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs);

    T generalizedAffinePreImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs);

    T concatenateAssign(T p);

    T addSpaceDimensionsAndEmbed(long d);

    T addSpaceDimensionsAndProject(long d);

    T removeSpaceDimensions(long ds[]);

    T removeHigherSpaceDimensions(long d);

    T mapSpaceDimensions(long[] maps);

    T expandSpaceDimension(long d, long m);

    T foldSpaceDimensions(long[] ds, long d);
}
