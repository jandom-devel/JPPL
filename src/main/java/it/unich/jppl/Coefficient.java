package it.unich.jppl;

import com.sun.jna.Native;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import static it.unich.jppl.nativelib.LibGMP.*;
import static it.unich.jppl.nativelib.LibPPL.*;

import java.math.BigInteger;

/**
 * Created by amato on 17/03/16.
 */
public class Coefficient {
    private Pointer obj;

    private static class CoefficientCleaner implements Runnable {
        private Pointer obj;

        CoefficientCleaner(Pointer obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Coefficient(obj);
        }
    }

    static public boolean isBounded() {
        return ppl_Coefficient_is_bounded() > 0;
    }

    public Pointer getNative() {
        return obj;
    }

    public Coefficient() {
        PointerByReference pc = new PointerByReference();
        ppl_new_Coefficient(pc);
        obj = pc.getValue();
        PPL.cleaner.register(this, new CoefficientCleaner(obj));
    }

    public Coefficient(long l) {
        PointerByReference pc = new PointerByReference();
        Memory mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_si(mpz, l);
        ppl_new_Coefficient_from_mpz_t(pc, mpz);
        obj = pc.getValue();
        PPL.cleaner.register(this, new CoefficientCleaner(obj));
    }

    public Coefficient(String s) {
        PointerByReference pc = new PointerByReference();
        Memory mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_str(mpz, s, 10);
        ppl_new_Coefficient_from_mpz_t(pc, mpz);
        obj = pc.getValue();
        PPL.cleaner.register(this, new CoefficientCleaner(obj));
    }

    public Coefficient(BigInteger z) {
        this(z.toString());
    }

    public Coefficient(Coefficient c) {
        PointerByReference pc = new PointerByReference();
        ppl_new_Coefficient_from_Coefficient(pc, c.obj);
        obj = pc.getValue();
        PPL.cleaner.register(this, new CoefficientCleaner(obj));
    }

    public Coefficient assign(long n) {
        Memory mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_si(mpz, n);
        ppl_assign_Coefficient_from_mpz_t(obj, mpz);
        return this;
    }

    public Coefficient assign(String s) {
        Memory mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_str(mpz, s, 10);
        ppl_assign_Coefficient_from_mpz_t(obj, mpz);
        return this;
    }

    public Coefficient assign(BigInteger bi) {
        return assign(bi.toString());
    }

    public Coefficient assign(Coefficient c) {
        ppl_assign_Coefficient_from_Coefficient(obj, this.obj);
        return this;
    }

    public BigInteger bigIntegerValue() {
        Pointer mpz = new Memory(MPZ_SIZE);
        __gmpz_init(mpz);
        ppl_Coefficient_to_mpz_t(obj, mpz);
        long strsize = __gmpz_sizeinbase (mpz, 10) + 2;
        Pointer str = new Memory(strsize);
        __gmpz_get_str(str, 10, mpz);
        String s = str.getString(0);
        return new BigInteger(s);
    }

    public boolean isOK() {
        return ppl_Coefficient_OK(obj) > 0;
    }

    public int minValue() {
        return ppl_Coefficient_min(obj);
    }

    public int maxValue() {
        return ppl_Coefficient_max(obj);
    }

    public String toString() {
        PointerByReference strp = new PointerByReference();
        ppl_io_asprint_Coefficient(strp, obj);
        Pointer p = strp.getValue();
        String s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
