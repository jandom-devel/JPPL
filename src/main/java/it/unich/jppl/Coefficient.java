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
     * Returns true is and only if the Coefficient class is implemented using native
     * integral types.
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
     * Creates a coefficient whose value is zero.
     */
    public Coefficient() {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient(pc);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Creates a coefficient whose value is z.
     */
    public Coefficient(MPZ z) {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient_from_mpz_t(pc, z.getPointer());
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Creates a coefficient whose value is l.
     */
    public Coefficient(long l) {
        this(new MPZ(l));
    }

    /**
     * Creates a coefficient whose value is given by the string representation s in
     * the specified radix.
     */
    public Coefficient(String s, int radix) {
        this(new MPZ(s, radix));
    }

    /**
     * Cretes a coefficient whose value is given by the decimal string
     * representation s. It is equivalent to {@code Coefficient(s, 10)}.
     */
    public Coefficient(String s) {
        this(s, 10);
    }

    /**
     * Creates a Coefficient whose value is equal to bi.
     */
    public Coefficient(BigInteger bi) {
        // We use radix 32 since we suspect it to be faster than radix 10.
        // Values bigger than 32 do not work.
        this(bi.toString(32), 32);
    }

    /**
     * Creates a copy of the Coefficient c.
     */
    public Coefficient(Coefficient c) {
        var pc = new PointerByReference();
        int result = ppl_new_Coefficient_from_Coefficient(pc, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
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
     * Convert the coefficient to a GNU MP integer.
     */
    public MPZ MPZValue() {
        var z = new MPZ();
        int result = ppl_Coefficient_to_mpz_t(pplObj, z.getPointer());
        if (result < 0)
            throw new PPLError(result);
        return z;
    }

    /**
     * Convert the coefficient to its string representation in the specified radix.
     */
    public String stringValue(int radix) {
        return MPZValue().toString(radix);
    }

    /**
     * Convert the coefficient to its decimal string representation. It is
     * equivalent to {@code stringValue(10)}.
     */
    public String stringValue() {
        return stringValue(10);
    }

    /**
     * Convert the coefficient to a BigInteger.
     */
    public BigInteger bigIntegerValue() {
        return new BigInteger(stringValue(32), 32);
    }

    /**
     * Convert the coefficient to a long.
     */
    public long longValue() {
        return MPZValue().longValue();
    }

    /**
     * Convert the coefficient to an int.
     */
    public int intValue() {
        return MPZValue().intValue();
    }

    /**
     * Convert the coefficient to a double.
     */
    public double doubleValue() {
        return MPZValue().doubleValue();
    }

    /**
     * Convert the coefficient to a float.
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
     * Returns whether obj is the same as this Coefficient.
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

}
