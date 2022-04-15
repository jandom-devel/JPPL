package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.LibPPL.SizeT;
import it.unich.jppl.LibPPL.SizeTArray;
import it.unich.jppl.LibPPL.SizeTByReference;

import java.util.Optional;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Common abstract parent for both closed and NNC polyhedra.
 */
public abstract class Polyhedron<T extends Polyhedron<T>> extends AbstractPPLObject<T> implements Property<T> {

    static class PolyhedronCleaner implements Runnable {
        private Pointer pplObj;

        PolyhedronCleaner(Pointer pplObj) {
            this.pplObj = pplObj;
        }

        @Override
        public void run() {
            ppl_delete_Polyhedron(pplObj);
        }
    }

    /**
     * Returns an object of type {@code T}. This is used to simulate the Scala
     * self-type in Java. Each concrete class extending Polyhedron should provide an
     * implementation which just returns {@code this}.
     */
    abstract protected T self();

    @Override
    public long getSpaceDimension() {
        var pd = new SizeTByReference();
        int result = ppl_Polyhedron_space_dimension(pplObj, pd);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return pd.getValue().longValue();
    }

    @Override
    public long getAffineDimension() {
        var pd = new SizeTByReference();
        int result = ppl_Polyhedron_affine_dimension(pplObj, pd);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return pd.getValue().longValue();
    }

