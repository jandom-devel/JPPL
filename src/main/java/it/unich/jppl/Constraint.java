package it.unich.jppl;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import it.unich.jppl.nativeppl.LibPPL;

/**
 * Created by amato on 17/03/16.
 */
public class Constraint {
    public Pointer obj;

    public Pointer getObj() {
        return obj;
    }

    public Constraint(LinearExpression le, int rel) {
        PointerByReference pc = new PointerByReference();
        LibPPL.ppl_new_Constraint(pc, le.getObj(), rel);
        obj = pc.getValue();
    }

    public String toString() {
        PointerByReference pstr = new PointerByReference();
        LibPPL.ppl_io_asprint_Constraint(pstr,obj);
        return pstr.getValue().getString(0);
        // should free string
    }
}
