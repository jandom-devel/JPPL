package it.unich.jppl;

import com.sun.jna.ptr.PointerByReference;

import it.unich.jppl.Domain.ComplexityClass;
import it.unich.jppl.Domain.DegenerateElement;
import it.unich.jppl.Domain.RecycleInput;

import static it.unich.jppl.nativelib.LibPPL.*;

/**
 * Created by amato on 17/03/16.
 */
public class NNCPolyhedron extends Polyhedron<NNCPolyhedron> implements Property<NNCPolyhedron>  {

    protected NNCPolyhedron self() {
        return this;
    }

    public NNCPolyhedron(long d, DegenerateElement kind) {
        PointerByReference pph = new PointerByReference();
        ppl_new_NNC_Polyhedron_from_space_dimension(pph, new Dimension(d), kind == DegenerateElement.EMPTY ? 1 : 0);
        init(pph.getValue());
    }

    public NNCPolyhedron(NNCPolyhedron ph) {
        PointerByReference pph = new PointerByReference();
        ppl_new_NNC_Polyhedron_from_NNC_Polyhedron(pph, ph.obj);
        init(pph.getValue());
    }

    public NNCPolyhedron(NNCPolyhedron ph, ComplexityClass complexity) {
        PointerByReference pph = new PointerByReference();
        ppl_new_NNC_Polyhedron_from_NNC_Polyhedron_with_complexity(pph, ph.obj, complexity.ordinal());
        init(pph.getValue());
    }

    public NNCPolyhedron(ConstraintSystem cs) {
        PointerByReference pph = new PointerByReference();
        ppl_new_NNC_Polyhedron_from_Constraint_System(pph, cs.obj);
        init(pph.getValue());
    }

    public NNCPolyhedron(ConstraintSystem cs, RecycleInput dummy) {
        PointerByReference pph = new PointerByReference();
        ppl_new_NNC_Polyhedron_recycle_Constraint_System(pph, cs.obj);
        init(pph.getValue());
    }
/*
    public NNCPolyhedron(CongruenceSystem cs) {
        PointerByReference pph = new PointerByReference();
        ppl_new_NNC_Polyhedron_from_Congruence_System(pph, cs.obj);
        init(pph.getValue());
    }

    public NNCPolyhedron(CongruenceSystem cs, RecycleInput dummy) {
        PointerByReference pph = new PointerByReference();
        ppl_new_NNC_Polyhedron_recycle_Congruence_System(pph, cs.obj);
        init(pph.getValue());
    }
*/

    public NNCPolyhedron assign(NNCPolyhedron ph) {
        ppl_assign_NNC_Polyhedron_from_NNC_Polyhedron(obj, ph.obj);
        return this;
    }

    public NNCPolyhedron(CPolyhedron ph) {
        PointerByReference pph = new PointerByReference();
        ppl_new_NNC_Polyhedron_from_C_Polyhedron(pph, ph.obj);
        init(pph.getValue());
    }

    public NNCPolyhedron(CPolyhedron ph, ComplexityClass complexity) {
        PointerByReference pph = new PointerByReference();
        ppl_new_NNC_Polyhedron_from_C_Polyhedron_with_complexity(pph, ph.obj, complexity.ordinal());
        init(pph.getValue());
    }
}
