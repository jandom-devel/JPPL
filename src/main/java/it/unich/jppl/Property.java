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

    public long getSpaceDimension();

    public long getAffineDimension();

    public int getRelationWithConstraint(Constraint c);

    public int getRelationWithGenerator(Generator g);

    public ConstraintSystem getConstraints();

    public CongruenceSystem getCongruences();

    public ConstraintSystem getMinimizedConstraints();

    public CongruenceSystem getMinimizedCongruences();

    public boolean isEmpty();

    public boolean isUniverse();

    public boolean isBounded();

    public boolean containsIntegerPoint();

    public boolean isTopologicallyClosed();

    public boolean isDiscrete();

    public boolean constraints(long var);

    public boolean boundsFromAbove(LinearExpression le);

    public boolean boundsFromBelow(LinearExpression le);

    public Optional<ExtremalOutput> maximize(LinearExpression le);

    public Optional<ExtremalOutput> maximizeWithPoint(LinearExpression le);

    public Optional<ExtremalOutput> minimize(LinearExpression le);

    public Optional<ExtremalOutput> minimizeWithPoint(LinearExpression le);

    public boolean contains(T p);

    public boolean strictlyContains(T p);

    public boolean isDisjointFrom(T p);

    public boolean isOK();

    public long getExternalMemoryInBytes();

    public long getTotalMemoryInBytes();

    public T addConstraint(Constraint c);

    public T addCongruence(Congruence c);

    public T addConstraints(ConstraintSystem cs);

    public T addCongruences(CongruenceSystem cs);

    public T addReycledConstraints(ConstraintSystem cs);

    public T addRecycledCongruences(CongruenceSystem cs);

    public T refineWithConstraint(Constraint c);

    public T refineWithCongruence(Congruence c);

    public T refineWithConstraints(ConstraintSystem c);

    public T refineWithCongruences(CongruenceSystem c);

    public T intersectionAssign(T p);

    public T upperBoundAssign(T p);

    public T differenceAssign(T p);

    public T simplifyUsingContextAssign(T p);

    public T timeElapseAssign(T p);

    public T topologicalClosureAssign();

    public T unconstrainSpaceDimension(long var);

    public T unconstrainSpaceDimensions(long[] ds);

    public T affineImage(long var, LinearExpression le, Coefficient d);

    default public T affineImage(long var, LinearExpression le) {
        return affineImage(var, le, Coefficient.ONE);
    }

    public T affinePreImage(long var, LinearExpression le, Coefficient d);

    default public T affinePreImage(long var, LinearExpression le) {
        return affinePreImage(var, le, Coefficient.ONE);
    }

    public T boundedAffineImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d);

    default public T boundedAffineImage(long var, LinearExpression lb, LinearExpression ub) {
        return boundedAffineImage(var, lb, ub, Coefficient.ONE);
    }

    public T boundedAffinePreImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d);

    default public T boundedAffinePreImage(long var, LinearExpression lb, LinearExpression ub) {
        return boundedAffinePreImage(var, lb, ub, Coefficient.ONE);
    }

    public T generalizedAffineImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d);

    default public T generalizedAffineImage(long var, ConstraintType relsym, LinearExpression le) {
        return generalizedAffineImage(var, relsym, le, Coefficient.ONE);
    }

    public T generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d);

    default public T generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le) {
        return generalizedAffinePreImage(var, relsym, le, Coefficient.ONE);
    }

    public T generalizedAffineImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs);

    public T generalizedAffinePreImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs);

    public T concatenateAssign(T p);

    public T addSpaceDimensionsAndEmbed(long d);

    public T addSpaceDimensionsAndProject(long d);

    public T removeSpaceDimensions(long ds[]);

    public T removeHigherSpaceDimensions(long d);

    public T mapSpaceDimensions(long[] maps);

    public T expandSpaceDimension(long d, long m);

    public T foldSpaceDimensions(long[] ds, long d);
}
