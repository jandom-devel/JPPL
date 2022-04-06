package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.Domain.ComplexityClass;
import it.unich.jppl.Domain.DegenerateElement;
import it.unich.jppl.Domain.RecycleInput;
import it.unich.jppl.nativelib.LibPPL.Dimension;

import com.sun.jna.ptr.PointerByReference;

public class CPolyhedron extends Polyhedron<CPolyhedron> implements Property<CPolyhedron> {
    protected CPolyhedron self() {
        return this;
    }

    public CPolyhedron(long d, DegenerateElement kind) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_space_dimension(pph, new Dimension(d),
                kind == DegenerateElement.EMPTY ? 1 : 0);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron(CPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_C_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron(CPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_C_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron(ConstraintSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron(ConstraintSystem cs, RecycleInput dummy) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_recycle_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    /*
    public CPolyhedron(CongruenceSystem cs) {
        PointerByReference pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Congruence_System(pph, cs.obj);
        if (result < 0) throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron(CongruenceSystem cs, RecycleInput dummy) {
        PointerByReference pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_recycle_Congruence_System(pph, cs.obj);
        if (result < 0) throw new PPLError(result);
        init(pph.getValue());
    }
    */

    public CPolyhedron assign(CPolyhedron ph) {
        int result = ppl_assign_C_Polyhedron_from_C_Polyhedron(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public CPolyhedron(NNCPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_NNC_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron(NNCPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_NNC_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    /*
    public CPolyhedron(GeneratorSystem cs) {
        var pph = new PointerByReference();
        ppl_new_C_Polyhedron_from_Generator_System(pph, cs.obj);
        if (result < 0) throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron(GeneratorSystem cs, RecycleInput dummy) {
        var pph = new PointerByReference();
        ppl_new_C_Polyhedron_recycle_Generator_System(pph, cs.obj);
        if (result < 0) throw new PPLError(result);
        init(pph.getValue());
    }
    */

}
