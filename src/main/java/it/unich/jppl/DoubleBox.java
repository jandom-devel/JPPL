package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.nativelib.SizeT;
import it.unich.jppl.nativelib.SizeTArray;
import it.unich.jppl.nativelib.SizeTByReference;

import java.util.Optional;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * A box with bounds implemented using native doubles.
 */
public class DoubleBox extends AbstractPPLObject<DoubleBox> implements Property<DoubleBox> {

    private static class DoubleBoxCleaner implements Runnable {
        private Pointer pplObj;

        DoubleBoxCleaner(Pointer pplObj) {
            this.pplObj = pplObj;
        }

        @Override
        public void run() {
            ppl_delete_Double_Box(pplObj);
        }
    }

    /**
     * Creates a closed polyhedron from the native object pointed by {@code p}.
     */
    DoubleBox(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new DoubleBoxCleaner(pplObj));
    }

    /**
     * Creates and returns an empty {@code d}-dimensional box.
     *
     * @throws PPLRuntimeException with code {@code LENGTH_ERROR} if {@code d}
     *                             exceeds the maximum allowed space dimension.
     */
    public static DoubleBox empty(long d) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_space_dimension(pbox, new SizeT(d), 1);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Creates and returns an universe {@code d}-dimensional box, i.e., the full
     * vector space \(\mathbb{R}^d\).
     *
     * @throws PPLRuntimeException with code {@code LENGTH_ERROR} if {@code d}
     *                             exceeds the maximum allowed space dimension.
     */
    public static DoubleBox universe(long d) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_space_dimension(pbox, new SizeT(d), 0);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Creates and returns a boxn from the constraints in {@code cs}. The box
     * inherits the space dimension of {@code cs}.
     *
     * @throws PPLRuntimeException with code {@code INVALID_ARGUMENT} if any of the
     *                             constraints in {@code cs} is not optimally
     *                             supported.
     */
    public static DoubleBox from(ConstraintSystem cs) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Constraint_System(pbox, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Similar to {@link #from(ConstraintSystem) from} but after calling this method
     * there is no guarantee on the content of {@code cs}. For increasing
     * performance, its internal data structure might have been reused.
     */
    public static DoubleBox recycledFrom(ConstraintSystem cs) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_recycle_Constraint_System(pbox, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Creates and returns a box from the congruences in {@code cs}. The box
     * inherits the space dimension of {@code cs}.
     *
     * @throws PPLRuntimeException with code {@code INVALID_ARGUMENT} if any of the
     *                             congruences in {@code cs} is not optimally
     *                             supported.
     */
    public static DoubleBox from(CongruenceSystem cs) {
        PointerByReference pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Congruence_System(pbox, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Similar to {@link #from(CongruenceSystem) from} but after calling this method
     * there is no guarantee on the content of {@code cs}. For increasing
     * performance, its internal data structure might have been reused.
     */
    public static DoubleBox recycledFrom(CongruenceSystem cs) {
        PointerByReference pbox = new PointerByReference();
        int result = ppl_new_Double_Box_recycle_Congruence_System(pbox, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Creates and returns a box from the generators in {@code gs}. The box inherits
     * the space dimension of {@code gs}.
     *
     * @throws PPLRuntimeException with code {@code INVALID_ARGUMENT} if any of the
     *                             generators in {@code gs} is not optimally
     *                             supported.
     */
    public static DoubleBox from(GeneratorSystem gs) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Generator_System(pbox, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Similar to {@link #from(GeneratorSystem) from} but after calling this method
     * there is no guarantee on the content of {@code gs}. For increasing
     * performance, its internal data structure might have been reused.
     */
    public static DoubleBox recycledFrom(GeneratorSystem gs) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_recycle_Generator_System(pbox, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Creates and returns a copy of {@code box}.
     */
    public static DoubleBox from(DoubleBox box) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Double_Box(pbox, box.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Creates and returns a copy of {@code box}. The parameter {@code complexity}
     * is ignored.
     */
    public static DoubleBox from(DoubleBox box, ComplexityClass complexity) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Double_Box_with_complexity(pbox, box.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Creates and returns a box containing {@code ph}.
     */
    public static DoubleBox from(CPolyhedron ph) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_C_Polyhedron(pbox, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Creates and returns a box containing {@code ph}. If {@code complexity} is
     * {@code ANY_COMPLEXITY}, then the built box is the smallest one containing
     * {@code ph}.
     */
    public static DoubleBox from(CPolyhedron ph, ComplexityClass complexity) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_NNC_Polyhedron_with_complexity(pbox, ph.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Creates and returns a box containing {@code ph}.
     */
    public static DoubleBox from(NNCPolyhedron ph) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_NNC_Polyhedron(pbox, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    /**
     * Creates and returns a box containing {@code ph}. If {@code complexity} is
     * {@code ANY_COMPLEXITY}, then the built box is the smallest one containing
     * {@code ph}.
     */
    public static DoubleBox from(NNCPolyhedron ph, ComplexityClass complexity) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_NNC_Polyhedron_with_complexity(pbox, ph.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new DoubleBox(pbox.getValue());
    }

    @Override
    public DoubleBox clone() {
        return from(this);
    }

    @Override
    DoubleBox assign(DoubleBox box) {
        int result = ppl_assign_Double_Box_from_Double_Box(pplObj, box.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public long getSpaceDimension() {
        var pd = new SizeTByReference();
        int result = ppl_Double_Box_space_dimension(pplObj, pd);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return pd.getValue().longValue();
    }

    @Override
    public long getAffineDimension() {
        var pd = new SizeTByReference();
        int result = ppl_Double_Box_affine_dimension(pplObj, pd);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return pd.getValue().longValue();
    }

    @Override
    public int getRelationWith(Constraint c) {
        int result = ppl_Double_Box_relation_with_Constraint(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result;
    }

    @Override
    public int getRelationWith(Generator g) {
        int result = ppl_Double_Box_relation_with_Generator(pplObj, g.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result;
    }

    @Override
    public ConstraintSystem getConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_constraints(pplObj, pcs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new ConstraintSystem(pcs.getValue(), false);
    }

    @Override
    public CongruenceSystem getCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_congruences(pplObj, pcs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CongruenceSystem(pcs.getValue(), false);
    }

    @Override
    public ConstraintSystem getMinimizedConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_minimized_constraints(pplObj, pcs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new ConstraintSystem(pcs.getValue(), false);
    }

    @Override
    public CongruenceSystem getMinimizedCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_minimized_congruences(pplObj, pcs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CongruenceSystem(pcs.getValue(), false);
    }

    @Override
    public boolean isEmpty() {
        int result = ppl_Double_Box_is_empty(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean isUniverse() {
        int result = ppl_Double_Box_is_universe(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean isBounded() {
        int result = ppl_Double_Box_is_bounded(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean containsIntegerPoint() {
        int result = ppl_Double_Box_contains_integer_point(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean isTopologicallyClosed() {
        int result = ppl_Double_Box_is_topologically_closed(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean isDiscrete() {
        int result = ppl_Double_Box_is_discrete(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean constraints(long i) {
        int result = ppl_Double_Box_constrains(pplObj, new SizeT(i));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean boundsFromAbove(LinearExpression le) {
        int result = ppl_Double_Box_bounds_from_above(pplObj, le.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean boundsFromBelow(LinearExpression le) {
        int result = ppl_Double_Box_bounds_from_below(pplObj, le.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public Optional<ExtremalOutput> maximize(LinearExpression le) {
        var cn = Coefficient.zero();
        var cd = Coefficient.zero();
        var pmaximum = new IntByReference();
        int result = ppl_Double_Box_maximize(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum);
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
        int result = ppl_Double_Box_maximize_with_point(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum,
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
        int result = ppl_Double_Box_minimize(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum);
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
        int result = ppl_Double_Box_minimize_with_point(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum,
                point.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(cn, cd, pmaximum.getValue() != 0, point.clone()));
    }

    @Override
    public boolean contains(DoubleBox y) {
        int result = ppl_Double_Box_contains_Double_Box(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean strictlyContains(DoubleBox y) {
        int result = ppl_Double_Box_strictly_contains_Double_Box(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean isDisjointFrom(DoubleBox y) {
        int result = ppl_Double_Box_is_disjoint_from_Double_Box(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    boolean isOK() {
        int result = ppl_Double_Box_OK(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public long getExternalMemoryInBytes() {
        var pd = new SizeTByReference();
        int result = ppl_Double_Box_external_memory_in_bytes(pplObj, pd);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return pd.getValue().longValue();
    }

    @Override
    public long getTotalMemoryInBytes() {
        var pd = new SizeTByReference();
        int result = ppl_Double_Box_total_memory_in_bytes(pplObj, pd);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return pd.getValue().longValue();
    }

    @Override
    public DoubleBox add(Constraint c) {
        int result = ppl_Double_Box_add_constraint(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox add(Congruence c) {
        int result = ppl_Double_Box_add_congruence(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox add(ConstraintSystem cs) {
        int result = ppl_Double_Box_add_constraints(pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox add(CongruenceSystem cs) {
        int result = ppl_Double_Box_add_congruences(pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox addReycled(ConstraintSystem cs) {
        int result = ppl_Double_Box_add_recycled_constraints(pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox addRecycled(CongruenceSystem cs) {
        int result = ppl_Double_Box_add_recycled_congruences(pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox refineWith(Constraint c) {
        int result = ppl_Double_Box_refine_with_constraint(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox refineWith(Congruence c) {
        int result = ppl_Double_Box_refine_with_congruence(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox refineWith(ConstraintSystem c) {
        int result = ppl_Double_Box_refine_with_constraints(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox refineWith(CongruenceSystem c) {
        int result = ppl_Double_Box_refine_with_congruences(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox intersection(DoubleBox y) {
        int result = ppl_Double_Box_intersection_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox upperBound(DoubleBox y) {
        int result = ppl_Double_Box_upper_bound_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox difference(DoubleBox y) {
        int result = ppl_Double_Box_difference_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox simplifyUsingContext(DoubleBox y) {
        int result = ppl_Double_Box_difference_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox timeElapse(DoubleBox y) {
        int result = ppl_Double_Box_time_elapse_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox topologicalClosure() {
        int result = ppl_Double_Box_topological_closure_assign(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox unconstrain(long i) {
        int result = ppl_Double_Box_unconstrain_space_dimension(pplObj, new SizeT(i));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox unconstrain(long[] ds) {
        var buffer = new SizeTArray(ds);
        int result = ppl_Double_Box_unconstrain_space_dimensions(pplObj, buffer, new SizeT(ds.length));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox affineImage(long i, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_affine_image(pplObj, new SizeT(i), le.pplObj, d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox affinePreImage(long i, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_affine_preimage(pplObj, new SizeT(i), le.pplObj, d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox boundedAffineImage(long i, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Double_Box_bounded_affine_image(pplObj, new SizeT(i), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox boundedAffinePreImage(long i, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Double_Box_bounded_affine_preimage(pplObj, new SizeT(i), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox generalizedAffineImage(long i, ConstraintType rel, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_generalized_affine_image(pplObj, new SizeT(i), rel.ordinal(), le.pplObj, d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox generalizedAffinePreImage(long i, ConstraintType rel, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_generalized_affine_preimage(pplObj, new SizeT(i), rel.ordinal(), le.pplObj,
                d.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox generalizedAffineImageLhsRhs(LinearExpression lhs, ConstraintType rel, LinearExpression rhs) {
        int result = ppl_Double_Box_generalized_affine_image_lhs_rhs(pplObj, lhs.pplObj, rel.ordinal(), rhs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox generalizedAffinePreImageLhsRhs(LinearExpression lhs, ConstraintType rel, LinearExpression rhs) {
        int result = ppl_Double_Box_generalized_affine_preimage_lhs_rhs(pplObj, lhs.pplObj, rel.ordinal(), rhs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox concatenate(DoubleBox y) {
        int result = ppl_Double_Box_concatenate_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox addSpaceDimensionsAndEmbed(long m) {
        int result = ppl_Double_Box_add_space_dimensions_and_embed(pplObj, new SizeT(m));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox addSpaceDimensionsAndProject(long m) {
        int result = ppl_Double_Box_add_space_dimensions_and_project(pplObj, new SizeT(m));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox removeSpaceDimensions(long ds[]) {
        int result = ppl_Double_Box_remove_space_dimensions(pplObj, new SizeTArray(ds), new SizeT(ds.length));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox removeHigherSpaceDimensions(long d) {
        int result = ppl_Double_Box_remove_higher_space_dimensions(pplObj, new SizeT(d));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox mapSpaceDimensions(long[] maps) {
        int result = ppl_Double_Box_remove_space_dimensions(pplObj, new SizeTArray(maps), new SizeT(maps.length));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox expandSpaceDimension(long i, long m) {
        int result = ppl_Double_Box_expand_space_dimension(pplObj, new SizeT(i), new SizeT(m));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox foldSpaceDimensions(long[] ds, long i) {
        int result = ppl_Double_Box_fold_space_dimensions(pplObj, new SizeTArray(ds), new SizeT(ds.length),
                new SizeT(i));
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    /**
     * Assigns to this box its CC76-widening with {@code y}. The box {@code y} must be
     * contained in (or be equal to) {@code this}.
     *
     * @return this box.
     */
    public DoubleBox CC76Widening(DoubleBox y) {
        int result = ppl_Double_Box_CC76_widening_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    /**
     * Variant of {@link #CC76Widening(DoubleBox) CC76Widening} which also applies
     * the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Widening_with_Tokens">widening
     * with tokens</a> delay tecnique.
     *
     * @param w number of available tokens.
     * @return this box.
     */
    public DoubleBox CC76Widening(DoubleBox y, WideningTokens w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Double_Box_CC76_widening_assign_with_tokens(pplObj, y.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Same as {@link #CC76Widening(DoubleBox) CC76Widening}.
     * </p>
     */
    @Override
    public DoubleBox widening(DoubleBox y) {
        int result = ppl_Double_Box_widening_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public DoubleBox widening(DoubleBox y, WideningTokens w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Double_Box_widening_assign_with_tokens(pplObj, y.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    /**
     * Assigns to this box its CC76-narrowing with {@code y}. The box {@code y} must
     * contain {@code this}.
     *
     * @return this box.
     */
    public DoubleBox CC76Narrowing(DoubleBox y) {
        int result = ppl_Double_Box_CC76_narrowing_assign(pplObj, y.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    /**
     * Assigns to this its CC76-widening with {@code y} using limited extrapolation.
     * It means that the result is intersected with the constraints in cs that are
     * satisfied by all the points of {@code this}. The box {@code y} must be
     * contained in (or be equal to) {@code this}.
     *
     * @return this box.
     */
    public DoubleBox limitedCC76Extrapolation(DoubleBox y, ConstraintSystem cs) {
        int result = ppl_Double_Box_limited_CC76_extrapolation_assign(pplObj, y.pplObj, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    /**
     * Variant of {@link #limitedCC76Extrapolation(DoubleBox, ConstraintSystem)
     * limitedCC76Extrapolation} which also applies the <a href=
     * "https://www.bugseng.com/products/ppl/documentation//devref/ppl-devref-1.2-html/index.html#Widening_with_Tokens">widening
     * with tokens</a> delay tecnique.
     *
     * @param w number of available tokens.
     * @return this box.
     */
    public DoubleBox limitedCC76Extrapolation(DoubleBox y, ConstraintSystem cs, WideningTokens w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Double_Box_limited_CC76_extrapolation_assign_with_tokens(pplObj, y.pplObj, cs.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other instanceof DoubleBox) {
            var box = (DoubleBox) other;
            int result = ppl_Double_Box_equals_Double_Box(pplObj, box.pplObj);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            return result > 0;
        }
        return false;
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Double_Box(pstr, pplObj);
    }

}
