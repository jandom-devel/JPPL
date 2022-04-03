package it.unich.jppl;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import static it.unich.jppl.nativelib.LibPPL.*;

/**
 * Created by amato on 17/03/16.
 */
public class LinearExpression {
    public Pointer obj;

    public Pointer getObj() {
        return obj;
    }

    public LinearExpression() {
        PointerByReference ple = new PointerByReference();
        ppl_new_Linear_Expression(ple);
        obj = ple.getValue();
    }

    public LinearExpression addToCoefficient(long var, Coefficient c) {
        ppl_Linear_Expression_add_to_coefficient(obj, var, c.getObj());
        return this;
    }

    public LinearExpression addToInhomogeneous(Coefficient c) {
        ppl_Linear_Expression_add_to_inhomogeneous(obj, c.getObj());
        return this;
    }

    public String toString() {
        PointerByReference pstr = new PointerByReference();
        ppl_io_asprint_Linear_Expression(pstr,obj);
        return pstr.getValue().getString(0);
        // should free string
    }
}
