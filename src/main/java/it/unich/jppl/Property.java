package it.unich.jppl;

import it.unich.jppl.Constraint.ConstraintType;

import java.util.Optional;

/**
 * An abstract object, i.e., an element of an abstract domain.
 *
 * <p>
 * Properties are semantic geometric descriptors, i.e., subsets of a
 * \(d\)-dimensional vector space \(\mathbb{R}^d\). They are also called
 * abstract objects, since they are elements of an abstract domain. Refer to the
 * <a href=
 * "https://www.bugseng.com/external/ppl/documentation/user/ppl-user-c-interface-1.2-html/">PPL
 * official documentation</a> for further information.
 * </p>
 *
 * @see Domain
 */
public interface Property<T extends Property<T>> {

    /**
     * Result of a minimization or maximization operation on abstract objects.
     */
    static class ExtremalOutput {
        /**
         * The numerator the result.
         */
        public final Coefficient num;

        /**
         * The denominator of the result.
         */
        public final Coefficient den;

        /**
         * This is true if the result is an extremum, false if it is only a supremum or
         * infimum.
         */
        public final boolean isExtremum;

        /**
         * When available, this is a point in the abstract object where the extremal
         * value is reached, {@code null} otherwise.
         */
        public final Generator point;

        /*
         * Creates an ExtremalOutput by providing all required fields.
         */
        ExtremalOutput(Coefficient num, Coefficient den, boolean isExtremum, Generator point) {
            this.num = num;
            this.den = den;
            this.isExtremum = isExtremum;
            this.point = point;
        }
    }

    /**
     * Class of constants representing the relation between a contraint and an
     * abstract object.
     */
    public static class RelationWithConstraint {
        /**
         * The abstract object and the set of points satisfying the constraint are
         * disjoint.
         */
        public static final int IS_DISJOINT = 1;
        /**
         * The abstract object intersects the set of points satisfying the constraint,
         * but it is not included in it.
         */
        public static final int STRICTLY_INTERSECTS = 2;
        /**
         * The abstract object is included in the set of points satisfying the
         * constraint.
         */
        public static final int IS_INCLUDED = 4;
        /**
         * The abstract object is included in the set of points saturating the
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
         * Adding the generator to the abstract object would not change the latter.
         */
        public static final int SUBSUMES = 1;
    }

    /** Returns the space dimension of this abstract object. */
    long getSpaceDimension();

    /** Returns the affine dimension of this abstract object. */
    long getAffineDimension();

    /**
     * Checks the relation between this abstract object and the constraint
     * {@code c}. The result is obtained as the bitwise or of the relevant bits in
     * {@link RelationWithConstraint RelationWithConstraint}.
     */
    int getRelationWith(Constraint c);

    /**
     * Checks the relation between this abstract object and the generator g. The
     * result is obtained as the bitwise or of the relevant bits in
     * {@link RelationWithGenerator RelationWithGenerator}.
     */
    int getRelationWith(Generator g);

    /**
     * Returns a constaint system approximating this abstract object. This is an
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
     * Returns a minimized constraint system approximating this abstract object.
     * This is an internal structure of the PPL, should not be modified and might
     * not survive any change to the abstract object.
     */
    ConstraintSystem getMinimizedConstraints();

    /**
     * Returns a minimized congruence system approximating this abstract object.
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
     * Checks whether {@code le} is bounded from above on the point of this abstract
     * object.
     */
    boolean boundsFromAbove(LinearExpression le);

    /**
     * Checks whether {@code le} is bounded from below on the point of this abstract
     * object.
     */
    boolean boundsFromBelow(LinearExpression le);

    /**
     * Computes the supremum of {@code le} on the point of this abstract object. If
     * the abstract object is empty or {@code le} is not bounded from above, returns
     * an empty {@link Optional}. Otherwise, it returns the result, encoded into an
     * {@link ExtremalOutput ExtremalOutput} object.
     */
    Optional<ExtremalOutput> maximizeWithPoint(LinearExpression le);

    /**
     * Variant of {@link #maximizeWithPoint(LinearExpression) maximizeWithPoint}
     * which does not include the point where the supremum is reached.
     */
    Optional<ExtremalOutput> maximize(LinearExpression le);

    /**
     * Computes the infimum of {@code le} on the point of this abstract object. If
     * the abstract object is empty or {@code le} is not bounded from above, returns
     * an empty {@link Optional}. Otherwise, it returns the result, encoded into an
     * {@link ExtremalOutput ExtremalOutput} object.
     */
    Optional<ExtremalOutput> minimizeWithPoint(LinearExpression le);

