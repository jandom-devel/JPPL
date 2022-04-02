package it.unich.jppl;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import it.unich.jppl.nativeppl.LibPPL;

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
        LibPPL.ppl_new_Coefficient(pc);
        obj = pc.getValue();
    }

    public Coefficient(long n) {
        PointerByReference pc = new PointerByReference();
        Memory mpz = new Memory(LibGMP.MPZ_SIZE);
        LibGMP.__gmpz_init_set_si(mpz, n);
        LibPPL.ppl_new_Coefficient_from_mpz_t(pc, mpz);
        obj = pc.getValue();
    }

    public String toString() {
        PointerByReference pstr = new PointerByReference();
        LibPPL.ppl_io_asprint_Coefficient(pstr,obj);
        return pstr.getValue().getString(0);
        // should free string
    }
}

