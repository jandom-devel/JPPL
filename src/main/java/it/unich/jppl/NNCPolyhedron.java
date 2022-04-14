package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/** A not-necessarily closed (NNC) polyehdron */
public class NNCPolyhedron extends Polyhedron<NNCPolyhedron> {

    /**
     * Creates an NNC polyhedron from the native object pointed by {@code p}.
     */
    public NNCPolyhedron(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new PolyhedronCleaner(pplObj));
    }

    /**
     * Returns {@code this}.
     *
     * @see Polyhedron#self()
     */
    @Override
    protected NNCPolyhedron self() {
        return this;
    }

    /**
     * Creates and returns an empty {@code d}-dimensional NNC polyhedron.
     *
     * @throws PPLRuntimeException with code {@code LENGTH_ERROR} if {@code d}
     *                             exceeds the maximum allowed space dimension.
     */
    public static NNCPolyhedron empty(long d) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_space_dimension(pph, new SizeT(d), 1);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns an universe {@code d}-dimensional NNC polyhedron, i.e.,
     * the full vector space \(\mathbb{R}^d\).
     *
     * @throws PPLRuntimeException with code {@code LENGTH_ERROR} if {@code d}
     *                             exceeds the maximum allowed space dimension.
     */
    public static NNCPolyhedron universe(long d) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_space_dimension(pph, new SizeT(d), 0);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns a NNC polyhedron from the constraints in {@code cs}. The
     * NNC polyhedron inherits the space dimension of {@code cs}.
     */
    public static NNCPolyhedron from(ConstraintSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(ConstraintSystem) from}, but after calling this
     * methods, there is no guarantee on the content of {@code cs}. supported.
     */
    public static NNCPolyhedron recycledFrom(ConstraintSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_recycle_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns a NNC polyhedron from the congruences in {@code cs}. The
     * NNC polyhedron inherits the space dimension of {@code cs}.
     *
     * @throws PPLRuntimeException with code {@code INVALID_ARGUMENT} if any of the
     *                             congruences in {@code cs} is not optimally
     *                             supported.
     */
    public static NNCPolyhedron from(CongruenceSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Congruence_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(CongruenceSystem) from}, but after calling this
     * methods, there is no guarantee on the content of {@code cs}. supported.
     */
    public static NNCPolyhedron recycledFrom(CongruenceSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_recycle_Congruence_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns a NNC polyhedron from the generators in {@code gs}. The
     * NNC polyhedron the space dimension of {@code gs}.
     *
     * @throws PPLRuntimeException with code {@code INVALID_ARGUMENT} if {@code gs}
     *                             is not empty but has no points.
     */
    public static NNCPolyhedron from(GeneratorSystem gs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Generator_System(pph, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(GeneratorSystem) from}, but after calling this
     * methods, there is no guarantee on the content of {@code gs}. supported.
     */
    public static NNCPolyhedron recycledFrom(GeneratorSystem gs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_recycle_Generator_System(pph, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns a copy of {@code ph}.
     */
    public static NNCPolyhedron from(NNCPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_NNC_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(NNCPolyhedron)}. The parameter {@code complexity} is
     * ignored.
     */
    public static NNCPolyhedron from(NNCPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_NNC_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns an NNC polyhedron from a closed polyhedron.
     */
    public static NNCPolyhedron from(CPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_C_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(CPolyhedron)}. The parameter {@code complexity} is
     * ignored.
     */
    public static NNCPolyhedron from(CPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_C_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Creates and returns a NNC polyhedron from a box. The polyhedron inherits the
     * space dimension of the box and is the most precise that includes the box. The
     * algorithm used has polynomial complexity.
     */
    public static NNCPolyhedron from(DoubleBox box) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Double_Box(pph, box.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    /**
     * Similar to {@link #from(DoubleBox)}. The parameter {@code complexity} is
     * ignored.
     */
    public static NNCPolyhedron from(DoubleBox box, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Double_Box_with_complexity(pph, box.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    @Override
    NNCPolyhedron assign(NNCPolyhedron ph) {
        int result = ppl_assign_NNC_Polyhedron_from_NNC_Polyhedron(pplObj, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public NNCPolyhedron clone() {
        return from(this);
    }

}
