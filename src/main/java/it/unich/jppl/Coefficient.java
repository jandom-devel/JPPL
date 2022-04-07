package it.unich.jppl;

import static it.unich.jppl.nativelib.LibGMP.*;
import static it.unich.jppl.nativelib.LibPPL.*;

import java.math.BigInteger;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class Coefficient extends Number {
    Pointer pplObj;

    private static class CoefficientCleaner implements Runnable {
        private Pointer pplObj;

        CoefficientCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Coefficient(pplObj);
        }
    }

    static public boolean isBounded() {
        int result = ppl_Coefficient_is_bounded();
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new CoefficientCleaner(pplObj));
    }

    public Coefficient() {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient(pc);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    public Coefficient(long l) {
        var pc = new PointerByReference();
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_si(mpz, l);
        int result = ppl_new_Coefficient_from_mpz_t(pc, mpz);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    public Coefficient(String s) {
        var pc = new PointerByReference();
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_str(mpz, s, 10);
        int result = ppl_new_Coefficient_from_mpz_t(pc, mpz);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    public Coefficient(BigInteger z) {
        this(z.toString());
    }

    public Coefficient(Coefficient c) {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient_from_Coefficient(pc, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    public Coefficient assign(long n) {
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_si(mpz, n);
        int result = ppl_assign_Coefficient_from_mpz_t(pplObj, mpz);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public Coefficient assign(String s) {
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_str(mpz, s, 10);
        int result = ppl_assign_Coefficient_from_mpz_t(pplObj, mpz);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public Coefficient assign(BigInteger bi) {
        return assign(bi.toString());
    }

    public Coefficient assign(Coefficient c) {
        int result = ppl_assign_Coefficient_from_Coefficient(pplObj, this.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    private Pointer mpzValue() {
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init(mpz);
        int result = ppl_Coefficient_to_mpz_t(pplObj, mpz);
        if (result < 0)
            throw new PPLError(result);
        return mpz;
    }

    public BigInteger bigIntegerValue() {
        var mpz = mpzValue();
        long strsize = __gmpz_sizeinbase(mpz, 10) + 2;
        var str = new Memory(strsize);
        __gmpz_get_str(str, 10, mpz);
        return new BigInteger(str.getString(0));
    }

    public long longValue() {
        return __gmpz_get_si(mpzValue());
    }

    public int intValue() {
        return (int) longValue();
    }

    public double doubleValue() {
        return __gmpz_get_d(mpzValue());
    }

    public float floatValue() {
        return (float) doubleValue();
    }

    public boolean isOK() {
        int result = ppl_Coefficient_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public int minValue() {
        int result = ppl_Coefficient_min(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    public int maxValue() {
        int result = ppl_Coefficient_max(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    @Override
    public String toString() {
        var strp = new PointerByReference();
        int result = ppl_io_asprint_Coefficient(strp, pplObj);
        if (result < 0)
            throw new PPLError(result);
        var p = strp.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Coefficient) {
            Coefficient c = (Coefficient) obj;
            var mpz1 = new Memory(MPZ_SIZE);
            var mpz2 = new Memory(MPZ_SIZE);
            __gmpz_init(mpz1);
            __gmpz_init(mpz2);
            ppl_Coefficient_to_mpz_t(pplObj, mpz1);
            ppl_Coefficient_to_mpz_t(c.pplObj, mpz2);
            return __gmpz_cmp(mpz1, mpz2) == 0;
        }
        return false;
    }

}
