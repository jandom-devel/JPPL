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
public class Coefficient extends Number {
    Pointer obj;

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

    private void init(Pointer p) {
        obj = p;
        PPL.cleaner.register(this, new CoefficientCleaner(obj));
    }

    public Coefficient() {
        PointerByReference pc = new PointerByReference();
        ppl_new_Coefficient(pc);
        init(pc.getValue());
    }

    public Coefficient(long l) {
        PointerByReference pc = new PointerByReference();
        Memory mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_si(mpz, l);
        ppl_new_Coefficient_from_mpz_t(pc, mpz);
        init(pc.getValue());
    }

    public Coefficient(String s) {
        PointerByReference pc = new PointerByReference();
        Memory mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_str(mpz, s, 10);
        ppl_new_Coefficient_from_mpz_t(pc, mpz);
        init(pc.getValue());
    }

    public Coefficient(BigInteger z) {
        this(z.toString());
    }

    public Coefficient(Coefficient c) {
        PointerByReference pc = new PointerByReference();
        ppl_new_Coefficient_from_Coefficient(pc, c.obj);
        init(pc.getValue());
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

    public long longValue() {
        Pointer mpz = new Memory(MPZ_SIZE);
        __gmpz_init(mpz);
        ppl_Coefficient_to_mpz_t(obj, mpz);
        return __gmpz_get_si(mpz);
    }

    public int intValue() {
        return (int) longValue();
    }

    public double doubleValue() {
        Pointer mpz = new Memory(MPZ_SIZE);
        __gmpz_init(mpz);
        ppl_Coefficient_to_mpz_t(obj, mpz);
        return __gmpz_get_d(mpz);
    }

    public float floatValue() {
        return (float) doubleValue();

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

    @Override
    public String toString() {
        PointerByReference strp = new PointerByReference();
        ppl_io_asprint_Coefficient(strp, obj);
        Pointer p = strp.getValue();
        String s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Coefficient){
            Coefficient c = (Coefficient) obj;
            var mpz1 = new Memory(MPZ_SIZE);
            var mpz2 = new Memory(MPZ_SIZE);
            __gmpz_init(mpz1);
            __gmpz_init(mpz2);
            ppl_Coefficient_to_mpz_t(this.obj, mpz1);
            ppl_Coefficient_to_mpz_t(c.obj, mpz2);
            return __gmpz_cmp(mpz1, mpz2) == 0;
        }
        return false;
    }

}
