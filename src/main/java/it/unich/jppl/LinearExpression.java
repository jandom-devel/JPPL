package it.unich.jppl;

import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;

import static it.unich.jppl.nativelib.LibPPL.*;
import static it.unich.jppl.nativelib.LibGMP.*;


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
        ppl_Linear_Expression_add_to_coefficient(obj, var, c.getNative());
        return this;
    }

    public LinearExpression addToInhomogeneous(Coefficient c) {
        ppl_Linear_Expression_add_to_inhomogeneous(obj, c.getNative());
        return this;
    }

    public LinearExpression addToInhomogeneous(long l) {
        Memory mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_si(mpz, l);
        ppl_Linear_Expression_add_to_inhomogeneous(obj, mpz);
        return this;
    }
    
    public String toString() {
        PointerByReference pstr = new PointerByReference();
        ppl_io_asprint_Linear_Expression(pstr,obj);
        Pointer p = pstr.getValue();
        String s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
