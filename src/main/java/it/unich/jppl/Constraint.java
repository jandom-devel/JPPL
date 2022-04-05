package it.unich.jppl;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import static it.unich.jppl.nativelib.LibPPL.*;

/**
 * Created by amato on 17/03/16.
 */
public class Constraint {
    Pointer obj;

    public static enum ConstraintType {
        LESS_THAN(0),
        LESS_OR_EQUAL(1),
        EQUAL(2),
        GREATER_OR_EQUAL(3),
        GREATER_THAN(4);

        int pplValue;

        ConstraintType (int pplValue) {
            this.pplValue = pplValue;
        }

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
            throw new IllegalStateException("Unexpected Constraint type " + t);
        }
    }

    public static enum ZeroDimConstraint {
        FALSITY,
        POSITIVITY
    }

    private static class ConstraintCleaner implements Runnable {
        private Pointer obj;

        ConstraintCleaner(Pointer obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Constraint(obj);
        }
    }

    private void init(Pointer p) {
        obj = p;
        PPL.cleaner.register(this, new ConstraintCleaner(obj));
    }

    Constraint(Pointer obj) {
        init(obj);
    }

    public Constraint(ZeroDimConstraint t) {
        PointerByReference pc = new PointerByReference();
        if (t == ZeroDimConstraint.FALSITY)
            ppl_new_Constraint_zero_dim_false(pc);
        else
            ppl_new_Constraint_zero_dim_positivity(pc);
        init(pc.getValue());
    }

    public Constraint(LinearExpression le, ConstraintType rel) {
        PointerByReference pc = new PointerByReference();
        ppl_new_Constraint(pc, le.obj, rel.pplValue);
        init(pc.getValue());
    }

    public Constraint(Constraint c) {
        PointerByReference pc = new PointerByReference();
        ppl_new_Constraint_from_Constraint(pc, c.obj);
        init(pc.getValue());
    }

    public Constraint assign(Constraint c) {
        ppl_assign_Constraint_from_Constraint(obj, c.obj);
        return this;
    }

    public long getSpaceDimension() {
        var dref = new DimensionByReference();
        ppl_Constraint_space_dimension(obj, dref);
        return dref.getValue().longValue();
    }

    public ConstraintType getType() {
        int t = ppl_Constraint_type(obj);
        return ConstraintType.valueOf(t);
    }

    public Coefficient getCoefficient(long var) {
        var c = new Coefficient();
        ppl_Constraint_coefficient(obj, new Dimension(var), c.obj);
        return c;
    }

    public Coefficient getCoefficient() {
        var c = new Coefficient();
        ppl_Constraint_inhomogeneous_term(obj, c.obj);
        return c;
    }

    public boolean isOK() {
        return ppl_Constraint_OK(obj) > 0;
    }

    @Override
    public String toString() {
        PointerByReference pstr = new PointerByReference();
        ppl_io_asprint_Constraint(pstr,obj);
        Pointer p = pstr.getValue();
        String s = p.getString(0);
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
            if (! c.getCoefficient().equals(getCoefficient()))
                return false;
            for (long d=0; d < Math.max(c.getSpaceDimension(), getSpaceDimension()); d++) {
                if (! c.getCoefficient(d).equals(getCoefficient(d)))
                    return false;
            }
            return true;
        }
        return false;
    }
}
