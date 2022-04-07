package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Domain.ComplexityClass;
import it.unich.jppl.Domain.DegenerateElement;
import it.unich.jppl.Domain.RecycleInput;
import it.unich.jppl.nativelib.LibPPL.Dimension;
import it.unich.jppl.nativelib.LibPPL.DimensionArray;
import it.unich.jppl.nativelib.LibPPL.DimensionByReference;

import java.util.Optional;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class DoubleBox implements Property<DoubleBox> {

    Pointer pplObj;

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

    protected void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new DoubleBoxCleaner(pplObj));
    }

    public DoubleBox(long d, DegenerateElement kind) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_space_dimension(pbox, new Dimension(d),
                kind == DegenerateElement.EMPTY ? 1 : 0);
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(ConstraintSystem cs) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Constraint_System(pbox, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(ConstraintSystem cs, RecycleInput dummy) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_recycle_Constraint_System(pbox, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(CongruenceSystem cs) {
        PointerByReference pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Congruence_System(pbox, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(CongruenceSystem cs, RecycleInput dummy) {
        PointerByReference pbox = new PointerByReference();
        int result = ppl_new_Double_Box_recycle_Congruence_System(pbox, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(GeneratorSystem gs) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Generator_System(pbox, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(GeneratorSystem gs, RecycleInput dummy) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_recycle_Generator_System(pbox, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(DoubleBox box) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Double_Box(pbox, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(DoubleBox box, ComplexityClass complexity) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_Double_Box_with_complexity(pbox, box.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(CPolyhedron ph) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_C_Polyhedron(pbox, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(CPolyhedron ph, ComplexityClass complexity) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_NNC_Polyhedron_with_complexity(pbox, ph.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(NNCPolyhedron ph) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_NNC_Polyhedron(pbox, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox(NNCPolyhedron ph, ComplexityClass complexity) {
        var pbox = new PointerByReference();
        int result = ppl_new_Double_Box_from_NNC_Polyhedron_with_complexity(pbox, ph.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        init(pbox.getValue());
    }

    public DoubleBox assign(DoubleBox box) {
        int result = ppl_assign_Double_Box_from_Double_Box(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public long getSpaceDimension() {
        var pd = new DimensionByReference();
        int result = ppl_Double_Box_space_dimension(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public long getAffineDimension() {
        var pd = new DimensionByReference();
        int result = ppl_Double_Box_affine_dimension(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public int getRelationWithConstraint(Constraint c) {
        int result = ppl_Double_Box_relation_with_Constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    public int getRelationWithGenerator(Generator g) {
        int result = ppl_Double_Box_relation_with_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    public ConstraintSystem getConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_constraints(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new ConstraintSystem(pcs.getValue());
    }

    public CongruenceSystem getCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_congruences(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue());
    }

    public ConstraintSystem getMinimizedConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_minimized_constraints(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new ConstraintSystem(pcs.getValue());
    }

    public CongruenceSystem getMinimizedCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Double_Box_get_minimized_congruences(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue());
    }

    public boolean isEmpty() {
        int result = ppl_Double_Box_is_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isUniverse() {
        int result = ppl_Double_Box_is_universe(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isBounded() {
        int result = ppl_Double_Box_is_bounded(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean containsIntegerPoint() {
        int result = ppl_Double_Box_contains_integer_point(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isTopologicallyClosed() {
        int result = ppl_Double_Box_is_topologically_closed(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isDiscrete() {
        int result = ppl_Double_Box_is_discrete(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean constraints(long var) {
        int result = ppl_Double_Box_constrains(pplObj, new Dimension(var));
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean boundsFromAbove(LinearExpression le) {
        int result = ppl_Double_Box_bounds_from_above(pplObj, le.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean boundsFromBelow(LinearExpression le) {
        int result = ppl_Double_Box_bounds_from_below(pplObj, le.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public Optional<ExtremalOutput> maximize(LinearExpression le) {
        var pcn = new PointerByReference();
        var result = ppl_new_Coefficient(pcn);
        if (result < 0)
            throw new PPLError(result);
        var cn = pcn.getValue();
        var pcd = new PointerByReference();
        result = ppl_new_Coefficient(pcd);
        if (result < 0)
            throw new PPLError(result);
        var cd = pcd.getValue();
        var pmaximum = new IntByReference();
        result = ppl_Double_Box_maximize(pplObj, le.pplObj, cn, cd, pmaximum);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional
                    .of(new ExtremalOutput(new Coefficient(cn), new Coefficient(cd), pmaximum.getValue() != 0, null));
    }

    public Optional<ExtremalOutput> maximizeWithPoint(LinearExpression le) {
        var pcn = new PointerByReference();
        var result = ppl_new_Coefficient(pcn);
        if (result < 0)
            throw new PPLError(result);
        var cn = pcn.getValue();
        var pcd = new PointerByReference();
        result = ppl_new_Coefficient(pcd);
        if (result < 0)
            throw new PPLError(result);
        var cd = pcd.getValue();
        var pmaximum = new IntByReference();
        var ppoint = new PointerByReference();
        result = ppl_new_Generator_zero_dim_point(ppoint);
        if (result < 0)
            throw new PPLError(result);
        var point = ppoint.getValue();
        result = ppl_Double_Box_maximize_with_point(pplObj, le.pplObj, cn, cd, pmaximum, point);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(new Coefficient(cn), new Coefficient(cd), pmaximum.getValue() != 0,
                    new Generator(point)));
    }

    public Optional<ExtremalOutput> minimize(LinearExpression le) {
        var pcn = new PointerByReference();
        var result = ppl_new_Coefficient(pcn);
        if (result < 0)
            throw new PPLError(result);
        var cn = pcn.getValue();
        var pcd = new PointerByReference();
        result = ppl_new_Coefficient(pcd);
        if (result < 0)
            throw new PPLError(result);
        var cd = pcd.getValue();
        var pmaximum = new IntByReference();
        result = ppl_Double_Box_minimize(pplObj, le.pplObj, cn, cd, pmaximum);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional
                    .of(new ExtremalOutput(new Coefficient(cn), new Coefficient(cd), pmaximum.getValue() != 0, null));
    }

    public Optional<ExtremalOutput> minimizeWithPoint(LinearExpression le) {
        var pcn = new PointerByReference();
        var result = ppl_new_Coefficient(pcn);
        if (result < 0)
            throw new PPLError(result);
        var cn = pcn.getValue();
        var pcd = new PointerByReference();
        result = ppl_new_Coefficient(pcd);
        if (result < 0)
            throw new PPLError(result);
        var cd = pcd.getValue();
        var pmaximum = new IntByReference();
        var ppoint = new PointerByReference();
        result = ppl_new_Generator_zero_dim_point(ppoint);
        if (result < 0)
            throw new PPLError(result);
        var point = ppoint.getValue();
        result = ppl_Double_Box_minimize_with_point(pplObj, le.pplObj, cn, cd, pmaximum, point);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(new Coefficient(cn), new Coefficient(cd), pmaximum.getValue() != 0,
                    new Generator(point)));
    }

    public boolean contains(DoubleBox box) {
        int result = ppl_Double_Box_contains_Double_Box(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean strictlyContains(DoubleBox box) {
        int result = ppl_Double_Box_strictly_contains_Double_Box(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isDisjointFrom(DoubleBox box) {
        int result = ppl_Double_Box_is_disjoint_from_Double_Box(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isOK() {
        int result = ppl_Double_Box_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public long getExternalMemoryInBytes() {
        var pd = new DimensionByReference();
        int result = ppl_Double_Box_external_memory_in_bytes(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public long getTotalMemoryInBytes() {
        var pd = new DimensionByReference();
        int result = ppl_Double_Box_total_memory_in_bytes(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public DoubleBox addConstraint(Constraint c) {
        int result = ppl_Double_Box_add_constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox addCongruence(Congruence c) {
        int result = ppl_Double_Box_add_congruence(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox addConstraints(ConstraintSystem cs) {
        int result = ppl_Double_Box_add_constraints(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox addCongruences(CongruenceSystem cs) {
        int result = ppl_Double_Box_add_congruences(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox addReycledConstraints(ConstraintSystem cs) {
        int result = ppl_Double_Box_add_recycled_constraints(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox addRecycledCongruences(CongruenceSystem cs) {
        int result = ppl_Double_Box_add_recycled_congruences(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox refineWithConstraint(Constraint c) {
        int result = ppl_Double_Box_refine_with_constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox refineWithCongruence(Congruence c) {
        int result = ppl_Double_Box_refine_with_congruence(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox refineWithConstraints(ConstraintSystem c) {
        int result = ppl_Double_Box_refine_with_constraints(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox refineWithCongruences(CongruenceSystem c) {
        int result = ppl_Double_Box_refine_with_congruences(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox intersectionAssign(DoubleBox box) {
        int result = ppl_Double_Box_intersection_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox upperBoundAssign(DoubleBox box) {
        int result = ppl_Double_Box_upper_bound_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox differenceAssign(DoubleBox box) {
        int result = ppl_Double_Box_difference_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox simplofyUsingContextAssign(DoubleBox box) {
        int result = ppl_Double_Box_difference_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox timeElapseAssign(DoubleBox box) {
        int result = ppl_Double_Box_time_elapse_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox topologicalClosureAssign() {
        int result = ppl_Double_Box_topological_closure_assign(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox unconstrainSpaceDimension(long var) {
        int result = ppl_Double_Box_unconstrain_space_dimension(pplObj, new Dimension(var));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox unconstrainSpaceDimensions(long[] ds) {
        var buffer = new DimensionArray(ds);
        int result = ppl_Double_Box_unconstrain_space_dimensions(pplObj, buffer, new Dimension(ds.length));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox affineImage(long var, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_affine_image(pplObj, new Dimension(var), le.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox affinePreImage(long var, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_affine_preimage(pplObj, new Dimension(var), le.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox boundedAffineImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Double_Box_bounded_affine_image(pplObj, new Dimension(var), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox boundedAffinePreImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Double_Box_bounded_affine_preimage(pplObj, new Dimension(var), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox generalizedAffineImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_generalized_affine_image(pplObj, new Dimension(var), relsym.ordinal(), le.pplObj,
                d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Double_Box_generalized_affine_preimage(pplObj, new Dimension(var), relsym.ordinal(), le.pplObj,
                d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox generalizedAffineImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs) {
        int result = ppl_Double_Box_generalized_affine_image_lhs_rhs(pplObj, lhs.pplObj, relsym.ordinal(), rhs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox generalizedAffinePreImageLhsRhs(LinearExpression lhs, ConstraintType relsym,
            LinearExpression rhs) {
        int result = ppl_Double_Box_generalized_affine_preimage_lhs_rhs(pplObj, lhs.pplObj, relsym.ordinal(),
                rhs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox concatenateAssign(DoubleBox box) {
        int result = ppl_Double_Box_concatenate_assign(pplObj, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox addSpaceDimensionsAndEmbed(long d) {
        int result = ppl_Double_Box_add_space_dimensions_and_embed(pplObj, new Dimension(d));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox addSpaceDimensionsAndProject(long d) {
        int result = ppl_Double_Box_add_space_dimensions_and_project(pplObj, new Dimension(d));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox removeSpaceDimensions(long ds[]) {
        int result = ppl_Double_Box_remove_space_dimensions(pplObj, new DimensionArray(ds), new Dimension(ds.length));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox removeHigherSpaceDimensions(long d) {
        int result = ppl_Double_Box_remove_higher_space_dimensions(pplObj, new Dimension(d));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox mapSpaceDimensions(long[] maps) {
        int result = ppl_Double_Box_remove_space_dimensions(pplObj, new DimensionArray(maps),
                new Dimension(maps.length));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox expandSpaceDimension(long d, long m) {
        int result = ppl_Double_Box_expand_space_dimension(pplObj, new Dimension(d), new Dimension(m));
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public DoubleBox foldSpaceDimensions(long[] ds, long d) {
        int result = ppl_Double_Box_fold_space_dimensions(pplObj, new DimensionArray(ds), new Dimension(ds.length),
                new Dimension(d));
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
    public String toString() {
        var pstr = new PointerByReference();
        ppl_io_asprint_Double_Box(pstr, pplObj);
        Pointer p = pstr.getValue();
        String s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
