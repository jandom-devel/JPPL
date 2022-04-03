package it.unich.jppl;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import static it.unich.jppl.nativelib.LibGMP.*;
import static it.unich.jppl.nativelib.LibPPL.*;

/**
 * Created by amato on 17/03/16.
 */
public class Coefficient {
    private Pointer obj;

    public Pointer getObj() {
        return obj;
    }

    public Coefficient() {
        PointerByReference pc = new PointerByReference();
        ppl_new_Coefficient(pc);
        obj = pc.getValue();
    }

    public Coefficient(long n) {
        PointerByReference pc = new PointerByReference();
        Memory mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_si(mpz, n);
        ppl_new_Coefficient_from_mpz_t(pc, mpz);
        obj = pc.getValue();
    }

    public String toString() {
        PointerByReference pstr = new PointerByReference();
        ppl_io_asprint_Coefficient(pstr,obj);
        return pstr.getValue().getString(0);
        // should free string
    }
}

