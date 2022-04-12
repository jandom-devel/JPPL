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

public class DoubleBox extends PPLObject<DoubleBox> implements Property<DoubleBox> {

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
     * Creates a box from a native object.
     */
    DoubleBox(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new DoubleBoxCleaner(pplObj));
    }

    public static DoubleBox empty(long d) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_space_dimension(pbox, new SizeT(d), 1);
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox universe(long d) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_space_dimension(pbox, new SizeT(d), 0);
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox from(ConstraintSystem cs) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Constraint_System(pbox, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox recycledFrom(ConstraintSystem cs){
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_recycle_Constraint_System(pbox, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox from(CongruenceSystem cs) {
        PointerByReference pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Congruence_System(pbox, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox recycledFrom(CongruenceSystem cs) {
        PointerByReference pbox = new PointerByReference();
        int result = ppl_new_Double_Box_recycle_Congruence_System(pbox, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox from(GeneratorSystem gs) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Generator_System(pbox, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox recycledFrom(GeneratorSystem gs) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_recycle_Generator_System(pbox, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox from(DoubleBox box) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Double_Box(pbox, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox from(DoubleBox box, ComplexityClass complexity) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Double_Box_with_complexity(pbox, box.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox from(CPolyhedron ph) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_C_Polyhedron(pbox, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox from(CPolyhedron ph, ComplexityClass complexity) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_NNC_Polyhedron_with_complexity(pbox, ph.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox from(NNCPolyhedron ph) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_NNC_Polyhedron(pbox, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    public static DoubleBox from(NNCPolyhedron ph, ComplexityClass complexity) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_NNC_Polyhedron_with_complexity(pbox, ph.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        return new DoubleBox(pbox.getValue());
    }

    @Override
    public DoubleBox clone() {
        return from(this);
    }

    @Override
    public DoubleBox assign(DoubleBox box) {
        int result = ppl_assign_Double_Box_from_Double_Box(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public long getSpaceDimension() {
        var pd = new SizeTByReference();
        int result = ppl_Double_Box_space_dimension(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    @Override
    public long getAffineDimension() {
        var pd = new SizeTByReference();
        int result = ppl_Double_Box_affine_dimension(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    @Override
    public int getRelationWithConstraint(Constraint c) {
        int result = ppl_Double_Box_relation_with_Constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    @Override
    public int getRelationWithGenerator(Generator g) {
        int result = ppl_Double_Box_relation_with_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    @Override
    public ConstraintSystem getConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_constraints(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new ConstraintSystem(pcs.getValue(), false);
    }

    @Override
    public CongruenceSystem getCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_congruences(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue(), false);
    }

    @Override
    public ConstraintSystem getMinimizedConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_minimized_constraints(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new ConstraintSystem(pcs.getValue(), false);
    }

    @Override
    public CongruenceSystem getMinimizedCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_minimized_congruences(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue(), false);
    }

    @Override
    public boolean isEmpty() {
        int result = ppl_Double_Box_is_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean isUniverse() {
        int result = ppl_Double_Box_is_universe(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean isBounded() {
        int result = ppl_Double_Box_is_bounded(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean containsIntegerPoint() {
        int result = ppl_Double_Box_contains_integer_point(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean isTopologicallyClosed() {
        int result = ppl_Double_Box_is_topologically_closed(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean isDiscrete() {
        int result = ppl_Double_Box_is_discrete(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean constraints(long var) {
        int result = ppl_Double_Box_constrains(pplObj, new SizeT(var));
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean boundsFromAbove(LinearExpression le) {
        int result = ppl_Double_Box_bounds_from_above(pplObj, le.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean boundsFromBelow(LinearExpression le) {
        int result = ppl_Double_Box_bounds_from_below(pplObj, le.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public Optional<ExtremalOutput> maximize(LinearExpression le) {
        var cn = Coefficient.zero();
        var cd = Coefficient.zero();
        var pmaximum = new IntByReference();
        int result = ppl_Double_Box_maximize(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
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
            throw new PPLError(result);
        else if (result == 0)
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
            throw new PPLError(result);
        else if (result == 0)
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
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(cn, cd, pmaximum.getValue() != 0, point.clone()));
    }

    @Override
    public boolean contains(DoubleBox box) {
        int result = ppl_Double_Box_contains_Double_Box(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean strictlyContains(DoubleBox box) {
        int result = ppl_Double_Box_strictly_contains_Double_Box(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean isDisjointFrom(DoubleBox box) {
        int result = ppl_Double_Box_is_disjoint_from_Double_Box(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean isOK() {
        int result = ppl_Double_Box_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public long getExternalMemoryInBytes() {
        var pd = new SizeTByReference();
        int result = ppl_Double_Box_external_memory_in_bytes(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    @Override
    public long getTotalMemoryInBytes() {
        var pd = new SizeTByReference();
        int result = ppl_Double_Box_total_memory_in_bytes(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    @Override
    public DoubleBox addConstraint(Constraint c) {
        int result = ppl_Double_Box_add_constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox addCongruence(Congruence c) {
        int result = ppl_Double_Box_add_congruence(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox addConstraints(ConstraintSystem cs) {
        int result = ppl_Double_Box_add_constraints(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox addCongruences(CongruenceSystem cs) {
        int result = ppl_Double_Box_add_congruences(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox addReycledConstraints(ConstraintSystem cs) {
        int result = ppl_Double_Box_add_recycled_constraints(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox addRecycledCongruences(CongruenceSystem cs) {
        int result = ppl_Double_Box_add_recycled_congruences(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox refineWithConstraint(Constraint c) {
        int result = ppl_Double_Box_refine_with_constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox refineWithCongruence(Congruence c) {
        int result = ppl_Double_Box_refine_with_congruence(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox refineWithConstraints(ConstraintSystem c) {
        int result = ppl_Double_Box_refine_with_constraints(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox refineWithCongruences(CongruenceSystem c) {
        int result = ppl_Double_Box_refine_with_congruences(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox intersectionAssign(DoubleBox box) {
        int result = ppl_Double_Box_intersection_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox upperBoundAssign(DoubleBox box) {
        int result = ppl_Double_Box_upper_bound_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox differenceAssign(DoubleBox box) {
        int result = ppl_Double_Box_difference_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox simplifyUsingContextAssign(DoubleBox box) {
        int result = ppl_Double_Box_difference_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox timeElapseAssign(DoubleBox box) {
        int result = ppl_Double_Box_time_elapse_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox topologicalClosureAssign() {
        int result = ppl_Double_Box_topological_closure_assign(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox unconstrainSpaceDimension(long var) {
        int result = ppl_Double_Box_unconstrain_space_dimension(pplObj, new SizeT(var));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox unconstrainSpaceDimensions(long[] ds) {
        var buffer = new SizeTArray(ds);
        int result = ppl_Double_Box_unconstrain_space_dimensions(pplObj, buffer, new SizeT(ds.length));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox affineImage(long var, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_affine_image(pplObj, new SizeT(var), le.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox affinePreImage(long var, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_affine_preimage(pplObj, new SizeT(var), le.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox boundedAffineImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Double_Box_bounded_affine_image(pplObj, new SizeT(var), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox boundedAffinePreImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Double_Box_bounded_affine_preimage(pplObj, new SizeT(var), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox generalizedAffineImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_generalized_affine_image(pplObj, new SizeT(var), relsym.ordinal(), le.pplObj,
                d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_generalized_affine_preimage(pplObj, new SizeT(var), relsym.ordinal(), le.pplObj,
                d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox generalizedAffineImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs) {
        int result = ppl_Double_Box_generalized_affine_image_lhs_rhs(pplObj, lhs.pplObj, relsym.ordinal(), rhs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox generalizedAffinePreImageLhsRhs(LinearExpression lhs, ConstraintType relsym,
            LinearExpression rhs) {
        int result = ppl_Double_Box_generalized_affine_preimage_lhs_rhs(pplObj, lhs.pplObj, relsym.ordinal(),
                rhs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox concatenateAssign(DoubleBox box) {
        int result = ppl_Double_Box_concatenate_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox addSpaceDimensionsAndEmbed(long d) {
        int result = ppl_Double_Box_add_space_dimensions_and_embed(pplObj, new SizeT(d));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox addSpaceDimensionsAndProject(long d) {
        int result = ppl_Double_Box_add_space_dimensions_and_project(pplObj, new SizeT(d));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox removeSpaceDimensions(long ds[]) {
        int result = ppl_Double_Box_remove_space_dimensions(pplObj, new SizeTArray(ds), new SizeT(ds.length));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox removeHigherSpaceDimensions(long d) {
        int result = ppl_Double_Box_remove_higher_space_dimensions(pplObj, new SizeT(d));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox mapSpaceDimensions(long[] maps) {
        int result = ppl_Double_Box_remove_space_dimensions(pplObj, new SizeTArray(maps), new SizeT(maps.length));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox expandSpaceDimension(long d, long m) {
        int result = ppl_Double_Box_expand_space_dimension(pplObj, new SizeT(d), new SizeT(m));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public DoubleBox foldSpaceDimensions(long[] ds, long d) {
        int result = ppl_Double_Box_fold_space_dimensions(pplObj, new SizeTArray(ds), new SizeT(ds.length),
                new SizeT(d));
        if (result < 0)
            throw new PPLError(result);
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
                throw new PPLError(result);
            return result > 0;
        }
        return false;
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Double_Box(pstr, pplObj);
    }

}
