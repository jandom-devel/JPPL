package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.Domain.ComplexityClass;
import it.unich.jppl.Domain.DegenerateElement;
import it.unich.jppl.Domain.RecycleInput;
import it.unich.jppl.nativelib.LibPPL.Dimension;

import com.sun.jna.ptr.PointerByReference;

public class NNCPolyhedron extends Polyhedron<NNCPolyhedron> implements Property<NNCPolyhedron> {

    protected NNCPolyhedron self() {
        return this;
    }

    public NNCPolyhedron(long d, DegenerateElement kind) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_space_dimension(pph, new Dimension(d),
                kind == DegenerateElement.EMPTY ? 1 : 0);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public NNCPolyhedron(ConstraintSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public NNCPolyhedron(ConstraintSystem cs, RecycleInput dummy) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_recycle_Constraint_System(pph, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    /*
    public NNCPolyhedron(CongruenceSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Congruence_System(pph, cs.obj);
        if (result < 0) throw new PPLError(result);
        init(pph.getValue());
    }

    public NNCPolyhedron(CongruenceSystem cs, RecycleInput dummy) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_recycle_Congruence_System(pph, cs.obj);
        if (result < 0) throw new PPLError(result);
        init(pph.getValue());
    }
    */

    /*
    public NNCPolyhedron(GeneratorSystem cs) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Generator_System(pph, cs.obj);
        if (result < 0) throw new PPLError(result);
        init(pph.getValue());
    }

    public NNCPolyhedron(GeneratorSystem cs, RecycleInput dummy) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_recycle_Generator_System(pph, cs.obj);
        if (result < 0) throw new PPLError(result);
        init(pph.getValue());
    }
    */

    public NNCPolyhedron(NNCPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_NNC_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public NNCPolyhedron(NNCPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_NNC_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public NNCPolyhedron(CPolyhedron ph) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_C_Polyhedron(pph, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public NNCPolyhedron(CPolyhedron ph, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_C_Polyhedron_with_complexity(pph, ph.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public NNCPolyhedron(DoubleBox box) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Double_Box(pph, box.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public NNCPolyhedron(DoubleBox box, ComplexityClass complexity) {
        var pph = new PointerByReference();
        int result = ppl_new_NNC_Polyhedron_from_Double_Box_with_complexity(pph, box.pplObj, complexity.ordinal());
        if (result < 0)
            throw new PPLError(result);
        init(pph.getValue());
    }

    public NNCPolyhedron assign(NNCPolyhedron ph) {
        int result = ppl_assign_NNC_Polyhedron_from_NNC_Polyhedron(pplObj, ph.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

}
