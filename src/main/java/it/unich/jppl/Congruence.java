package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.nativelib.LibPPL.Dimension;
import it.unich.jppl.nativelib.LibPPL.DimensionByReference;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class Congruence {
    Pointer pplObj;

    public static enum ZeroDimCongruence {
        FALSITY, INTEGRALITY
    }

    public static class RelationWithCongruence {
        public final static int IS_DISJOINT = 1;
        public final static int STRICTLY_INTERSECTS = 2;
        public final static int IS_INCLUDED = 4;
        public final static int SATURATES = 8;
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

    public Congruence(LinearExpression le, Coefficient m) {
        var pc = new PointerByReference();
        int result = ppl_new_Congruence(pc, le.pplObj, m.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    public Congruence(ZeroDimCongruence t) {
        var pc = new PointerByReference();
        int result = (t == ZeroDimCongruence.FALSITY) ? ppl_new_Congruence_zero_dim_false(pc)
                : ppl_new_Congruence_zero_dim_integrality(pc);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    public Congruence(Congruence c) {
        var pc = new PointerByReference();
        int result = ppl_new_Congruence_from_Congruence(pc, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    Congruence() {
        this(ZeroDimCongruence.INTEGRALITY);
    }

    Congruence(Pointer pplObj) {
        init(pplObj);
    }

    public Congruence assign(Congruence c) {
        int result = ppl_assign_Congruence_from_Congruence(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public long getSpaceDimension() {
        var m = new DimensionByReference();
        int result = ppl_Congruence_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    public Coefficient getCoefficient(long var) {
        var n = new Coefficient();
        int result = ppl_Congruence_coefficient(pplObj, new Dimension(var), n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    public Coefficient getCoefficient() {
        var n = new Coefficient();
        int result = ppl_Congruence_inhomogeneous_term(pplObj, n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    public Coefficient getModulus() {
        var n = new Coefficient();
        int result = ppl_Congruence_modulus(pplObj, n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    public boolean isOK() {
        int result = ppl_Congruence_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

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

    // The equals method is probably slow since it calls native methods several times
    // and allocates many Java objects. However, this is used mainly for debugging
    // purpose, hence this should not be a problem.
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Congruence) {
            var c = (Congruence) obj;
            if (c.getSpaceDimension() != getSpaceDimension())
                return false;
            if (c.getModulus() != getModulus())
                return false;
            if (!c.getCoefficient().equals(getCoefficient()))
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