    @Override
    public int getRelationWith(Constraint c) {
        int result = ppl_Polyhedron_relation_with_Constraint(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result;
    }

    @Override
    public int getRelationWith(Generator g) {
        int result = ppl_Polyhedron_relation_with_Generator(pplObj, g.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result;
    }

    @Override
    public ConstraintSystem getConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_constraints(pplObj, pcs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new ConstraintSystem(pcs.getValue(), false);
    }

    @Override
    public CongruenceSystem getCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_congruences(pplObj, pcs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CongruenceSystem(pcs.getValue(), false);
    }

    @Override
    public ConstraintSystem getMinimizedConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_minimized_constraints(pplObj, pcs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new ConstraintSystem(pcs.getValue(), false);
    }

    @Override
    public CongruenceSystem getMinimizedCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_minimized_congruences(pplObj, pcs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CongruenceSystem(pcs.getValue(), false);
    }

    @Override
    public boolean isEmpty() {
        int result = ppl_Polyhedron_is_empty(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean isUniverse() {
        int result = ppl_Polyhedron_is_universe(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean isBounded() {
        int result = ppl_Polyhedron_is_bounded(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean containsIntegerPoint() {
        int result = ppl_Polyhedron_contains_integer_point(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean isTopologicallyClosed() {
        int result = ppl_Polyhedron_is_topologically_closed(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean isDiscrete() {
        int result = ppl_Polyhedron_is_discrete(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean constraints(long var) {
        int result = ppl_Polyhedron_constrains(pplObj, new SizeT(var));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean boundsFromAbove(LinearExpression le) {
        int result = ppl_Polyhedron_bounds_from_above(pplObj, le.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean boundsFromBelow(LinearExpression le) {
        int result = ppl_Polyhedron_bounds_from_below(pplObj, le.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public Optional<ExtremalOutput> maximize(LinearExpression le) {
        var cn = Coefficient.zero();
        var cd = Coefficient.zero();
        var pmaximum = new IntByReference();
        int result = ppl_Polyhedron_maximize(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(cn, cd, pmaximum.getValue() != 0, null));
    }

    @Override
    public Optional<ExtremalOutput> maximizeWithPoint(LinearExpression le) {
        var cn = Coefficient.zero();
        var cd = Coefficient.zero();
        var point = Generator.zeroDimPoint();
        var pmaximum = new IntByReference();
        int result = ppl_Polyhedron_maximize_with_point(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum,
                point.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(cn, cd, pmaximum.getValue() != 0, point.clone()));
    }

    @Override
    public Optional<ExtremalOutput> minimize(LinearExpression le) {
        var cn = Coefficient.zero();
        var cd = Coefficient.zero();
        var pmaximum = new IntByReference();
        int result = ppl_Polyhedron_minimize(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(cn, cd, pmaximum.getValue() != 0, null));
    }

    @Override
    public Optional<ExtremalOutput> minimizeWithPoint(LinearExpression le) {
        var cn = Coefficient.zero();
        var cd = Coefficient.zero();
        var point = Generator.zeroDimPoint();
        var pmaximum = new IntByReference();
        int result = ppl_Polyhedron_minimize_with_point(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum,
                point.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(cn, cd, pmaximum.getValue() != 0, point.clone()));
    }

    @Override
    public boolean contains(T y) {
        int result = ppl_Polyhedron_contains_Polyhedron(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean strictlyContains(T y) {
        int result = ppl_Polyhedron_strictly_contains_Polyhedron(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean isDisjointFrom(T y) {
        int result = ppl_Polyhedron_is_disjoint_from_Polyhedron(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    boolean isOK() {
        int result = ppl_Polyhedron_OK(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public long getExternalMemoryInBytes() {
        var pd = new SizeTByReference();
        int result = ppl_Polyhedron_external_memory_in_bytes(pplObj, pd);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return pd.getValue().longValue();
    }

    @Override
    public long getTotalMemoryInBytes() {
        var pd = new SizeTByReference();
        int result = ppl_Polyhedron_total_memory_in_bytes(pplObj, pd);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return pd.getValue().longValue();
    }

    @Override
    public T add(Constraint c) {
        int result = ppl_Polyhedron_add_constraint(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T add(Congruence c) {
        int result = ppl_Polyhedron_add_congruence(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T add(ConstraintSystem cs) {
        int result = ppl_Polyhedron_add_constraints(pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T add(CongruenceSystem cs) {
        int result = ppl_Polyhedron_add_congruences(pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T addReycled(ConstraintSystem cs) {
        int result = ppl_Polyhedron_add_recycled_constraints(pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T addRecycled(CongruenceSystem cs) {
        int result = ppl_Polyhedron_add_recycled_congruences(pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T refineWith(Constraint c) {
        int result = ppl_Polyhedron_refine_with_constraint(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T refineWith(Congruence c) {
        int result = ppl_Polyhedron_refine_with_congruence(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T refineWith(ConstraintSystem c) {
        int result = ppl_Polyhedron_refine_with_constraints(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T refineWith(CongruenceSystem c) {
        int result = ppl_Polyhedron_refine_with_congruences(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T intersection(T y) {
        int result = ppl_Polyhedron_intersection_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T upperBound(T y) {
        int result = ppl_Polyhedron_upper_bound_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T difference(T y) {
        int result = ppl_Polyhedron_difference_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T simplifyUsingContext(T y) {
        int result = ppl_Polyhedron_difference_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T timeElapse(T y) {
        int result = ppl_Polyhedron_time_elapse_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T topologicalClosure() {
        int result = ppl_Polyhedron_topological_closure_assign(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T unconstrain(long i) {
        int result = ppl_Polyhedron_unconstrain_space_dimension(pplObj, new SizeT(i));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T unconstrain(long[] ds) {
        var buffer = new SizeTArray(ds);
        int result = ppl_Polyhedron_unconstrain_space_dimensions(pplObj, buffer, new SizeT(ds.length));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T affineImage(long i, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_affine_image(pplObj, new SizeT(i), le.pplObj, d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T affinePreImage(long i, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_affine_preimage(pplObj, new SizeT(i), le.pplObj, d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T boundedAffineImage(long i, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Polyhedron_bounded_affine_image(pplObj, new SizeT(i), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T boundedAffinePreImage(long i, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Polyhedron_bounded_affine_preimage(pplObj, new SizeT(i), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T generalizedAffineImage(long i, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_generalized_affine_image(pplObj, new SizeT(i), relsym.ordinal(), le.pplObj,
                d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T generalizedAffinePreImage(long i, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_generalized_affine_preimage(pplObj, new SizeT(i), relsym.ordinal(), le.pplObj,
                d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T generalizedAffineImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs) {
        int result = ppl_Polyhedron_generalized_affine_image_lhs_rhs(pplObj, lhs.pplObj, relsym.ordinal(), rhs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T generalizedAffinePreImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs) {
        int result = ppl_Polyhedron_generalized_affine_preimage_lhs_rhs(pplObj, lhs.pplObj, relsym.ordinal(),
                rhs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T concatenate(T y) {
        int result = ppl_Polyhedron_concatenate_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T addSpaceDimensionsAndEmbed(long d) {
        int result = ppl_Polyhedron_add_space_dimensions_and_embed(pplObj, new SizeT(d));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T addSpaceDimensionsAndProject(long d) {
        int result = ppl_Polyhedron_add_space_dimensions_and_project(pplObj, new SizeT(d));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T removeSpaceDimensions(long ds[]) {
        int result = ppl_Polyhedron_remove_space_dimensions(pplObj, new SizeTArray(ds), new SizeT(ds.length));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T removeHigherSpaceDimensions(long d) {
        int result = ppl_Polyhedron_remove_higher_space_dimensions(pplObj, new SizeT(d));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T mapSpaceDimensions(long[] maps) {
        int result = ppl_Polyhedron_remove_space_dimensions(pplObj, new SizeTArray(maps), new SizeT(maps.length));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T expandSpaceDimension(long d, long m) {
        int result = ppl_Polyhedron_expand_space_dimension(pplObj, new SizeT(d), new SizeT(m));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    @Override
    public T foldSpaceDimensions(long[] ds, long i) {
        int result = ppl_Polyhedron_fold_space_dimensions(pplObj, new SizeTArray(ds), new SizeT(ds.length),
                new SizeT(i));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Returns a generator system approximating this polyhedron. This is an internal
     * structure of the PPL, should not be modified and might not survive any change
     * to the polyhedron.
     */
    public GeneratorSystem getGenerators() {
        var pgs = new PointerByReference();
        int result = ppl_Polyhedron_get_generators(pplObj, pgs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new GeneratorSystem(pgs.getValue(), false);
    }

    /**
     * Returns a minimized generator system approximating this polyhedron. This is
     * an internal structure of the PPL, should not be modified and might not
     * survive any change to the polyhedron.
     */
    public GeneratorSystem getMinimizedGenerators() {
        var pgs = new PointerByReference();
        int result = ppl_Polyhedron_get_minimized_generators(pplObj, pgs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new GeneratorSystem(pgs.getValue(), false);
    }

    /**
     * Adds the generator {@code g} to this polyhedron.
     *
     * @throws PPLRuntimeException if {@code this} and {@code g} are
     *                             dimension-incompatible, or the generator
     *                             {@code g} is not optimally supported by the class
     *                             of the polyhedron.
     * @return this polyhedron
     */
    public T add(Generator g) {
        int result = ppl_Polyhedron_add_generator(pplObj, g.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Adds the generators in {@code gs} to the polyhedron.
     *
     * @throws PPLRuntimeException if {@code this} and {@code gs} are
     *                             dimension-incompatible, or {@code gs} constrains
     *                             a generator which is not optimally supported by
     *                             the class of the polyhedron.
     * @return this polyhedron.
     */
    public T add(GeneratorSystem gs) {
        int result = ppl_Polyhedron_add_generators(pplObj, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Similar to {@link #add(GeneratorSystem)} but after calling this method there
     * is no guarantee on the content of {@code gs}. For increasing performance, its
     * internal data structure might have been reused.
     *
     * @return this polyhedron.
     */
    public T addReycled(GeneratorSystem gs) {
        int result = ppl_Polyhedron_add_recycled_generators(pplObj, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Assigns to this polyhedron is poly-hull with {@code ph}.
     *
     * @return this polyhedron.
     */
    public T polyHull(T y) {
        int result = ppl_Polyhedron_poly_hull_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Assigns to this polyhedron its <a href=
     * "https://www.bugseng.com/products/ppl/documentation/devref/ppl-devref-1.2-html/index.html#Convex_Polyhedral_Difference">poly-difference</a>
     * with {@code ph}.
     *
     * @return this polyhedron.
     */
    public T polyDifferenceAssign(T y) {
        int result = ppl_Polyhedron_poly_difference_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Variant of {@link #BHRZ03Widening(Polyhedron) BHRZ03Widening} which also
     * applies the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Widening_with_Tokens">widening
     * with tokens</a> delay tecnique.
     *
     * @param w number of available tokens.
     * @return this polyhedron.
     */
    public T BHRZ03Widening(T y, WideningTokens w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_BHRZ03_widening_assign_with_tokens(pplObj, y.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Assign to this polyhedron its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#BHRZ03_widening">BHRZ03-widening</a>
     * with {@code ph}. The polyhedron {@code ph} should be contained in (or be
     * equal to) {@code this}.
     *
     * @return this polyhedron.
     */
    public T BHRZ03Widening(T y) {
        int result = ppl_Polyhedron_BHRZ03_widening_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Variant of {@link #H79Widening(Polyhedron) H79Widening} which also applies
     * the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Widening_with_Tokens">widening
     * with tokens</a> delay tecnique.
     *
     * @param w number of available tokens.
     * @return this polyhedron.
     */
    public T H79Widening(T y, WideningTokens w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_H79_widening_assign_with_tokens(pplObj, y.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Assign to this polyhedron its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#H79_widening">H79-widening</a>
     * with {@code ph}. The polyhedron {@code ph} should be contained in (or be
     * equal to) {@code this}.
     *
     * @return this polyhedron.
     */
    public T H79Widening(T y) {
        int result = ppl_Polyhedron_H79_widening_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Variant of {@link #limitedBHRZ03Extrapolation(Polyhedron, ConstraintSystem)
     * limitedBHRZ03Extrapolation} which also applies the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Widening_with_Tokens">widening
     * with tokens</a> delay tecnique.
     *
     * @param w number of available tokens.
     * @return this polyhedron.
     */
    public T limitedBHRZ03Extrapolation(T y, ConstraintSystem cs, WideningTokens w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_limited_BHRZ03_extrapolation_assign_with_tokens(pplObj, y.pplObj, cs.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Assign to this polyhedron its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#BHRZ03_widening">BHRZ03-widening</a>
     * with {@code ph} using limited extrapolation. It means that the result is
     * intersected with the constraints in {@code cs} that are satisfied by all the
     * points of {@code this}. The polyhedron {@code ph} should be contained in (or
     * be equal to) {@code this}.
     *
     * @return this polyhedron.
     */
    public T limitedBHRZ03Extrapolation(T y, ConstraintSystem cs) {
        int result = ppl_Polyhedron_limited_BHRZ03_extrapolation_assign(pplObj, y.pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Variant of {@link #limitedH79Extrapolation(Polyhedron, ConstraintSystem)
     * limitedH79Extrapolation} which also applies the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Widening_with_Tokens">widening
     * with tokens</a> delay tecnique.
     *
     * @param w number of available tokens.
     * @return this polyhedron.
     */
    public T limitedH79Extrapolation(T y, ConstraintSystem cs, WideningTokens w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_limited_H79_extrapolation_assign_with_tokens(pplObj, y.pplObj, cs.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Assign to this polyhedron its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#H79_widening">H79-widening</a>
     * with {@code ph} using limited extrapolation. It means that the result is
     * intersected with the constraints in {@code cs} that are satisfied by all the
     * points of {@code this}. The polyhedron {@code ph} should be contained in (or
     * be equal to) {@code this}.
     *
     * @return this polyhedron.
     */
    public T limitedH79Extrapolation(T y, ConstraintSystem cs) {
        int result = ppl_Polyhedron_limited_H79_extrapolation_assign(pplObj, y.pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Variant of
     * {@link #boundedBHRZ03ExtrapolationAssign(Polyhedron, ConstraintSystem)
     * boundedBHRZ03ExtrapolationAssign} which also applies the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Widening_with_Tokens">widening
     * with tokens</a> delay tecnique.
     *
     * @param w number of available tokens.
     * @return this polyhedron.
     */
    public T boundedBHRZ03ExtrapolationAssign(T y, ConstraintSystem cs, WideningTokens w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_bounded_BHRZ03_extrapolation_assign_with_tokens(pplObj, y.pplObj, cs.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Assign to this polyhedron its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#BHRZ03_widening">BHRZ03-widening</a>
     * with {@code ph} using bounded extrapolation. It means the result is
     * intersected with the constraints in {@code cs} that are satisfied by all the
     * points of {@code this}, further intersected with all the constraints of the
     * form \(\pm v \leq r\) and \(\pm v &lt; r\), with $r \in \mathbbt{Q}$, that
     * are satisfied by all the points of {@code this}. The polyhedron {@code ph}
     * should be contained in (or be equal to) {@code this}.
     *
     * @return this polyhedron.
     */
    public T boundedBHRZ03ExtrapolationAssign(T y, ConstraintSystem cs) {
        int result = ppl_Polyhedron_bounded_BHRZ03_extrapolation_assign(pplObj, y.pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Variant of
     * {@link #boundedH79ExtrapolationAssign(Polyhedron, ConstraintSystem)
     * boundedH79ExtrapolationAssign} which also applies the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Widening_with_Tokens">widening
     * with tokens</a> delay tecnique.
     *
     * @param w number of available tokens.
     * @return this polyhedron.
     */
    public T boundedH79ExtrapolationAssign(T y, ConstraintSystem cs, WideningTokens w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_bounded_H79_extrapolation_assign_with_tokens(pplObj, y.pplObj, cs.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * Assign to this polyhedron its <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#H79_widening">H79-widening</a>
     * with {@code ph} using bounded extrapolation. It means the result is
     * intersected with the constraints in {@code cs} that are satisfied by all the
     * points of {@code this}, further intersected with all the constraints of the
     * form \(\pm v \leq r\) and \(\pm v &lt; r\), with $r \in \mathbbt{Q}$, that
     * are satisfied by all the points of {@code this}. The polyhedron {@code ph}
     * should be contained in (or be equal to) {@code this}.
     *
     * @return this polyhedron.
     */
    public T boundedH79ExtrapolationAssign(T y, ConstraintSystem cs) {
        int result = ppl_Polyhedron_bounded_H79_extrapolation_assign(pplObj, y.pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return self();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * We use {@link #H79Widening(Polyhedron)} as the standard widening.
     * </p>
     */
    @Override
    public T widening(T y) {
        return H79Widening(y);
    }

    @Override
    public T widening(T y, WideningTokens w) {
        return H79Widening(y, w);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other instanceof Polyhedron<?>) {
            var ph = (Polyhedron<?>) other;
            int result = ppl_Polyhedron_equals_Polyhedron(pplObj, ph.pplObj);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            return result > 0;
        }
        return false;
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Polyhedron(pstr, pplObj);
    }
}
