package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;
import it.unich.jppl.LibPPL.SizeTByReference;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A linear conguence
 *
 * <p>
 * An object of the class Congruence is a congruence:
 *
 * \[\text{cg} = \sum_{i=0}^{n-1} a_i x_i + b \equiv 0 \pmod m\]
 *
 * where \(n\) is the dimension of the space, \(a_i\) is the integer coefficient
 * of variable \(x_i\), \(b\) is the integer inhomogeneous term and \(m\) is the
 * integer modulus; if \(m = 0\), then $\cg$ represents the equality congruence
 * \(\sum_{i=0}^{n-1} a_i x_i + b = 0\) and, if \(m \neq 0\), then the
 * congruence \(\text{cg}\) is said to be a <em>proper</em> congruence.
 * </p>
 * <p>
 * If using only public methods, the Congruence class may be considered
 * immutable. Almost all methods throw {@link PPLError} when the underlying PPL
 * library generates an error.
 * </p>
 */
public class Congruence {
    Pointer pplObj;

    /**
     * Enumeration for the two possible congruences of space dimension 0. It is used
     * by the constructor of Congruence.
     */
    public static enum ZeroDimCongruence {
        /**
         * Refers to the false (zero-dimensional) congruence \(0 \equiv 1 \pmod 0\).
         */
        FALSITY,
        /**
         * Refers to the true (zero-dimensional) congruence \(0 \equiv 1 \pmod 1\), also
         * known as the <em>integrality congruence</em>.
         */
        INTEGRALITY
    }

    private static class CongruenceCleaner implements Runnable {
        private Pointer pplObj;

        CongruenceCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Congruence(pplObj);
        }
    }

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new CongruenceCleaner(pplObj));
    }

    /**
     * Returns the new Congruence \(le \equiv 0 \pmod m\).
     */
    public Congruence(LinearExpression le, Coefficient m) {
        var pc = new PointerByReference();
        int result = ppl_new_Congruence(pc, le.pplObj, m.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Returns the zero-dimensional congruence specified by type
     */
    public Congruence(ZeroDimCongruence type) {
        var pc = new PointerByReference();
        int result = (type == ZeroDimCongruence.FALSITY) ? ppl_new_Congruence_zero_dim_false(pc)
                : ppl_new_Congruence_zero_dim_integrality(pc);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Returns a copy of the Congruence c.
     */
    public Congruence(Congruence c) {
        var pc = new PointerByReference();
        int result = ppl_new_Congruence_from_Congruence(pc, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Returns a Congruence obtained from the specified native object.
     */
    Congruence(Pointer pplObj) {
        init(pplObj);
    }

    /**
     * Set the value of this Congruence to a copy of the Congruence c.
     */
    Congruence assign(Congruence c) {
        int result = ppl_assign_Congruence_from_Congruence(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Returns the space dimension of this Congruence.
     */
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Congruence_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    /**
     * Returns the Coefficient for the variable \(x_i\).
     */
    public Coefficient getCoefficient(long var) {
        var n = new Coefficient();
        int result = ppl_Congruence_coefficient(pplObj, new SizeT(var), n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    /**
     * Returns the inhomogeneous term of this Congruence.
     */
    public Coefficient getInhomogeneousTerm() {
        var n = new Coefficient();
        int result = ppl_Congruence_inhomogeneous_term(pplObj, n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    /**
     * Returns the modulus of this Congruence.
     */
    public Coefficient getModulus() {
        var n = new Coefficient();
        int result = ppl_Congruence_modulus(pplObj, n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    /**
     * Returns true if this Congruence satisfies all its implementation invariants;
     * returns false and perhaps makes some noise if it is broken. Useful for
     * debugging purposes.
     */
    public boolean isOK() {
        int result = ppl_Congruence_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Returns the string representation of this Congruence.
     */
    @Override
    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Congruence(pstr, pplObj);
        if (result < 0)
            throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

    /**
     * Returns whether obj is the same as this Congruence. Two congruences are the
     * same if they have the same space dimension, coefficients, inhomogeneous term
     * and modulus.
     */
    @Override
    public boolean equals(Object obj) {
        // The equals method is probably slow since it calls native methods several times
        // and allocates many Java objects. However, this is used mainly for debugging
        // purpose, hence this should not be a problem.

        if (this == obj)
            return true;
        if (obj instanceof Congruence) {
            var c = (Congruence) obj;
            if (c.getSpaceDimension() != getSpaceDimension())
                return false;
            if (c.getModulus() != getModulus())
                return false;
            if (!c.getInhomogeneousTerm().equals(getInhomogeneousTerm()))
                return false;
            for (long d = 0; d < getSpaceDimension(); d++) {
                if (!c.getCoefficient(d).equals(getCoefficient(d)))
                    return false;
            }
            return true;
        }
        return false;
    }
}
