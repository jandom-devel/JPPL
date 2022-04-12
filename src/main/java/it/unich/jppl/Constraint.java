package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;
import it.unich.jppl.LibPPL.SizeTByReference;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A linear equality or inequality.
 *
 * <p>
 * An object of the class Constraint is either:
 * </p>
 * <ul>
 * <li>an equality: \(\sum_{i=0}^{n-1} a_i x_i + b = 0$\);
 * <li>a non-strict inequality: \(\sum_{i=0}^{n-1} a_i x_i + b \geq 0$\);
 * <li>or a strict inequality: \(\sum_{i=0}^{n-1} a_i x_i + b &gt; 0\);
 * </ul>
 * <p>
 * where \(n\) is the dimension of the space, \(a_i\) is the integer coefficient
 * of variable \(x_i\) and \(b\) is the integer inhomogeneous term.
 * </p>
 * <p>
 * If using only public methods, the Constraint class may be considered
 * immutable. Almost all methods throw {@link PPLError} when the underlying PPL
 * library generates an error.
 * </p>
 */
public class Constraint extends GeometricDescriptor<Constraint> {

    /**
     * Enumerates the possible types of a constraint.
     */
    public static enum ConstraintType {
        /** The constraint is of the form \(e &lt; 0\). */
        LESS_THAN,
        /** The constraint is of the form \(e \leq 0\). */
        LESS_OR_EQUAL,
        /** The constraint is of the form \(e = 0\). */
        EQUAL,
        /** The constraint is of the form \(e \geq 0\). */
        GREATER_OR_EQUAL,
        /** The constraint is of the form \(e &gt; 0\). */
        GREATER_THAN;

        /**
         * Returns the ConstraintType corrisponding to its ordinal value.
         *
         * @throws IllegalArgumentException if t is not a valid ordinal value.
         */
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
            throw new IllegalArgumentException("Unexpected constraint type " + t);
        }
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

    /**
     * Creates a constraint from a native object.
     *
     * @param registerCleaner if true, the native object is registered for deletion
     *                        when the constraint is garbage collected.
     */
    Constraint(Pointer p, boolean registerCleaner) {
        pplObj = p;
        if (registerCleaner)
            PPL.cleaner.register(this, new ConstraintCleaner(p));
    }

    /**
     * Creates a constraint from a native object. It is equivalent to
     * {@code Constraint(p, true)}.
     */
    private Constraint(Pointer p) {
        this(p, false);
    }

    /**
     * Creates and returns the constraint \(le \textrm{rel} 0\).
     */
    public static Constraint of(LinearExpression le, ConstraintType rel) {
        var pc = new PointerByReference();
        int result = ppl_new_Constraint(pc, le.pplObj, rel.ordinal());
        if (result < 0)
            throw new PPLError(result);
        return new Constraint(pc.getValue());
    }

    /**
     * Creates and returns the unsatisfiable zero-dimensional constraint \(0 = 1\).
     */
    public static Constraint zeroDimFalse() {
        var pc = new PointerByReference();
        int result = ppl_new_Constraint_zero_dim_false(pc);
        if (result < 0)
            throw new PPLError(result);
        return new Constraint(pc.getValue());
    }

    /**
     * Creates and returns the zero-dimensional constraint \(0 \leq 1\), also known
     * as <em>positivity constraint</em>.
     */
    public static Constraint zeroDimPositivity() {
        var pc = new PointerByReference();
        int result = ppl_new_Constraint_zero_dim_positivity(pc);
        if (result < 0)
            throw new PPLError(result);
        return new Constraint(pc.getValue());
    }

    @Override
    public Constraint clone() {
        var pc = new PointerByReference();
        int result = ppl_new_Constraint_from_Constraint(pc, pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new Constraint(pc.getValue());
    }

    @Override
    Constraint assign(Constraint c) {
        int result = ppl_assign_Constraint_from_Constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Constraint_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    @Override
    public Coefficient getCoefficient(long var) {
        var n = Coefficient.zero();
        int result = ppl_Constraint_coefficient(pplObj, new SizeT(var), n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    @Override
    boolean isOK() {
        int result = ppl_Constraint_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Constraint(pstr, pplObj);
    }

    /**
     * Returns whether obj is the same as this constraint. Two constraints are the
     * same if they have the same space dimension, coefficients, inhomogeneous term
     * and type.
     */
    @Override
    public boolean equals(Object obj) {
        // The equals method is probably slow since it calls native methods several times
        // and allocates many Java objects. However, this is used mainly for debugging
        // purpose, hence this should not be a problem.
        if (this == obj)
            return true;
        if (obj instanceof Constraint) {
            var c = (Constraint) obj;
            if (c.getType() != getType())
                return false;
            if (c.getSpaceDimension() != getSpaceDimension())
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

    /**
     * Returns the type of this Constraint.
     */
    public ConstraintType getType() {
        int result = ppl_Constraint_type(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return ConstraintType.valueOf(result);
    }

    /**
     * Returns the inhomogeneous term of this constraint.
     */
    public Coefficient getInhomogeneousTerm() {
        var n = Coefficient.zero();
        int result = ppl_Constraint_inhomogeneous_term(pplObj, n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

}