    /**
     * Variant of {@link #minimizeWithPoint(LinearExpression) minimizeWithPoint}
     * which does not include the point where the infimum is reached.
     */
    Optional<ExtremalOutput> minimize(LinearExpression le);

    /**
     * Checkss whether this abstract object contains {@code y}.
     */
    boolean contains(T y);

    /**
     * Checks whether this abstract object strictly contains {@code y}.
     */
    boolean strictlyContains(T y);

    /**
     * Checks whether this abstract object is disjoint from {@code y}.
     */
    boolean isDisjointFrom(T y);

    /**
     * Returns the size in bytes of the memory managed by this abstract object. It
     * refers only to the size of the native PPL object.
     */
    long getExternalMemoryInBytes();

    /**
     * Returns the total size in bytes of the memory managed by this abstract
     * object. It refers only to the size of the native PPL object.
     */
    long getTotalMemoryInBytes();

    /**
     * Adds the constraint {@code c} to this abstract object.
     *
     * @throws PPLRuntimeException if {@code this} and {@code c} are
     *                             dimension-incompatible, or the constraint
     *                             {@code c} is not optimally supported by the class
     *                             of the abstract object.
     * @return this abstract object.
     */
    T add(Constraint c);

    /**
     * Adds the congruence {@code c} to this abstract object.
     *
     * @throws PPLRuntimeException if {@code this} and {@code c} are
     *                             dimension-incompatible, or the congruence
     *                             {@code c} is not optimally supported by the class
     *                             abstract object.
     * @return this abstract object.
     */
    T add(Congruence c);

    /**
     * Adds the constraints in {@code cs} to the abstract object.
     *
     * @throws PPLRuntimeException if {@code this} and {@code cs} are
     *                             dimension-incompatible, or {@code cs} constrains
     *                             a constraint which is not optimally supported by
     *                             the class of the abstract object.
     * @return this abstract object.
     */
    T add(ConstraintSystem cs);

    /**
     * Adds the congruences in {@code cs} to the abstract object.
     *
     * @throws PPLRuntimeException if {@code this} and {@code cs} are
     *                             dimension-incompatible, or {@code cs} constrains
     *                             a congruence which is not optimally supported by
     *                             the class of the abstract object.
     * @return this abstract object.
     */
    T add(CongruenceSystem cs);

    /**
     * Similar to {@link #add(ConstraintSystem) add} but after calling this method
     * there is no guarantee on the content of {@code cs}. For increasing
     * performance, its internal data structure might have been reused.
     *
     * @return this abstract object.
     */
    T addReycled(ConstraintSystem cs);

    /**
     * Similar to {@link #add(CongruenceSystem) add} but after calling this method
     * there is no guarantee on the content of {@code cs}. For increasing
     * performance, its internal data structure might have been reused.
     *
     * @return this abstract object.
     */
    T addRecycled(CongruenceSystem cs);

    /**
     * Uses {@code c} to refine this abstract object. It is similar to
     * {@link #add(Constraint) add}, but if the constraint is not optimally
     * supported by the class of the abstract object, an over-approximation of the
     * optimal result is returned.
     *
     * @return this abstract object.
     */
    T refineWith(Constraint c);

    /**
     * Uses {@code c} to refine this abstract object. It is similar to
     * {@link #add(Congruence) add}, but if the congruence is not optimally
     * supported by the class of the abstract object, an over-approximation of the
     * optimal result is returned.
     *
     * @return this abstract object.
     */
    T refineWith(Congruence c);

    /**
     * Uses {@code cs} to refine this abstract object. It is similar to
     * {@link #add(ConstraintSystem) add}, but if some constraint is not optimally
     * supported by the class of the abstract object, an over-approximation of the
     * optimal result is returned.
     *
     * @return this abstract object.
     */
    T refineWith(ConstraintSystem cs);

    /**
     * Uses {@code cs} to refine this abstract object. It is similar to
     * {@link #add(CongruenceSystem) add}, but if some congruence is not optimally
     * supported by the class of the abstract object, an over-approximation of the
     * optimal result is returned.
     *
     * @return this abstract object.
     */
    T refineWith(CongruenceSystem cs);

    /**
     * Assigns to {@code this} the best over-approximation of its intersection with
     * {@code y}.
     *
     * @return this abstract object.
     */
    T intersection(T y);

    /**
     * Assigns to {@code this} the best over-approximation of its union with
     * {@code y}.
     *
     * @return this abstract object.
     */
    T upperBound(T y);

    /**
     * Assigns to {@code this} the best over-approximation of its difference with
     * {@code y}.
     *
     * @return this abstract object.
     */
    T difference(T y);

