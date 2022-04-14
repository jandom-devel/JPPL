package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/** Not-necessarily closed polyehdron */
public class NNCPolyhedron extends Polyhedron<NNCPolyhedron> {

    public NNCPolyhedron(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new PolyhedronCleaner(pplObj));
    }

    @Override
    protected NNCPolyhedron self() {
        return this;
    }

    public static NNCPolyhedron empty(long d) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_space_dimension(pph, new SizeT(d), 1);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron universe(long d) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_space_dimension(pph, new SizeT(d), 0);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron from(ConstraintSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron recycledFrom(ConstraintSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_recycle_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron from(CongruenceSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Congruence_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron recycledFrom(CongruenceSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_recycle_Congruence_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron from(GeneratorSystem gs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Generator_System(pph, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron recycledFrom(GeneratorSystem gs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_recycle_Generator_System(pph, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron from(NNCPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_NNC_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron from(NNCPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_NNC_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron from(CPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_C_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron from(CPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_C_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron from(DoubleBox box) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Double_Box(pph, box.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    public static NNCPolyhedron from(DoubleBox box, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Double_Box_with_complexity(pph, box.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new NNCPolyhedron(pph.getValue());
    }

    @Override
    public NNCPolyhedron assign(NNCPolyhedron ph) {
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
