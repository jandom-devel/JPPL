package it.unich.jppl;

import static it.unich.jppl.nativelib.LibGMP.*;
import static it.unich.jppl.nativelib.LibPPL.*;

import java.math.BigInteger;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * Objects of type Coefficient are used to implement the integral valued coefficients occurring in
 * linear expressions, constraints, generators, intervals, bounding boxes and so on. Depending on the
 * way the PPL is configured, a Coefficient may actually be:
 * <p>
 * <ul>
 * <li>an integer in the <a href="https://gmplib.org/">GNU MP library</a>;
 * <li>a native integral type with overflow detection.
 * </ul>
 * If using only public methods, the Coefficient class is immutable.
 */
public class Coefficient extends Number {
    Pointer pplObj;

    /**
     * A coefficient which is equal to zero.
     */
    public static final Coefficient ZERO = new Coefficient(0);

    /**
     * A coefficient which is equal to one.
     */
    public static final Coefficient ONE = new Coefficient(1);

    /**
     * A coefficient which is equal to minus one.
     */
    public static final Coefficient MINUS_ONE = new Coefficient(-1);

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

    /**
     * Returns true whether the Coefficient class is implemented with integral types and
     * overflow detection.
     */
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

    /**
     * Returns a Coefficient whose value is zero. This has package level visibility since
     * end users should use the Coefficient.ZERO constant.
     */
    Coefficient() {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient(pc);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Returns a Coefficient whose value is equal to that of the specified long.
     */
    public Coefficient(long l) {
        var pc = new PointerByReference();
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_si(mpz, l);
        int result = ppl_new_Coefficient_from_mpz_t(pc, mpz);
        __gmpz_clear(mpz);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Translates the String representation in the specified radix into a Coefficient.
     */
    public Coefficient(String s, int radix) {
        var pc = new PointerByReference();
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_str(mpz, s, radix);
        int result = ppl_new_Coefficient_from_mpz_t(pc, mpz);
        __gmpz_clear(mpz);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Translates the decimal String representation into a Coefficient.
     */
    public Coefficient(String s) {
        this(s, 10);
    }

    /**
     * Returns a Coefficient whose value is equal to that of the specified BigInteger.
     */
    public Coefficient(BigInteger z) {
        // we use radix 32 since I suspect it is faster than radix 10
        // values bigger than 32 do not work
        this(z.toString(32),32);
    }

    /**
     * Returns a new Coefficient whose value is the same of the specified input Coefficient.
     */
    Coefficient(Coefficient c) {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient_from_Coefficient(pc, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Set the value of this Coefficient with the specified long.
     */
    Coefficient set(long n) {
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_si(mpz, n);
        int result = ppl_assign_Coefficient_from_mpz_t(pplObj, mpz);
        __gmpz_clear(mpz);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Set the value of this Coefficient using the provided String representation in the specified radix.
     */
    Coefficient set(String s, int base) {
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_str(mpz, s, base);
        int result = ppl_assign_Coefficient_from_mpz_t(pplObj, mpz);
        __gmpz_clear(mpz);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Set the value of this Coefficient using the provided decimal String representation.
     */
    Coefficient set(String s) {
        return set(s, 10);
    }

    /**
     * Set the value of this Coefficient with the BigInteger.
     */
    Coefficient set(BigInteger bi) {
        return set(bi.toString(32),32);
    }

    /**
     * Set the value of this Coefficient with the value of the Coefficient c.
     */
    Coefficient set(Coefficient c) {
        int result = ppl_assign_Coefficient_from_Coefficient(pplObj, this.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * This is a private method which returns the value of a Coefficient as GNU MP integer.
     * After it is used, the pointer should be cleared with __gmpz_clear.
     */
    private Pointer mpzValue() {
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init(mpz);
        int result = ppl_Coefficient_to_mpz_t(pplObj, mpz);
        if (result < 0)
            throw new PPLError(result);
        return mpz;
    }

    /**
     * Convert the Coefficient to a BigInteger.
     */
    public BigInteger bigIntegerValue() {
        var mpz = mpzValue();
        long strsize = __gmpz_sizeinbase(mpz, 32) + 2;
        var str = new Memory(strsize);
        __gmpz_get_str(str, 32, mpz);
        __gmpz_clear(mpz);
        return new BigInteger(str.getString(0), 32);
    }

    /**
     * Convert the Coefficient to a long.
     */
    public long longValue() {
        var mpz = mpzValue();
        var l =__gmpz_get_si(mpz);
        __gmpz_clear(mpz);
        return l;
    }

    /**
     * Convert the Coefficient to an int.
     */
    public int intValue() {
        return (int) longValue();
    }

    /**
     * Convert the Coefficient to a double.
     */
    public double doubleValue() {
        var mpz = mpzValue();
        var d = __gmpz_get_d(mpz);
        __gmpz_clear(mpz);
        return d;
    }

    /**
     * Convert the Coefficient to a float.
     */
    public float floatValue() {
        return (float) doubleValue();
    }

    /**
     * Returns true if this Coefficient satisfies all its implementation invariants; returns false and perhaps makes
     * some noise if it is broken. Useful for debugging purposes.
     */
    boolean isOK() {
        int result = ppl_Coefficient_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * If coeffients are native integral types, returns their minimum value.
     */
    public int minValue() {
        int result = ppl_Coefficient_min(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    /**
     * If coeffients are native integral types, returns their maximum value.
     */
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
            var result = __gmpz_cmp(mpz1, mpz2) == 0;
            __gmpz_clear(mpz1);
            __gmpz_clear(mpz2);
            return result;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return bigIntegerValue().hashCode();
    }

}
