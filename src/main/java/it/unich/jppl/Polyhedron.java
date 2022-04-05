package it.unich.jppl;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Property.ExtremalOutput;
import static it.unich.jppl.nativelib.LibPPL.*;

import java.util.Optional;

import com.sun.jna.Pointer;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.IntByReference;

abstract class Polyhedron<T extends Polyhedron<T> & Property<T>> {

    Pointer obj;

    private static class PolyhedronCleaner implements Runnable {
        private Pointer obj;

        PolyhedronCleaner(Pointer obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Polyhedron(obj);
        }
    }

    protected void init(Pointer p) {
        obj = p;
        PPL.cleaner.register(this, new PolyhedronCleaner(obj));
    }

    // This is used to simulate the Scala self-type in Java
    abstract protected T self();

    public long getSpaceDimension() {
        var pd = new DimensionByReference();
        int result = ppl_Polyhedron_space_dimension(obj, pd);
        if (result < 0) throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public long getAffineDimension() {
        var pd = new DimensionByReference();
        int result = ppl_Polyhedron_affine_dimension(obj, pd);
        if (result < 0) throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public int getRelationWithConstraint(Constraint c) {
        int result = ppl_Polyhedron_relation_with_Constraint(obj, c.obj);
        if (result < 0) throw new PPLError(result);
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
        int result = ppl_Polyhedron_get_constraints(obj, pcs);
        if (result < 0) throw new PPLError(result);
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
        int result = ppl_Polyhedron_get_minimized_constraints(obj, pcs);
        if (result < 0) throw new PPLError(result);
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
        int result = ppl_Polyhedron_is_empty(obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean isUniverse() {
        int result = ppl_Polyhedron_is_universe(obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean isBounded() {
        int result = ppl_Polyhedron_is_bounded(obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean containsIntegerPoint() {
        int result = ppl_Polyhedron_contains_integer_point(obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean isTopologicallyClosed() {
        int result = ppl_Polyhedron_is_topologically_closed(obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean isDiscrete() {
        int result = ppl_Polyhedron_is_discrete(obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean constraints(long var) {
        int result = ppl_Polyhedron_constrains(obj, new Dimension(var));
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean boundsFromAbove(LinearExpression le) {
        int result = ppl_Polyhedron_bounds_from_above(obj, le.obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean boundsFromBelow(LinearExpression le) {
        int result = ppl_Polyhedron_bounds_from_below(obj, le.obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    /*
    public boolean maximizeWithPoint(LinearExpression le) {

    }
    */

    public Optional<ExtremalOutput> maximize(LinearExpression le) {
        var pcn = new PointerByReference();
        var result = ppl_new_Coefficient(pcn);
        if (result < 0) throw new PPLError(result);
        var cn = pcn.getValue();
        var pcd = new PointerByReference();
        result = ppl_new_Coefficient(pcd);
        if (result < 0) throw new PPLError(result);
        var cd = pcd.getValue();
        var pmaximum = new IntByReference();
        result = ppl_Polyhedron_maximize(obj, le.obj, cn, cd, pmaximum);
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
        if (result < 0) throw new PPLError(result);
        var cn = pcn.getValue();
        var pcd = new PointerByReference();
        result = ppl_new_Coefficient(pcd);
        if (result < 0) throw new PPLError(result);
        var cd = pcd.getValue();
        var pmaximum = new IntByReference();
        result = ppl_Polyhedron_minimize(obj, le.obj, cn, cd, pmaximum);
        if (result < 0)
            throw new PPLError(result);
        else if (result == 0)
            return Optional.empty();
        else
            return Optional.of(new ExtremalOutput(new Coefficient(cn), new Coefficient(cd), pmaximum.getValue() != 0));
    }

    public boolean contains(T ph) {
        int result = ppl_Polyhedron_contains_Polyhedron(obj, ph.obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean strictlyContains(T ph) {
        int result = ppl_Polyhedron_strictly_contains_Polyhedron(obj, ph.obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean isDisjointFrom(T ph) {
        int result = ppl_Polyhedron_is_disjoint_from_Polyhedron(obj, ph.obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean isOK() {
        int result = ppl_Polyhedron_OK(obj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public long getExternalMemoryInBytes() {
        var pd = new DimensionByReference();
        int result = ppl_Polyhedron_external_memory_in_bytes(obj, pd);
        if (result < 0) throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public long getTotalMemoryInBytes() {
        var pd = new DimensionByReference();
        int result = ppl_Polyhedron_total_memory_in_bytes(obj, pd);
        if (result < 0) throw new PPLError(result);
        return pd.getValue().longValue();
    }

    public T addConstraint(Constraint c) {
        int result = ppl_Polyhedron_add_constraint(obj, c.obj);
        if (result < 0) throw new PPLError(result);
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
        int result = ppl_Polyhedron_add_constraints(obj, cs.obj);
        if (result < 0) throw new PPLError(result);
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
        int result = ppl_Polyhedron_add_recycled_constraints(obj, cs.obj);
        if (result < 0) throw new PPLError(result);
        return self();

    }

/*
    public T addRecycledCongruences(CongruenceSystem cs) {
        int result = ppl_Polyhedron_add_recycled_congruences(obj, cs.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }
*/

    public T refineWithConstraint(Constraint c) {
        int result = ppl_Polyhedron_refine_with_constraint(obj, c.obj);
        if (result < 0) throw new PPLError(result);
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
        int result = ppl_Polyhedron_refine_with_constraints(obj, c.obj);
        if (result < 0) throw new PPLError(result);
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
        int result = ppl_Polyhedron_intersection_assign(obj, ph.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T upperBoundAssign(T ph) {
        int result = ppl_Polyhedron_upper_bound_assign(obj, ph.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T differenceAssign(T ph) {
        int result = ppl_Polyhedron_difference_assign(obj, ph.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T simplofyUsingContextAssign(T ph) {
        int result = ppl_Polyhedron_difference_assign(obj, ph.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T timeElapseAssign(T ph) {
        int result = ppl_Polyhedron_time_elapse_assign(obj, ph.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T topologicalClosureAssign() {
        int result = ppl_Polyhedron_topological_closure_assign(obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T unconstrainSpaceDimension(long var) {
        int result = ppl_Polyhedron_unconstrain_space_dimension(obj, new Dimension(var));
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T unconstrainSpaceDimensions(long[] ds) {
        var buffer = new DimensionArray(ds);
        int result = ppl_Polyhedron_unconstrain_space_dimensions(obj, buffer, new Dimension(ds.length));
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T affineImage(long var, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_affine_image(obj, new Dimension(var), le.obj, d.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T affinePreImage(long var, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_affine_preimage(obj, new Dimension(var), le.obj, d.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T boundedAffineImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Polyhedron_bounded_affine_image(obj, new Dimension(var), lb.obj, ub.obj, d.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T boundedAffinePreImage(long var, LinearExpression lb, LinearExpression ub, Coefficient d) {
        int result = ppl_Polyhedron_bounded_affine_preimage(obj, new Dimension(var), lb.obj, ub.obj, d.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T generalizedAffineImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_generalized_affine_image(obj, new Dimension(var), relsym.pplValue, le.obj, d.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T generalizedAffinePreImage(long var, ConstraintType relsym, LinearExpression le, Coefficient d) {
        int result = ppl_Polyhedron_generalized_affine_preimage(obj, new Dimension(var), relsym.pplValue, le.obj, d.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T generalizedAffineImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs) {
        int result = ppl_Polyhedron_generalized_affine_image_lhs_rhs(obj, lhs.obj, relsym.pplValue, rhs.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T generalizedAffinePreImageLhsRhs(LinearExpression lhs, ConstraintType relsym, LinearExpression rhs) {
        int result = ppl_Polyhedron_generalized_affine_preimage_lhs_rhs(obj, lhs.obj, relsym.pplValue, rhs.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T concatenateAssign(T ph) {
        int result = ppl_Polyhedron_concatenate_assign(obj, ph.obj);
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T addSpaceDimensionsAndEmbed(long d) {
        int result = ppl_Polyhedron_add_space_dimensions_and_embed(obj, new Dimension(d));
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T addSpaceDimensionsAndProject(long d) {
        int result = ppl_Polyhedron_add_space_dimensions_and_project(obj, new Dimension(d));
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T removeSpaceDimensions(long ds[]) {
        int result = ppl_Polyhedron_remove_space_dimensions(obj,  new DimensionArray(ds), new Dimension(ds.length));
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T removeHigherSpaceDimensions(long d) {
        int result = ppl_Polyhedron_remove_higher_space_dimensions(obj, new Dimension(d));
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T mapSpaceDimensions(long[] maps) {
        int result = ppl_Polyhedron_remove_space_dimensions(obj,  new DimensionArray(maps), new Dimension(maps.length));
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T expandSpaceDimension(long d, long m) {
        int result = ppl_Polyhedron_expand_space_dimension(obj, new Dimension(d), new Dimension(m));
        if (result < 0) throw new PPLError(result);
        return self();
    }

    public T foldSpaceDimensions(long[] ds, long d) {
        int result = ppl_Polyhedron_fold_space_dimensions(obj, new DimensionArray(ds), new Dimension(ds.length), new Dimension(d));
        if (result < 0) throw new PPLError(result);
        return self();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other instanceof Polyhedron<?>){
            var ph = (Polyhedron<?>) other;
            int result = ppl_Polyhedron_equals_Polyhedron(obj, ph.obj);
            if (result < 0) throw new PPLError(result);
            return result > 0;
        }
        return false;
    }

    @Override
    public String toString() {
        var pstr = new PointerByReference();
        ppl_io_asprint_Polyhedron(pstr,obj);
        Pointer p = pstr.getValue();
        String s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}

