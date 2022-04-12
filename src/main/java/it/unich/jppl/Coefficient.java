package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jgmp.*;

import java.math.BigInteger;

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
public class Coefficient extends PPLObject<Coefficient> {

    /**
     * A coefficient which is equal to zero.
     */
    public static final Coefficient ZERO = Coefficient.zero();

    /**
     * A coefficient which is equal to one.
     */
    public static final Coefficient ONE = Coefficient.valueOf(1);

    /**
     * A coefficient which is equal to minus one.
     */
    public static final Coefficient MINUS_ONE = Coefficient.valueOf(-1);

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
     * Creates a Coefficient.valueOf from the pointer p to a native object.
     */
    private Coefficient(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new CoefficientCleaner(pplObj));
    }

    /**
     * Creates and returns a coefficient whose value is zero.
     */
    public static Coefficient zero() {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient(pc);
        if (result < 0)
            throw new PPLError(result);
        return new Coefficient(pc.getValue());
    }

    /**
     * Creates and returns a coefficient whose value is z.
     */
    public static Coefficient valueOf(MPZ z) {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient_from_mpz_t(pc, z.getPointer());
        if (result < 0)
            throw new PPLError(result);
        return new Coefficient(pc.getValue());
    }

    /**
     * Creates and returns a coefficient whose value is l.
     */
    public static Coefficient valueOf(long l) {
        return valueOf(new MPZ(l));
    }

    /**
     * Creates and returns a coefficient whose value is given by the string
     * representation s in the specified radix.
     */
    public static Coefficient valueOf(String s, int radix) {
        return valueOf(new MPZ(s, radix));
    }

    /**
     * Cretes and returns a coefficient whose value is given by the decimal string
     * representation s. It is equivalent to {@code Coefficient(s, 10)}.
     */
    public static Coefficient valueOf(String s) {
        return valueOf(s, 10);
    }

    /**
     * Creates and returns a Coefficient whose value is equal to bi.
     */
    public static Coefficient valueOf(BigInteger bi) {
        // We use radix 32 since we suspect it to be faster than radix 10.
        // Values bigger than 32 do not work.
        return valueOf(bi.toString(32), 32);
    }

    @Override
    public Coefficient clone() {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient_from_Coefficient(pc, pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new Coefficient(pc.getValue());
    }

    /**
     * Set the value of this coefficient to z.
     */
    Coefficient assign(MPZ z) {
        int result = ppl_assign_Coefficient_from_mpz_t(pplObj, z.getPointer());
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    Coefficient assign(Coefficient c) {
        int result = ppl_assign_Coefficient_from_Coefficient(pplObj, this.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Converts the coefficient to a GNU MP integer.
     */
    public MPZ MPZValue() {
        var z = new MPZ();
        int result = ppl_Coefficient_to_mpz_t(pplObj, z.getPointer());
        if (result < 0)
            throw new PPLError(result);
        return z;
    }

    /**
     * Converts the coefficient to its string representation in the specified radix.
     */
    public String stringValue(int radix) {
        return MPZValue().toString(radix);
    }

    /**
     * Converts the coefficient to its decimal string representation. It is
     * equivalent to {@code stringValue(10)}.
     */
    public String stringValue() {
        return stringValue(10);
    }

    /**
     * Converts the coefficient to a BigInteger.
     */
    public BigInteger bigIntegerValue() {
        return new BigInteger(stringValue(32), 32);
    }

    /**
     * Converts the coefficient to a long.
     */
    public long longValue() {
        return MPZValue().longValue();
    }

    /**
     * Converts the coefficient to an int.
     */
    public int intValue() {
        return MPZValue().intValue();
    }

    /**
     * Converts the coefficient to a double.
     */
    public double doubleValue() {
        return MPZValue().doubleValue();
    }

    /**
     * Converts the coefficient to a float.
     */
    public float floatValue() {
        return MPZValue().floatValue();
    }

    @Override
    boolean isOK() {
        int result = ppl_Coefficient_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * If coefficients are native integral types, returns their minimum value.
     */
    public int minAllowed() {
        int result = ppl_Coefficient_min(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    /**
     * If coefficients are native integral types, returns their maximum value.
     */
    public int maxAllowed() {
        int result = ppl_Coefficient_max(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Coefficient(pstr, pplObj);
    }

    /**
     * Returns whether obj is the same as this coefficient.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Coefficient) {
            Coefficient c = (Coefficient) obj;
            return MPZValue().equals(c.MPZValue());
        }
        return false;
    }

    /**
     * Returns true if and only if the Coefficient class is implemented using native
     * integral types.
     */
    public static boolean isBounded() {
        int result = ppl_Coefficient_is_bounded();
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

}
