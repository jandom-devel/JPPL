package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Property.ExtremalOutput;
import it.unich.jppl.Property.WideningToken;
import it.unich.jppl.nativelib.LibPPL.Dimension;
import it.unich.jppl.nativelib.LibPPL.DimensionArray;
import it.unich.jppl.nativelib.LibPPL.DimensionByReference;

import java.util.Optional;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

abstract class Polyhedron<T extends Polyhedron<T> & Property<T>> {

    Pointer pplObj;

    private static class PolyhedronCleaner implements Runnable {
        private Pointer pplObj;

        PolyhedronCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Polyhedron(pplObj);
        }
    }

    protected void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new PolyhedronCleaner(pplObj));
    }

    // This is used to simulate the Scala self-type in Java
    abstract protected T self();

    public long getSpaceDimension() {
        var pd = new DimensionByReference();
        int result = ppl_Polyhedron_space_dimension(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public long getAffineDimension() {
        var pd = new DimensionByReference();
        int result = ppl_Polyhedron_affine_dimension(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public int getRelationWithConstraint(Constraint c) {
        int result = ppl_Polyhedron_relation_with_Constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    /*
    public int getRelationWithGenerator(Generator g) {
        int result = ppl_Polyhedron_relation_with_Generator(obj, g.obj);
        if (result < 0) throw new PPLError(result);
        return resulT;
    }
    */

    public ConstraintSystem getConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_constraints(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new ConstraintSystem(pcs.getValue());
    }

    /*
    public ConstraintSystem getCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_congruences(obj, pcs);
        if (result < 0) throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue());
    }
    */

    public ConstraintSystem getMinimizedConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_minimized_constraints(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new ConstraintSystem(pcs.getValue());
    }

    /*
    public ConstraintSystem getMinimizedCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_minimized_congruences(obj, pcs);
        if (result < 0) throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue());
    }
    */

    public boolean isEmpty() {
        int result = ppl_Polyhedron_is_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isUniverse() {
        int result = ppl_Polyhedron_is_universe(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isBounded() {
        int result = ppl_Polyhedron_is_bounded(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean containsIntegerPoint() {
        int result = ppl_Polyhedron_contains_integer_point(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isTopologicallyClosed() {
        int result = ppl_Polyhedron_is_topologically_closed(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isDiscrete() {
        int result = ppl_Polyhedron_is_discrete(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean constraints(long var) {
        int result = ppl_Polyhedron_constrains(pplObj, new Dimension(var));
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean boundsFromAbove(LinearExpression le) {
        int result = ppl_Polyhedron_bounds_from_above(pplObj, le.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean boundsFromBelow(LinearExpression le) {
        int result = ppl_Polyhedron_bounds_from_below(pplObj, le.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /*
    public boolean maximizeWithPoint(LinearExpression le) {

    }
    */

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
        result = ppl_Polyhedron_maximize(pplObj, le.pplObj, cn, cd, pmaximum);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(new Coefficient(cn), new Coefficient(cd), pmaximum.getValue() != 0));
    }

    /*
    public boolean minimizeWithPoint(LinearExpression le) {

    }
    */

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
        result = ppl_Polyhedron_minimize(pplObj, le.pplObj, cn, cd, pmaximum);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(new Coefficient(cn), new Coefficient(cd), pmaximum.getValue() != 0));
    }

    public boolean contains(T ph) {
        int result = ppl_Polyhedron_contains_Polyhedron(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean strictlyContains(T ph) {
        int result = ppl_Polyhedron_strictly_contains_Polyhedron(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isDisjointFrom(T ph) {
        int result = ppl_Polyhedron_is_disjoint_from_Polyhedron(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isOK() {
        int result = ppl_Polyhedron_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public long getExternalMemoryInBytes() {
        var pd = new DimensionByReference();
        int result = ppl_Polyhedron_external_memory_in_bytes(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public long getTotalMemoryInBytes() {
        var pd = new DimensionByReference();
        int result = ppl_Polyhedron_total_memory_in_bytes(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public T addConstraint(Constraint c) {
        int result = ppl_Polyhedron_add_constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    /*
    public T addCongruence(Congruence c) {
        int result = ppl_Polyhedron_add_congruence(obj, c.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }
    */

    public T addConstraints(ConstraintSystem cs) {
        int result = ppl_Polyhedron_add_constraints(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    /*
    public T addCongruences(CongruenceSystem cs) {
        int result = ppl_Polyhedron_add_congruences(obj, cs.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }
    */

    public T addReycledConstraints(ConstraintSystem cs) {
        int result = ppl_Polyhedron_add_recycled_constraints(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    /*
    public T addReycledGenerators(GeneratorSystem cs) {
        int result = ppl_Polyhedron_add_recycled_generators(obj, cs.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }
    */

    public T refineWithConstraint(Constraint c) {
        int result = ppl_Polyhedron_refine_with_constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    /*
    public T refineWithCongruence(Congruence c) {
        int result = ppl_Polyhedron_refine_with_congruence(obj, c.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }
    */

    public T refineWithConstraints(ConstraintSystem c) {
        int result = ppl_Polyhedron_refine_with_constraints(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    /*
    public T refineWithCongruences(CongruenceSystem c) {
        int result = ppl_Polyhedron_refine_with_congruences(obj, c.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }
    */

    public T intersectionAssign(T ph) {
        int result = ppl_Polyhedron_intersection_assign(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T upperBoundAssign(T ph) {
        int result = ppl_Polyhedron_upper_bound_assign(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T differenceAssign(T ph) {
        int result = ppl_Polyhedron_difference_assign(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T simplofyUsingContextAssign(T ph) {
        int result = ppl_Polyhedron_difference_assign(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T timeElapseAssign(T ph) {
        int result = ppl_Polyhedron_time_elapse_assign(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T topologicalClosureAssign() {
        int result = ppl_Polyhedron_topological_closure_assign(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T unconstrainSpaceDimension(long var) {
        int result = ppl_Polyhedron_unconstrain_space_dimension(pplObj, new Dimension(var));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T unconstrainSpaceDimensions(long[] ds) {
        var buffer = new DimensionArray(ds);
        int result = ppl_Polyhedron_unconstrain_space_dimensions(pplObj, buffer, new Dimension(ds.length));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T affineImage(long var, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_affine_image(pplObj, new Dimension(var), le.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T affinePreImage(long var, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_affine_preimage(pplObj, new Dimension(var), le.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T boundedAffineImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Polyhedron_bounded_affine_image(pplObj, new Dimension(var), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T boundedAffinePreImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Polyhedron_bounded_affine_preimage(pplObj, new Dimension(var), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T generalizedAffineImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_generalized_affine_image(pplObj, new Dimension(var), relsym.ordinal(), le.pplObj,
                d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_generalized_affine_preimage(pplObj, new Dimension(var), relsym.ordinal(), le.pplObj,
                d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T generalizedAffineImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs) {
        int result = ppl_Polyhedron_generalized_affine_image_lhs_rhs(pplObj, lhs.pplObj, relsym.ordinal(), rhs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T generalizedAffinePreImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs) {
        int result = ppl_Polyhedron_generalized_affine_preimage_lhs_rhs(pplObj, lhs.pplObj, relsym.ordinal(), rhs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T concatenateAssign(T ph) {
        int result = ppl_Polyhedron_concatenate_assign(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T addSpaceDimensionsAndEmbed(long d) {
        int result = ppl_Polyhedron_add_space_dimensions_and_embed(pplObj, new Dimension(d));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T addSpaceDimensionsAndProject(long d) {
        int result = ppl_Polyhedron_add_space_dimensions_and_project(pplObj, new Dimension(d));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T removeSpaceDimensions(long ds[]) {
        int result = ppl_Polyhedron_remove_space_dimensions(pplObj, new DimensionArray(ds), new Dimension(ds.length));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T removeHigherSpaceDimensions(long d) {
        int result = ppl_Polyhedron_remove_higher_space_dimensions(pplObj, new Dimension(d));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T mapSpaceDimensions(long[] maps) {
        int result = ppl_Polyhedron_remove_space_dimensions(pplObj, new DimensionArray(maps), new Dimension(maps.length));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T expandSpaceDimension(long d, long m) {
        int result = ppl_Polyhedron_expand_space_dimension(pplObj, new Dimension(d), new Dimension(m));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T foldSpaceDimensions(long[] ds, long d) {
        int result = ppl_Polyhedron_fold_space_dimensions(pplObj, new DimensionArray(ds), new Dimension(ds.length),
                new Dimension(d));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    /*
    public ConstraintSystem getGenerators() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_generators(obj, pcs);
        if (result < 0) throw new PPLError(result);
        return new GeneratorSystem(pcs.getValue());
    }
    */

    /*
    public ConstraintSystem getMinimizedGenerators() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_minimized_generators(obj, pcs);
        if (result < 0) throw new PPLError(result);
        return new GeneratorSystem(pcs.getValue());
    }
    */

    /*
    public T addGenerator(Generator c) {
        int result = ppl_Polyhedron_add_generator(obj, c.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }
    */

    /*
    public T addGenerators(GeneratorSystem cs) {
        int result = ppl_Polyhedron_add_generators(obj, cs.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }
    */

    /*
    public T addRecycledCongruences(CongruenceSystem cs) {
        int result = ppl_Polyhedron_add_recycled_congruences(obj, cs.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }
    */

    public T polyHullAssign(T ph) {
        int result = ppl_Polyhedron_poly_hull_assign(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T polyDifferenceAssign(T ph) {
        int result = ppl_Polyhedron_poly_difference_assign(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T BHRZ03WideningAssign(T ph, WideningToken w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_BHRZ03_widening_assign_with_tokens(pplObj, ph.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T BHRZ03WideningAssign(T ph) {
        int result = ppl_Polyhedron_BHRZ03_widening_assign(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T H79WideningAssign(T ph, WideningToken w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_H79_widening_assign_with_tokens(pplObj, ph.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T H79WideningAssign(T ph) {
        int result = ppl_Polyhedron_H79_widening_assign(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T limitedBHRZ03ExtrapolationAssign(T ph, ConstraintSystem cs, WideningToken w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_limited_BHRZ03_extrapolation_assign_with_tokens(pplObj, ph.pplObj, cs.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T limitedBHRZ03ExtrapolationAssign(T ph, ConstraintSystem cs) {
        int result = ppl_Polyhedron_limited_BHRZ03_extrapolation_assign(pplObj, ph.pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T limitedH79ExtrapolationAssign(T ph, ConstraintSystem cs, WideningToken w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_limited_H79_extrapolation_assign_with_tokens(pplObj, ph.pplObj, cs.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T limitedH79ExtrapolationAssign(T ph, ConstraintSystem cs) {
        int result = ppl_Polyhedron_limited_H79_extrapolation_assign(pplObj, ph.pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T boundedBHRZ03ExtrapolationAssign(T ph, ConstraintSystem cs, WideningToken w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_bounded_BHRZ03_extrapolation_assign_with_tokens(pplObj, ph.pplObj, cs.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T boundedBHRZ03ExtrapolationAssign(T ph, ConstraintSystem cs) {
        int result = ppl_Polyhedron_bounded_BHRZ03_extrapolation_assign(pplObj, ph.pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T boundedH79ExtrapolationAssign(T ph, ConstraintSystem cs, WideningToken w) {
        IntByReference tp = new IntByReference(w.tokens);
        int result = ppl_Polyhedron_bounded_H79_extrapolation_assign_with_tokens(pplObj, ph.pplObj, cs.pplObj, tp);
        w.tokens = tp.getValue();
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T boundedH79ExtrapolationAssign(T ph, ConstraintSystem cs) {
        int result = ppl_Polyhedron_bounded_H79_extrapolation_assign(pplObj, ph.pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other instanceof Polyhedron<?>) {
            var ph = (Polyhedron<?>) other;
            int result = ppl_Polyhedron_equals_Polyhedron(pplObj, ph.pplObj);
            if (result < 0)
                throw new PPLError(result);
            return result > 0;
        }
        return false;
    }

    @Override
    public String toString() {
        var pstr = new PointerByReference();
        ppl_io_asprint_Polyhedron(pstr, pplObj);
        Pointer p = pstr.getValue();
        String s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}