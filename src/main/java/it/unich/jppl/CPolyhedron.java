package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.Domain.ComplexityClass;
import it.unich.jppl.Domain.DegenerateElement;
import it.unich.jppl.Domain.RecycleInput;
import it.unich.jppl.LibPPL.SizeT;

import com.sun.jna.ptr.PointerByReference;

public class CPolyhedron extends Polyhedron<CPolyhedron> implements Property<CPolyhedron> {

    protected CPolyhedron self() {
        return this;
    }

    public CPolyhedron(long d, DegenerateElement kind) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_space_dimension(pph, new SizeT(d),
                kind == DegenerateElement.EMPTY ? 1 : 0);
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

    public CPolyhedron(CongruenceSystem cs) {
        PointerByReference pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Congruence_System(pph, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron(CongruenceSystem cs, RecycleInput dummy) {
        PointerByReference pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_recycle_Congruence_System(pph, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron(GeneratorSystem gs) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Generator_System(pph, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron(GeneratorSystem gs, RecycleInput dummy) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_recycle_Generator_System(pph, gs.pplObj);
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

    public CPolyhedron(DoubleBox box) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Double_Box(pph, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron(DoubleBox box, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_C_Polyhedron_from_Double_Box_with_complexity(pph, box.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public CPolyhedron assign(CPolyhedron ph) {
        int result = ppl_assign_C_Polyhedron_from_C_Polyhedron(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

}