    /**
     * Assigns to {@code this} an over-approximation of its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Meet_Preserving_Simplification">
     * meet preserving simplification</a> with context {@code y}.
     *
     * @return this abstract object.
     */
    T simplifyUsingContext(T y);

    /**
     * Assigns to {@code this} the result of computing the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Time_Elapse_Operator">
     * time elapse</a> between {@code this} this and {@code y}.
     *
     * @return this abstract object.
     */
    T timeElapse(T y);

    /**
     * Assigns to this abstract object its topological closure.
     *
     * @return this abstract object.
     */
    T topologicalClosure();

    /**
     * Assigns to this abstract object its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Cylindrification">cylindrification</a>
     * with respect to the space dimension {@code i}.
     *
     * @return this abstract object.
     */
    T unconstrain(long i);

    /**
     * Assigns to this abstract object its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Cylindrification">cylindrification</a>
     * with respect to the space dimensions in {@code ds}.
     *
     * @return this abstract object.
     */
    T unconstrain(long[] ds);

    /**
     * Assigns to {@code this} an over-approximation of its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Single_Update_Affine_Functions">image</a>
     * through a function \(f\). The function \(f\) replaces the variable \(x_i\)
     * with the result of the linear expression specified by {@code le} and the
     * divisor {@code d}.
     *
     * @return this abstract object.
     */
    T affineImage(long i, LinearExpression le, Coefficient d);

    /**
     * This is equivalent to {@code affineImage(i, le, Coefficient.ONE)}.
     */
    default T affineImage(long i, LinearExpression le) {
        return affineImage(i, le, Coefficient.ONE);
    }

    /**
     * Assigns to {@code this} an over-approximation of its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Single_Update_Affine_Functions">preimage</a>
     * through a function \(f\). The function \(f\) replaces the variable \(x_i\)
     * with the result of the linear expression specified by {@code le} and the
     * divisor {@code d}.
     *
     * @return this abstract object.
     */
    T affinePreImage(long i, LinearExpression le, Coefficient d);

    /**
     * This is equivalent to {@code affinePreImage(i, le, Coefficient.ONE)}.
     */
    default T affinePreImage(long i, LinearExpression le) {
        return affinePreImage(i, le, Coefficient.ONE);
    }

    /**
     * Assigns to {@code this} an over-approximation of its image with respect to
     * the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Single_Update_Bounded_Affine_Relations">bounded
     * affine relation</a> \(\frac{\mathit{lb}}{\mathit{den}} \leq x'_i \leq
     * \frac{\mathit{ub}}{\mathit{d}}\).
     *
     * @return this abstract object.
     */
    T boundedAffineImage(long i, LinearExpression lb, LinearExpression ub, Coefficient d);

    /**
     * This is equivalent to {@code boundedAffineImage(i, lb, ub, Coefficient.ONE)}.
     */
    default T boundedAffineImage(long i, LinearExpression lb, LinearExpression ub) {
        return boundedAffineImage(i, lb, ub, Coefficient.ONE);
    }

    /**
     * Assigns to {@code this} an over-approximation of its preimage with respect to
     * the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Single_Update_Bounded_Affine_Relations">bounded
     * affine relation</a> \(\frac{\mathit{lb}}{\mathit{d}} \leq x'_i \leq
     * \frac{\mathit{ub}}{\mathit{d}}\).
     *
     * @return this abstract object.
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
     * Assigns to {@code this} an over-approximation of its image with respect to
     * the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Generalized_Affine_Relations">generalized
     * affine relation</a> \(x'_i \bowtie \frac{\mathit{e}}{\mathit{d}}\). The
     * symbol \(\bowtie\) stands for the relation symbol encoded by {@code rel}.
     *
     * @return this abstract object.
     */
    T generalizedAffineImage(long i, ConstraintType rel, LinearExpression le, Coefficient d);

    /**
     * This is equivalent to
     * {@code generalizedAffineImage(i, rel, le, Coefficient.ONE)}.
     */
    default T generalizedAffineImage(long i, ConstraintType rel, LinearExpression le) {
        return generalizedAffineImage(i, rel, le, Coefficient.ONE);
    }

    /**
     * Assigns to this an over-approximation if its preimage with respect to the
     * <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Generalized_Affine_Relations">generalized
     * affine relation</a> \(x'_i \bowtie \frac{\mathit{e}}{\mathit{d}}\). The
     * symbol \(\bowtie\) stands for the relation symbol encoded by {@code rel}.
     *
     * @return this abstract object.
     */
    T generalizedAffinePreImage(long i, ConstraintType rel, LinearExpression le, Coefficient d);

