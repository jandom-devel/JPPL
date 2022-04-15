package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.nativelib.SizeT;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/** A closed polyehdron. */
public class CPolyhedron extends Polyhedron<CPolyhedron> {

    /**
     * Creates a closed polyhedron from the native object pointed by {@code p}.
     */
    public CPolyhedron(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new PolyhedronCleaner(pplObj));
    }

    /**
     * Returns {@code this}.
     *
     * @see Polyhedron#self()
     */
    @Override
    protected CPolyhedron self() {
        return this;
    }

    /**
     * Creates and returns an empty {@code d}-dimensional closed polyhedron.
     *
     * @throws PPLRuntimeException with code {@code LENGTH_ERROR} if {@code d}
     *                             exceeds the maximum allowed space dimension.
     */
    public static CPolyhedron empty(long d) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_space_dimension(pph, new SizeT(d), 1);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns an universe {@code d}-dimensional closed polyhedron,
     * i.e., the full vector space \(\mathbb{R}^d\).
     *
     * @throws PPLRuntimeException with code {@code LENGTH_ERROR} if {@code d}
     *                             exceeds the maximum allowed space dimension.
     */
    public static CPolyhedron universe(long d) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_space_dimension(pph, new SizeT(d), 0);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns a closed polyhedron from the constraints in {@code cs}.
     * The closed polyhedron inherits the space dimension of {@code cs}.
     *
     * @throws PPLRuntimeException with code {@code INVALID_ARGUMENT} if {@code cs}
     *                             contains strict inequalities.
     */
    public static CPolyhedron from(ConstraintSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(ConstraintSystem) from} but after calling this
     * method there is no guarantee on the content of {@code cs}. For increasing
     * performance, its internal data structure might have been reused.
     */
    public static CPolyhedron recycledFrom(ConstraintSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_recycle_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns a closed polyhedron from the congruences in {@code cs}.
     * The closed polyhedron inherits the space dimension of {@code cs}.
     *
     * @throws PPLRuntimeException with code {@code INVALID_ARGUMENT} if any of the
     *                             congruences in {@code cs} is not optimally
     *                             supported.
     */
    public static CPolyhedron from(CongruenceSystem cs) {
        PointerByReference pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Congruence_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(CongruenceSystem) from} but after calling this
     * method there is no guarantee on the content of {@code cs}. For increasing
     * performance, its internal data structure might have been reused.
     */
    public static CPolyhedron recycledFrom(CongruenceSystem cs) {
        PointerByReference pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_recycle_Congruence_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns a closed polyhedron from the generators in {@code gs}.
     * The abstract closed polyhedron the space dimension of {@code gs}.
     *
     * @throws PPLRuntimeException with code {@code INVALID_ARGUMENT} if {@code gs}
     *                             is not empty but has no points, or if it contains
     *                             closure points.
     */
    public static CPolyhedron from(GeneratorSystem gs) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Generator_System(pph, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(GeneratorSystem) from} but after calling this
     * methods, there is no guarantee on the content of {@code gs}. For increasing
     * performance, its internal data structure might have been reused.
     */
    public static CPolyhedron recycledFrom(GeneratorSystem gs) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_recycle_Generator_System(pph, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns a copy of {@code ph}.
     */
    public static CPolyhedron from(CPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_C_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(CPolyhedron)}. The parameter {@code complexity} is
     * ignored.
     */
    public static CPolyhedron from(CPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_C_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns a closed polyhedron representing the topological closure
     * of the NNC polyhedron {@code ph}.
     */
    public static CPolyhedron from(NNCPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_NNC_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(NNCPolyhedron)}. The parameter {@code complexity} is
     * ignored.
     */
    public static CPolyhedron from(NNCPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_NNC_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns a closed polyhedron from a box. The polyhedron inherits
     * the space dimension of the box and is the most precise that includes the box.
     * The algorithm used has polynomial complexity.
     */
    public static CPolyhedron from(DoubleBox box) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Double_Box(pph, box.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(DoubleBox)}. The parameter {@code complexity} is
     * ignored.
     */
    public static CPolyhedron from(DoubleBox box, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Double_Box_with_complexity(pph, box.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    @Override
    CPolyhedron assign(CPolyhedron p) {
        int result = ppl_assign_C_Polyhedron_from_C_Polyhedron(pplObj, p.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public CPolyhedron clone() {
        return from(this);
    }

}
