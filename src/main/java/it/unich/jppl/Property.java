package it.unich.jppl;

import it.unich.jppl.Constraint.ConstraintType;

import java.util.Optional;

/**
 * An abstract objectm, i.e., an element of an abstract domain.
 *
 * <p>
 * Properties are semantic geometric descriptors, i.e., subsets of a
 * d-dimensional vector space \(\mathbb{R}^d\). They are also called abstract
 * objects, since they are elements of an abstract domain. Refer to the <a href=
 * "https://www.bugseng.com/external/ppl/documentation/user/ppl-user-c-interface-1.2-html/">PPL
 * official documentation</a> for further information.
 * </p>
 *
 * @see Domain
 */
public interface Property<T extends Property<T>> {

    /**
     * Class containing the result of a
     * {@link Property#maximizeWithPoint(LinearExpression)},
     * {@link Property#maximize(LinearExpression)},
     * {@link Property#minimizeWithPoint(LinearExpression)} or
     * {@link Property#minimize(LinearExpression)} method.
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
     * Enumeration of complexity pseudo-classes.
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
     * abstract object.
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
     * Class of constants representing the relation between a generator and an
     * abstract object. At the moment, there is a single valid relation which is
     * {@code SUBSUMES}.
     */
    public static class RelationWithGenerator {

        /**
         * It mans that adding the generator would not change the geometric object.
         */
        public static final int SUBSUMES = 1;
    }

    /**
     * Class which holds an integer value which is the number of tokens available in
     * a widening with token.
     */
    public static class WideningTokens {
        /**
         * Number of abailable tokens.
         */
        int tokens;

        /**
         * Returns the number of available tokens.
         */
        int getTokens() {
            return tokens;
        }

        /**
         * Sets the number of available tokens.
         */
        void setTokens(int n) {
            tokens = n;
        }

        /**
         * Creates a WideningTokens object by specifying the number of available tokens.
         */
        WideningTokens(int tokens) {
            this.tokens = tokens;
        }
    }

    /** Returns the space dimension of this abstract object. */
    long getSpaceDimension();

    /** Returns the affine dimentsion of this abstract object. */
    long getAffineDimension();

    /**
     * Checks the relation between this abstract object and the constraint c. The
     * result is obtained as the bitwise or of the bits in
     * {@link RelationWithConstraint RelationWithConstraint}.
     */
    int getRelationWithConstraint(Constraint c);

    /**
     * Checks the relation between this abstract object and the generator g. The
     * result is obtained as the bitwise or of the bits in
     * {@link RelationWithGenerator RelationWithGenerator}.
     */
    int getRelationWithGenerator(Generator g);

    /**
     * Returns a constaint system approximating this abstract obkect. This is an
     * internal structure of the PPL, should not be modified and might not survive
     * any change to the abstract object.
     */
    ConstraintSystem getConstraints();

    /**
     * Returns a congruence system approximating this abstract obkect. This is an
     * internal structure of the PPL, should not be modified and might not survive
     * any change to the abstract object.
     */
    CongruenceSystem getCongruences();

    /**
     * Returns a minimized constraint system approximating this abstract obkect.
     * This is an internal structure of the PPL, should not be modified and might
     * not survive any change to the abstract object.
     */
    ConstraintSystem getMinimizedConstraints();

    /**
     * Returns a minimized congruence system approximating this abstract obkect.
     * This is an internal structure of the PPL, should not be modified and might
     * not survive any change to the abstract object.
     */
    CongruenceSystem getMinimizedCongruences();

    /**
     * Checks whether this abstract object is empty.
     */
    boolean isEmpty();

    /**
     * Checks whether this abstract object is the universe set, i.e., the full
     * vector space \(\mathbb{R}^d\) for space dimension \(d\).
     */
    boolean isUniverse();

    /**
     * Checks whether this abstract object is bounded.
     */
    boolean isBounded();

    /**
     * Checks whether this abstract object contains at least an integer point.
     */
    boolean containsIntegerPoint();

    /**
     * Checks whether this abstract object is topologically closed.
     */
    boolean isTopologicallyClosed();

    /**
     * Checks whether this abstract object is a discrete set.
     */
    boolean isDiscrete();

    /**
     * Checks whether this abstract object constraints the variable \(x_i\) in any
     * way.
     */
    boolean constraints(long i);

    /**
     * Checks whether the results of valuating the the linear expression le on the
     * points of this abstract object form a set bounded from above.
     */
    boolean boundsFromAbove(LinearExpression le);

    /**
     * Checks whether the results of valuating the the linear expression le on the
     * points of this abstract objects form a set bounded from below.
     */
    boolean boundsFromBelow(LinearExpression le);

