package it.unich.jppl;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import it.unich.jppl.nativeppl.LibPPL;

/**
 * Created by amato on 17/03/16.
 */
public class CPolyhedron {
    private Pointer obj;

    public CPolyhedron(long dimension, int empty) {
        PointerByReference objref = new PointerByReference();
        LibPPL.ppl_new_C_Polyhedron_from_space_dimension(objref, dimension, empty);
        obj = objref.getValue();
    }

    public CPolyhedron refineWithConstraint(Constraint c) {
        LibPPL.ppl_Polyhedron_refine_with_constraint(obj, c.getObj());
        return this;
    }

    public String toString() {
        PointerByReference pstr = new PointerByReference();
        LibPPL.ppl_io_asprint_Polyhedron(pstr,obj);
        return pstr.getValue().getString(0);
        // should free string
    }
}