    /**
     * This is equivalent to
     * {@code generalizedAffinePreImage(i, rel, le, Coefficient.ONE)}.
     */
    default T generalizedAffinePreImage(long i, ConstraintType rel, LinearExpression le) {
        return generalizedAffinePreImage(i, rel, le, Coefficient.ONE);
    }

    /**
     * Assigns to this an over-approximation if its image with respect to the
     * <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Generalized_Affine_Relations">generalized
     * affine relation</a> \(\mathit{lhs}' \bowtie \mathit{rhs}\). The symbol
     * \(\bowtie\) stands for the relation symbol encoded by {@code rel}.
     *
     * @return this abstract object.
     */
    T generalizedAffineImageLhsRhs(LinearExpression lhs, ConstraintType rel, LinearExpression rhs);

    /**
     * Assigns to this an over-approximation if its preimage with respect to the
     * <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Generalized_Affine_Relations">generalized
     * affine relation</a> \(\mathit{lhs}' \bowtie \mathit{rhs}\). The symbol
     * \(\bowtie\) stands for the relation symbol encode by {@code rel}.
     *
     * @return this abstract object.
     */
    T generalizedAffinePreImageLhsRhs(LinearExpression lhs, ConstraintType rel, LinearExpression rhs);

    /**
     * Assigns to this abstract object its cartesian product with {@code y}.
     *
     * @return this abstract object.
     */
    T concatenate(T y);

    /**
     * Adds {@code m} new dimensions to {@code this}, and embeds the old abstract
     * object in the new vector space. The new dimensions are those having the
     * highest indexes, and they are all unconstrained.
     *
     * @return this abstract object.
     */
    T addSpaceDimensionsAndEmbed(long m);

    /**
     * Adds {@code m} new dimensions to {@code this}, and does not embed the olds
     * abstract object in the new vector space. The new dimensions are those having
     * the highest indexes in the new abstract object, and they are all constrained
     * to be equal to zero.
     *
     * @return this abstract object.
     */
    T addSpaceDimensionsAndProject(long m);

    /**
     * Removes all the dimensions specified in {@code ds}.
     *
     * @return this abstract object.
     */
    T removeSpaceDimensions(long ds[]);

    /**
     * Removes the higher dimensions so that the resulting space will have dimension
     * {@code d}.
     *
     * @return this abstract object.
     */
    T removeHigherSpaceDimensions(long d);

    /**
     * Remaps the dimension of the vector space according to the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Mapping_the_Dimensions_of_the_Vector_Space">partial
     * function</a> specified by the {@code maps} array.
     *
     * <p>
     * The partial function is defined on dimension {@code i} if
     * {@code i < maps.length} and {@code maps[i] !=
     * PPL.getNotADimension()}; otherwise it is undefined on dimension {@code i}. If
     * the function is defined on dimension {@code i}, then dimension {@code i} is
     * mapped onto dimension {@code maps[i]}. The result is undefined if
     * {@code maps} does not encode a partial function with the properties described
     * in the specification of the mapping operator.
     * </p>
     *
     * @return this abstract object.
     */
    T mapSpaceDimensions(long[] maps);

    /**
     * Creates {@code m} copies of the space dimension {@code i}. If {@code this}
     * has space dimension \(n\), with \(n &gt; 0\), and \(i \leq n\), then the
     * \(i\)-th space dimension is <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#expand_space_dimension">expanded</a>
     * to \(m\) new space dimensions \(n, n+1, \dots, n+m-1 \).
     *
     * @return this abstract object.
     */
    T expandSpaceDimension(long i, long m);

    /**
     * Folds the the space dimensions in {@code ds} into {@code i}. If {@code this}
     * has space dimension \(n\), with \(n &gt; 0\), \(i \leq n\), {@code ds} is a
     * set of dimensions less than or equal to \(n\), and \(i\) is not a member of
     * {@code ds}, then the space dimensions in {@code ds} are <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#fold_space_dimensions">folded</a>
     * into the \(i\)-th space dimension.
     *
     * @return this abstract object.
     */
    T foldSpaceDimensions(long[] ds, long i);

    /**
     * Assigns to this abstract object its <em>standard</em> widening with
     * {@code y}. The box {@code y} must be contained in (or be equal to)
     * {@code this}. Which widening is considered standard is up to the JPPL
     * developers.
     *
     * @return this abstract object.
     */
    T widening(T y);

    /**
     * Variant of {@link #widening(Property) widening} which also applies the
     * <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Widening_with_Tokens">widening
     * with tokens</a> delay tecnique.
     *
     * @param w number of available tokens.
     * @return this abstract object.
     */
    T widening(T y, WideningTokens w);

    /**
     * Returns whether {@code obj} is the same abstract object as {@code this}.
     */
    boolean equals(Object obj);
}