    /**
     * Returns the supremum of the results of valuating the linear expression le on
     * the points of this abstract object. If the abstract object is empty or le is
     * not bounded from above, returns an empty Optional. Otherwise, it returns the
     * result, encoded into the {@link ExtremalOutput ExtremalOutput} class.
     */
    Optional<ExtremalOutput> maximizeWithPoint(LinearExpression le);

    /**
     * Similar to the {@link #maximizeWithPoint(LinearExpression)} method, but the
     * result does not include the point where the supremum is reached.
     */
    Optional<ExtremalOutput> maximize(LinearExpression le);

    /**
     * Similar to the {@link #maximizeWithPoint(LinearExpression)} method, but it
     * computes the infimum instead of the supremum.
     */
    Optional<ExtremalOutput> minimizeWithPoint(LinearExpression le);

    /**
     * Similar to the {@link #minimizeWithPoint(LinearExpression)} method, but the
     * result does not include the point where the infimum is reached.
     */
    Optional<ExtremalOutput> minimize(LinearExpression le);

    /**
     * Check whether this abstract object contains p.
     */
    boolean contains(T p);

    /**
     * Check whether this abstract object strictly contains p.
     */
    boolean strictlyContains(T p);

    /**
     * Check whether this abstract object is disjoint from p.
     */
    boolean isDisjointFrom(T p);

    /**
     * Returns a lower bound to the size in byte of the external memory managed by
     * the this abstract object (only counts the size of the native PPL object).
     */
    long getExternalMemoryInBytes();

    /**
     * Returns a lower bound to the size in byte of the memory managed by the this
     * abstract object (only counts the size of the native PPL object).
     */
    long getTotalMemoryInBytes();

    /**
     * Adds the constraint c to this abstract object.
     *
     * @throws PPLRuntimeException if this and c are dimension-incompatible, or the constraint
     *                  c is not optimally supported by the class of the abstract
     *                  object.
     * @return this abstract object
     */
    T addConstraint(Constraint c);

    /**
     * Adds the congruence c to this abstract object.
     *
     * @throws PPLRuntimeException if this and c are dimension-incompatible, or the congruence
     *                  c is not optimally supported by the class abstract object.
     * @return this abstract object
     */
    T addCongruence(Congruence c);

    /**
     * Adds the constraints in cs to the abstract object.
     *
     * @throws PPLRuntimeException if this and cs are dimension-incompatible, or cs constrains
     *                  a constraint which is not optimally supported by the class
     *                  of the abstract object.
     * @return this abstract object
     */
    T addConstraints(ConstraintSystem cs);

    /**
     * Adds the congruences in cs to the abstract object.
     *
     * @throws PPLRuntimeException if this and cs are dimension-incompatible, or cs constrains
     *                  a congruence which is not optimally supported by the class
     *                  of the abstract object.
     * @return this abstract object
     */
    T addCongruences(CongruenceSystem cs);

    /**
     * Similar to {@link #addConstraints(ConstraintSystem)}, but cs cannot be used
     * afterwards, since its internal data structure might have been reused.
     *
     * @return this abstract object
     */
    T addReycledConstraints(ConstraintSystem cs);

    /**
     * Similar to {@link #addCongruences(CongruenceSystem)}, but cs cannot be used
     * afterwards, since its internal data structure might have been reused.
     *
     * @return this abstract object
     */
    T addRecycledCongruences(CongruenceSystem cs);

    /**
     * Uses c to refine this abstract object. It is similar to
     * {@link #addConstraint(Constraint)}, but if the constraint is not supported
     * optimally by the class of the abstract object, an over-approximation of the
     * optimal result is returned.
     *
     * @return this abstract object
     */
    T refineWithConstraint(Constraint c);

    /**
     * Uses c to refine this abstract object. It is similar to
     * {@link #addCongruence(Congruence)}, but if the congruence is not supported
     * optimally by the class of the abstract object, an over-approximation of the
     * optimal result is returned.
     *
     * @return this abstract object
     */
    T refineWithCongruence(Congruence c);

    /**
     * Uses cs to refine this abstract object. It is similar to
     * {@link #addConstraints(ConstraintSystem)}, but if some constraint is not
     * supported optimally by the class of the abstract object, an
     * over-approximation of the optimal result is returned.
     *
     * @return this abstract object
     */
    T refineWithConstraints(ConstraintSystem c);

