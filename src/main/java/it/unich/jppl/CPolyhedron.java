package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/** Closed polyehdron */
public class CPolyhedron extends Polyhedron<CPolyhedron>  {

    public CPolyhedron(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new PolyhedronCleaner(pplObj));
    }

    @Override
    protected CPolyhedron self() {
        return this;
    }

    public static CPolyhedron empty(long d) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_space_dimension(pph, new SizeT(d), 1);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron universe(long d) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_space_dimension(pph, new SizeT(d), 0);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron from(ConstraintSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron recycledFrom(ConstraintSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_recycle_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron from(CongruenceSystem cs) {
        PointerByReference pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Congruence_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron recycledFrom(CongruenceSystem cs) {
        PointerByReference pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_recycle_Congruence_System(pph, cs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron from(GeneratorSystem gs) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Generator_System(pph, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron recycledFrom(GeneratorSystem gs) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_recycle_Generator_System(pph, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron from(CPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_C_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron from(CPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_C_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron from(NNCPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_NNC_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron from(NNCPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_NNC_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron from(DoubleBox box) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Double_Box(pph, box.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    public static CPolyhedron from(DoubleBox box, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Double_Box_with_complexity(pph, box.pplObj, complexity.ordinal());
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new CPolyhedron(pph.getValue());
    }

    @Override
    public CPolyhedron assign(CPolyhedron ph) {
        int result = ppl_assign_C_Polyhedron_from_C_Polyhedron(pplObj, ph.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public CPolyhedron clone() {
        return from(this);
    }

}
