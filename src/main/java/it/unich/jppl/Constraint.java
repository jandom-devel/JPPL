package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.nativelib.LibPPL.Dimension;
import it.unich.jppl.nativelib.LibPPL.DimensionByReference;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class Constraint {
    Pointer pplObj;

    public static enum ConstraintType {
        LESS_THAN, LESS_OR_EQUAL, EQUAL, GREATER_OR_EQUAL, GREATER_THAN;

        static ConstraintType valueOf(int t) {
            switch (t) {
            case 0:
                return LESS_THAN;
            case 1:
                return LESS_OR_EQUAL;
            case 2:
                return EQUAL;
            case 3:
                return GREATER_OR_EQUAL;
            case 4:
                return GREATER_THAN;
            }
            throw new IllegalStateException("Unexpected constraint type " + t);
        }
    }

    public static enum ZeroDimConstraint {
        FALSITY, POSITIVITY
    }

    public static class RelationWithConstraint {
        public final static int IS_DISJOINT = 1;
        public final static int STRICTLY_INTERSECTS = 2;
        public final static int IS_INCLUDED = 4;
        public final static int SATURATES = 8;
    }

    private static class ConstraintCleaner implements Runnable {
        private Pointer pplObj;

        ConstraintCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Constraint(pplObj);
        }
    }

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new ConstraintCleaner(pplObj));
    }

    public Constraint(ZeroDimConstraint t) {
        var pc = new PointerByReference();
        int result = (t == ZeroDimConstraint.FALSITY) ? ppl_new_Constraint_zero_dim_false(pc)
                : ppl_new_Constraint_zero_dim_positivity(pc);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    public Constraint(LinearExpression le, ConstraintType rel) {
        var pc = new PointerByReference();
        int result = ppl_new_Constraint(pc, le.pplObj, rel.ordinal());
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    public Constraint(Constraint c) {
        this(c.pplObj);
    }

    Constraint(Pointer obj) {
        var pc = new PointerByReference();
        int result = ppl_new_Constraint_from_Constraint(pc, obj);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    public Constraint assign(Constraint c) {
        int result = ppl_assign_Constraint_from_Constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public long getSpaceDimension() {
        var m = new DimensionByReference();
        int result = ppl_Constraint_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    public ConstraintType getType() {
        int result = ppl_Constraint_type(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return ConstraintType.valueOf(result);
    }

    public Coefficient getCoefficient(long var) {
        var n = new Coefficient();
        int result = ppl_Constraint_coefficient(pplObj, new Dimension(var), n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    public Coefficient getCoefficient() {
        var n = new Coefficient();
        int result = ppl_Constraint_inhomogeneous_term(pplObj, n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    public boolean isOK() {
        int result = ppl_Constraint_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Constraint(pstr, pplObj);
        if (result < 0)
            throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Constraint) {
            var c = (Constraint) obj;
            if (c.getType() != getType())
                return false;
            // Since it is not possible to access dimensions greater than the getSpaceDimension() of a
            // constraint, we think it is safe to require two constraints to have the same space dimension
            // in order to be considered equals.
            if (c.getSpaceDimension() != getSpaceDimension())
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