    /**
     * Uses cs to refine this abstract object. It is similar to
     * {@link #addCongruences(CongruenceSystem)}, but if some congruence is not
     * supported optimally by the class of the abstract object, an
     * over-approximation of the optimal result is returned.
     *
     * @return this abstract object
     */
    T refineWithCongruences(CongruenceSystem c);

    /**
     * Assign to this abstract object an over-approximation of the set-theoretic
     * intersection with p.
     *
     * @return this abstract object
     */
    T intersectionAssign(T p);

    /**
     * Assign to this abstract object an over-approximation of the set-theoretic
     * union with p.
     *
     * @return this abstract object
     */
    T upperBoundAssign(T p);

    /**
     * Assign to this abstract object an over-approximation of the set-theoretic
     * difference with p.
     *
     * @return this abstract object
     */
    T differenceAssign(T p);

    /**
     * Assign to this abstract object an over-approximation of the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Meet_Preserving_Simplification">
     * meet preserving simplification</a> of this with context p.
     *
     * @return this abstract object
     */
    T simplifyUsingContextAssign(T p);

    /**
     * Assigns to this the result of computing the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Time_Elapse_Operator">
     * time elapse</a> between this and p.
     *
     * @return this abstract object
     */
    T timeElapseAssign(T p);

    /**
     * Assign to this its topological closure.
     *
     * @return this abstract object
     */
    T topologicalClosureAssign();

    /**
     * Assign to this the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Cylindrification">cylindrification</a>
     * of this w.r.t. the space dimension i.
     *
     * @return this abstract object
     */
    T unconstrainSpaceDimension(long i);

    /**
     * Assign to this the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Cylindrification">cylindrification</a>
     * of this w.r.t. the space dimensions in ds.
     *
     * @return this abstract object
     */
    T unconstrainSpaceDimensions(long[] ds);

    /**
     * Assign to this an over-approximation of its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Single_Update_Affine_Functions">image</a>
     * under the function mapping the variable \(x_i\) to the linear expression
     * specified by {@code le} and {@code denominator}.
     *
     * @return this abstract object
     */
    T affineImage(long i, LinearExpression le, Coefficient denominator);

    /**
     * This is equivalent to {@code affineImage(i, le, Coefficient.ONE)}.
     */
    default T affineImage(long i, LinearExpression le) {
        return affineImage(i, le, Coefficient.ONE);
    }

    /**
     * Assign to this an over-approximation of its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Single_Update_Affine_Functions">preimage</a>
     * under the function mapping the variable \(x_i\) to the linear expression
     * specified by {@code le} and {@code denominator}.
     *
     * @return this abstract object
     */
    T affinePreImage(long var, LinearExpression le, Coefficient d);

    /**
     * This is equivalent to {@code affinePreImage(i, le, Coefficient.ONE)}.
     */
    default T affinePreImage(long var, LinearExpression le) {
        return affinePreImage(var, le, Coefficient.ONE);
    }

    /**
     * Assign to this an over-approximation if its image with respect to the
     * <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Single_Update_Bounded_Affine_Relations">bounded
     * affine relation</a> \(\frac{\mathit{lb}}{d} \leq x'_i \leq
     * \frac{\mathit{ub}}{d})\).
     *
     * @return this abstract object
     */
    T boundedAffineImage(long i, LinearExpression lb, LinearExpression ub, Coefficient d);

    /**
     * This is equivalent to {@code boundedAffineImage(i, lb, ub, Coefficient.ONE)}.
     */
    default T boundedAffineImage(long i, LinearExpression lb, LinearExpression ub) {
        return boundedAffineImage(i, lb, ub, Coefficient.ONE);
    }

    /**
     * Assign to this an over-approximation if its preimage with respect to the
     * <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Single_Update_Bounded_Affine_Relations">bounded
     * affine relation</a> \(\frac{\mathit{lb}}{d} \leq x'_i \leq
     * \frac{\mathit{ub}}{d})\).
     *
     * @return this abstract object
     */
    T boundedAffinePreImage(long i, LinearExpression lb, LinearExpression ub, Coefficient d);

    /**
     * This is equivalent to
     * {@code boundedAffinePreImage(i, lb, ub, Coefficient.ONE)}.
     */
    default T boundedAffinePreImage(long i, LinearExpression lb, LinearExpression ub) {
        return boundedAffinePreImage(i, lb, ub, Coefficient.ONE);
    }

    /**
     * Assign to this an over-approximation if its image with respect to the
     * <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Generalized_Affine_Relations">generalized
     * affine relation</a> \(x'_i \bowtie \frac{\mathit{e}}{d}\), where \(\bowtie\)
     * is the relation symbol encode by {@code relsym}.
     *
     * @return this abstract object
     */
    T generalizedAffineImage(long i, ConstraintType relsym, LinearExpression le, Coefficient d);

