package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Property.ExtremalOutput;
import it.unich.jppl.Property.WideningToken;
import it.unich.jppl.nativelib.LibPPL.SizeT;
import it.unich.jppl.nativelib.LibPPL.SizeTArray;
import it.unich.jppl.nativelib.LibPPL.SizeTByReference;

import java.util.Optional;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

abstract class Polyhedron<T extends Polyhedron<T> & Property<T>> {

    Pointer pplObj;

    private static class PolyhedronCleaner implements Runnable {
        private Pointer pplObj;

        PolyhedronCleaner(Pointer pplObj) {
            this.pplObj = pplObj;
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
        var pd = new SizeTByReference();
        int result = ppl_Polyhedron_space_dimension(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public long getAffineDimension() {
        var pd = new SizeTByReference();
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

    public int getRelationWithGenerator(Generator g) {
        int result = ppl_Polyhedron_relation_with_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    public ConstraintSystem getConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_constraints(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new ConstraintSystem(pcs.getValue());
    }

    public CongruenceSystem getCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_congruences(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue());
    }

    public ConstraintSystem getMinimizedConstraints() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_minimized_constraints(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new ConstraintSystem(pcs.getValue());
    }

    public CongruenceSystem getMinimizedCongruences() {
        var pcs = new PointerByReference();
        int result = ppl_Polyhedron_get_minimized_congruences(pplObj, pcs);
        if (result < 0)
            throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue());
    }

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
        int result = ppl_Polyhedron_constrains(pplObj, new SizeT(var));
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

    public Optional<ExtremalOutput> maximize(LinearExpression le) {
        var cn = new Coefficient();
        var cd = new Coefficient();
        var pmaximum = new IntByReference();
        int result = ppl_Polyhedron_maximize(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(cn, cd, pmaximum.getValue() != 0, null));
    }

    public Optional<ExtremalOutput> maximizeWithPoint(LinearExpression le) {
        var cn = new Coefficient();
        var cd = new Coefficient();
        var point = new Generator();
        var pmaximum = new IntByReference();
        int result = ppl_Polyhedron_maximize_with_point(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum,
                point.pplObj);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(cn, cd, pmaximum.getValue() != 0, new Generator(point)));
    }

    public Optional<ExtremalOutput> minimize(LinearExpression le) {
        var cn = new Coefficient();
        var cd = new Coefficient();
        var pmaximum = new IntByReference();
        int result = ppl_Polyhedron_minimize(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(cn, cd, pmaximum.getValue() != 0, null));
    }

    public Optional<ExtremalOutput> minimizeWithPoint(LinearExpression le) {
        var cn = new Coefficient();
        var cd = new Coefficient();
        var point = new Generator();
        var pmaximum = new IntByReference();
        int result = ppl_Polyhedron_minimize_with_point(pplObj, le.pplObj, cn.pplObj, cd.pplObj, pmaximum,
                point.pplObj);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(cn, cd, pmaximum.getValue() != 0, new Generator(point)));
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
        var pd = new SizeTByReference();
        int result = ppl_Polyhedron_external_memory_in_bytes(pplObj, pd);
        if (result < 0)
            throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public long getTotalMemoryInBytes() {
        var pd = new SizeTByReference();
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

    public T addCongruence(Congruence c) {
        int result = ppl_Polyhedron_add_congruence(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T addConstraints(ConstraintSystem cs) {
        int result = ppl_Polyhedron_add_constraints(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T addCongruences(CongruenceSystem cs) {
        int result = ppl_Polyhedron_add_congruences(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T addReycledConstraints(ConstraintSystem cs) {
        int result = ppl_Polyhedron_add_recycled_constraints(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T addRecycledCongruences(CongruenceSystem cs) {
        int result = ppl_Polyhedron_add_recycled_congruences(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T refineWithConstraint(Constraint c) {
        int result = ppl_Polyhedron_refine_with_constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T refineWithCongruence(Congruence c) {
        int result = ppl_Polyhedron_refine_with_congruence(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T refineWithConstraints(ConstraintSystem c) {
        int result = ppl_Polyhedron_refine_with_constraints(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T refineWithCongruences(CongruenceSystem c) {
        int result = ppl_Polyhedron_refine_with_congruences(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

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

    public T simplifyUsingContextAssign(T ph) {
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
        int result = ppl_Polyhedron_unconstrain_space_dimension(pplObj, new SizeT(var));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T unconstrainSpaceDimensions(long[] ds) {
        var buffer = new SizeTArray(ds);
        int result = ppl_Polyhedron_unconstrain_space_dimensions(pplObj, buffer, new SizeT(ds.length));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T affineImage(long var, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_affine_image(pplObj, new SizeT(var), le.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T affinePreImage(long var, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_affine_preimage(pplObj, new SizeT(var), le.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T boundedAffineImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Polyhedron_bounded_affine_image(pplObj, new SizeT(var), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T boundedAffinePreImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Polyhedron_bounded_affine_preimage(pplObj, new SizeT(var), lb.pplObj, ub.pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T generalizedAffineImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_generalized_affine_image(pplObj, new SizeT(var), relsym.ordinal(), le.pplObj,
                d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_generalized_affine_preimage(pplObj, new SizeT(var), relsym.ordinal(), le.pplObj,
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
        int result = ppl_Polyhedron_generalized_affine_preimage_lhs_rhs(pplObj, lhs.pplObj, relsym.ordinal(),
                rhs.pplObj);
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
        int result = ppl_Polyhedron_add_space_dimensions_and_embed(pplObj, new SizeT(d));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T addSpaceDimensionsAndProject(long d) {
        int result = ppl_Polyhedron_add_space_dimensions_and_project(pplObj, new SizeT(d));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T removeSpaceDimensions(long ds[]) {
        int result = ppl_Polyhedron_remove_space_dimensions(pplObj, new SizeTArray(ds), new SizeT(ds.length));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T removeHigherSpaceDimensions(long d) {
        int result = ppl_Polyhedron_remove_higher_space_dimensions(pplObj, new SizeT(d));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T mapSpaceDimensions(long[] maps) {
        int result = ppl_Polyhedron_remove_space_dimensions(pplObj, new SizeTArray(maps), new SizeT(maps.length));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T expandSpaceDimension(long d, long m) {
        int result = ppl_Polyhedron_expand_space_dimension(pplObj, new SizeT(d), new SizeT(m));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T foldSpaceDimensions(long[] ds, long d) {
        int result = ppl_Polyhedron_fold_space_dimensions(pplObj, new SizeTArray(ds), new SizeT(ds.length),
                new SizeT(d));
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public GeneratorSystem getGenerators() {
        var pgs = new PointerByReference();
        int result = ppl_Polyhedron_get_generators(pplObj, pgs);
        if (result < 0)
            throw new PPLError(result);
        return new GeneratorSystem(pgs.getValue());
    }

    public GeneratorSystem getMinimizedGenerators() {
        var pgs = new PointerByReference();
        int result = ppl_Polyhedron_get_minimized_generators(pplObj, pgs);
        if (result < 0)
            throw new PPLError(result);
        return new GeneratorSystem(pgs.getValue());
    }

    public T addGenerator(Generator g) {
        int result = ppl_Polyhedron_add_generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T addGenerators(GeneratorSystem gs) {
        int result = ppl_Polyhedron_add_generators(pplObj, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

    public T addReycledGenerators(GeneratorSystem gs) {
        int result = ppl_Polyhedron_add_recycled_generators(pplObj, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return self();
    }

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
