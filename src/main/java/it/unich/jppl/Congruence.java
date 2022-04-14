package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;
import it.unich.jppl.LibPPL.SizeTByReference;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A linear congruence.
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
 * congruence \(\text{cg}\) is said to be a <em>proper congruence</em>.
 * </p>
 * <p>
 * If using only public methods, the Congruence class may be considered
 * immutable. Almost all methods throw {@link PPLRuntimeException} when the underlying PPL
 * library generates an error.
 * </p>
 */
public class Congruence extends AbstractPPLObject<Congruence> implements GeometricDescriptor<Congruence> {

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

    /**
     * Creates a congruence from a native object.
     *
     * @param registerCleaner if true, the native object is registered for deletion
     *                        when the congruence is garbage collected.
     */
    Congruence(Pointer p, boolean registerCleaner) {
        pplObj = p;
        if (registerCleaner)
            PPL.cleaner.register(this, new CongruenceCleaner(p));
    }

    /**
     * Creates a congruence from a native object. It is equivalent to
     * {@code Congruence(p, true)}.
     */
    private Congruence(Pointer p) {
        this(p, true);
    }

    /**
     * Creates and returns the congruence \(le \equiv 0 \pmod m\).
     */
    public static Congruence of(LinearExpression le, Coefficient m) {
        var pc = new PointerByReference();
        int result = ppl_new_Congruence(pc, le.pplObj, m.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new Congruence(pc.getValue());
    }

    /**
     * Creates the false zero-dimensional congruence \(0 \equiv 1 \pmod 0\).
     */
    public static Congruence zeroDimFalse() {
        var pc = new PointerByReference();
        int result = ppl_new_Congruence_zero_dim_false(pc);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new Congruence(pc.getValue());
    }

    /**
     * Create and returns the true zero-dimensional congruence \(0 \equiv 1 \pmod
     * 1\), also known as the <em>integrality congruence</em>.
     */
    public static Congruence zeroDimIntegrality() {
        var pc = new PointerByReference();
        int result = ppl_new_Congruence_zero_dim_integrality(pc);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new Congruence(pc.getValue());
    }

    @Override
    public Congruence clone() {
        var pc = new PointerByReference();
        int result = ppl_new_Congruence_from_Congruence(pc, pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new Congruence(pc.getValue());
    }

    @Override
    Congruence assign(Congruence c) {
        int result = ppl_assign_Congruence_from_Congruence(pplObj, c.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Congruence_space_dimension(pplObj, m);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return m.getValue().longValue();
    }

    @Override
    public Coefficient getCoefficient(long i) {
        var n = Coefficient.zero();
        int result = ppl_Congruence_coefficient(pplObj, new SizeT(i), n.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return n;
    }

    /**
     * Returns the inhomogeneous term of this congruence.
     */
    public Coefficient getInhomogeneousTerm() {
        var n = Coefficient.zero();
        int result = ppl_Congruence_inhomogeneous_term(pplObj, n.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return n;
    }

    /**
     * Returns the modulus of this congruence.
     */
    public Coefficient getModulus() {
        var n = Coefficient.zero();
        int result = ppl_Congruence_modulus(pplObj, n.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return n;
    }

    @Override
    boolean isOK() {
        int result = ppl_Congruence_OK(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Congruence(pstr, pplObj);
    }

    /**
     * Returns whether obj is the same as this congruence. Two congruences are the
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