    /**
     * This is equivalent to
     * {@code generalizedAffineImage(i, relsym, le, Coefficient.ONE)}.
     */
    default T generalizedAffineImage(long i, ConstraintType relsym, LinearExpression le) {
        return generalizedAffineImage(i, relsym, le, Coefficient.ONE);
    }

    /**
     * Assign to this an over-approximation if its preimage with respect to the
     * <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Generalized_Affine_Relations">generalized
     * affine relation</a> \(x'_i \bowtie \frac{\mathit{e}}{d}\), where \(\bowtie\)
     * is the relation symbol encode by {@code relsym}.
     *
     * @return this abstract object
     */
    T generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d);

    /**
     * This is equivalent to
     * {@code generalizedAffinePreImage(i, relsym, le, Coefficient.ONE)}.
     */
    default T generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le) {
        return generalizedAffinePreImage(var, relsym, le, Coefficient.ONE);
    }

    /**
     * Assign to this an over-approximation if its image with respect to the
     * <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Generalized_Affine_Relations">generalized
     * affine relation</a> \(\mathit{lhs}' \bowtie \mathit{rhs}), where \(\bowtie\)
     * is the relation symbol encode by {@code relsym}.
     *
     * @return this abstract object
     */
    T generalizedAffineImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs);

    /**
     * Assign to this an over-approximation if its preimage with respect to the
     * <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Generalized_Affine_Relations">generalized
     * affine relation</a> \(\mathit{lhs}' \bowtie \mathit{rhs}\), where \(\bowtie\)
     * is the relation symbol encode by {@code relsym}.
     *
     * @return this abstract object
     */
    T generalizedAffinePreImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs);

    /**
     * Assign to this the cartesian product of this and p.
     *
     * @return this abstract object
     */
    T concatenateAssign(T p);

    /**
     * Adds {@code m} new dimensions to this, and embed the old abstract object in
     * the new vector space. The new dimensions will be those having the highest
     * indexes in the new abstract object, and they are all unconstrained.
     *
     * @return this abstract object
     */
    T addSpaceDimensionsAndEmbed(long m);

    /**
     * Adds {@code m} new dimensions to this, and embed the old abstract object in
     * the new vector space. The new dimensions will be those having the highest
     * indexes in the new abstract object, and they are all constrained to be equal
     * to zero.
     *
     * @return this abstract object
     */
    T addSpaceDimensionsAndProject(long d);

    /**
     * Removes all the specified dimensions.
     *
     * @return this abstract object
     */
    T removeSpaceDimensions(long ds[]);

    /**
     * Removes the higher dimensions so that the resulting space will have dimension
     * {@code new_dimension}.
     *
     * @return this abstract object
     */
    T removeHigherSpaceDimensions(long new_dimension);

    /**
     * Remaps the dimension of the vector space according to a <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Mapping_the_Dimensions_of_the_Vector_Space">partial
     * function</a>. This function is specified by means of the {@code maps} array.
     *
     * <p>
     * The partial function is defined on dimension {@code i} if
     * {@code i < maps.length} and {@code maps[i] !=
     * ppl_not_a_dimension}; otherwise it is undefined on dimension {@code i}. If
     * the function is defined on dimension {@code i}, then dimension {@code i} is
     * mapped onto dimension {@code maps[i]}.
     * </p>
     * <p>
     * The result is undefined if {@code maps} does not encode a partial function
     * with the properties described in the specification of the mapping operator.
     * </p>
     *
     * @return this abstract object
     */
    T mapSpaceDimensions(long[] maps);

    /**
     * Creates {@code m} copies of the space dimension {@code i}. If this has space
     * dimension \(n\), with \(n &gt; 0\), and \(i \leq n\), then the \(i\)-th space
     * dimension is <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#expand_space_dimension">expanded</a>
     * to \(m\) new space dimensions \(n, n+1, \dots, n+m-1 \).
     *
     * @return this abstract object
     */
    T expandSpaceDimension(long i, long m);

    /**
     * Folds the the space dimensions in ds into i. If this has space dimension
     * \(n\), with \(n &gt; 0\), \(i \leq n\), {@code ds} is a set of dimensions
     * less than or equal to \(n\), and \(d\) is not a member of {@code ds}, then
     * the space dimensions in {@code ds} are <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#fold_space_dimensions">folded</a>
     * into the \(d\)-th space dimension.
     *
     * @return this abstract object
     */
    T foldSpaceDimensions(long[] ds, long i);
}
