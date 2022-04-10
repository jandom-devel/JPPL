package it.unich.jppl;

import static it.unich.jppl.LibGMP.*;
import static it.unich.jppl.LibPPL.*;

import java.math.BigInteger;

import com.sun.jna.Native;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * <p>
 * Objects of type Coefficient are used to implement the integral valued
 * coefficients occurring in linear expressions, constraints, generators,
 * intervals, bounding boxes and so on. Depending on the way the PPL is
 * configured, a Coefficient may actually be:
 * </p>
 * <ul>
 * <li>an integer in the <a href="https://gmplib.org/">GNU MP library</a>;
 * <li>a native integral type with overflow detection.
 * </ul>
 * <p>
 * If using only public methods, the Coefficient class may be considered
 * immutable. Almost all methods throw {@link PPLError} when the underlying PPL
 * library generates an error.
 * </p>
 */
public class Coefficient extends Number {
    Pointer pplObj;

    /**
     * A coefficient which is equal to zero.
     */
    public static final Coefficient ZERO = new Coefficient();

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
     * Returns true if and only if the Coefficient class is implemented with
     * integral types and overflow detection.
     */
    public static boolean isBounded() {
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
     * Returns a Coefficient whose value is zero. This constructor has package level
     * visibility since end users are encouraged to use the Coefficient.ZERO
     * constant.
     */
    Coefficient() {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient(pc);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Returns a Coefficient whose value is l.
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
     * Returns a Coefficient whose value is given by the string representation s in
     * the specified radix.
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
     * Returns a Coefficient whose value is given by the decimal string
     * representation s. It is equivalent to {@code Coefficient(s, 10)}.
     */
    public Coefficient(String s) {
        this(s, 10);
    }

    /**
     * Returns a Coefficient whose value is equal to bi.
     */
    public Coefficient(BigInteger bi) {
        // We use radix since32  we suspect it is faster than radix 10.
        // Values bigger than 32 do not work.
        this(bi.toString(32), 32);
    }

    /**
     * Returns a copy of the Coefficient c.
     */
    Coefficient(Coefficient c) {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient_from_Coefficient(pc, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Set the value of this Coefficient to l.
     */
    Coefficient set(long l) {
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_si(mpz, l);
        int result = ppl_assign_Coefficient_from_mpz_t(pplObj, mpz);
        __gmpz_clear(mpz);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Set the value of this Coefficient to the value given by the string
     * representation s in the specified radix.
     */
    Coefficient set(String s, int radix) {
        var mpz = new Memory(MPZ_SIZE);
        __gmpz_init_set_str(mpz, s, radix);
        int result = ppl_assign_Coefficient_from_mpz_t(pplObj, mpz);
        __gmpz_clear(mpz);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Set the value of this Coefficientto the value given by the decimal string
     * representation s. It is equivalent to {@code set(s,10)}.
     */
    Coefficient set(String s) {
        return set(s, 10);
    }

    /**
     * Set the value of this Coefficient to bi.
     */
    Coefficient set(BigInteger bi) {
        return set(bi.toString(32), 32);
    }

    /**
     * Set the value of this Coefficient to a copy of the Coefficient c.
     */
    Coefficient set(Coefficient c) {
        int result = ppl_assign_Coefficient_from_Coefficient(pplObj, this.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * This is a private method which returns the value of a Coefficient as GNU MP
     * Integer. After it is used, the pointer should be cleared with __gmpz_clear.
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
     * Convert the Coefficient to its string representation in the specified radix.
     */
    public String stringValue(int radix) {
        var mpz = mpzValue();
        long strsize = __gmpz_sizeinbase(mpz, radix) + 2;
        var str = new Memory(strsize);
        __gmpz_get_str(str, radix, mpz);
        __gmpz_clear(mpz);
        return str.getString(0);
    }

    /**
     * Convert the Coefficient to its decimal string representation. It is
     * equivalent to {@code stringValue(10)}.
     */
    public String stringValue() {
        return stringValue(10);
    }

    /**
     * Convert the Coefficient to a BigInteger.
     */
    public BigInteger bigIntegerValue() {
        return new BigInteger(stringValue(32), 32);
    }

    /**
     * Convert the Coefficient to a long.
     */
    public long longValue() {
        var mpz = mpzValue();
        var l = __gmpz_get_si(mpz);
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
     * Returns true if this Coefficient satisfies all its implementation invariants;
     * returns false and perhaps makes some noise if it is broken. Useful for
     * debugging purposes.
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
    public int minAllowed() {
        int result = ppl_Coefficient_min(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    /**
     * If coeffients are native integral types, returns their maximum value.
     */
    public int maxAllowed() {
        int result = ppl_Coefficient_max(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    /**
     * Returns the string representation of the linear expression. It should be
     * equivalent to {@link stringValue() stringValue()}, but it uses a different
     * native method.
     */
    @Override
    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Coefficient(pstr, pplObj);
        if (result < 0)
            throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

    /**
     * Indicates whether some other object is "equal to" this Coefficient.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Coefficient) {
            Coefficient c = (Coefficient) obj;
            var mpz1 = mpzValue();
            var mpz2 = c.mpzValue();
            var result = __gmpz_cmp(mpz1, mpz2) == 0;
            __gmpz_clear(mpz1);
            __gmpz_clear(mpz2);
            return result;
        }
        return false;
    }

}
