package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;
import it.unich.jppl.LibPPL.SizeTByReference;

import com.sun.jna.Native;
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
public class Constraint {
    Pointer pplObj;

    /**
     * Describes the relations represented by a constraint.
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
         * Returns the enum corrisponding to its ordinal value.
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

    /**
     * Enumeration for the two possible constraints of space dimension 0. It is used
     * by the constructor of Constraint.
     */
    public static enum ZeroDimConstraint {
        /** Referes to the unsatisfiable (zero-dimension space) constraint \(0 = 1\). */
        FALSITY,
        /**
         * Referes to the true (zero-dimension space) constraint \(0 \leq 1\), also
         * known as <em>positivity constraint</em>.
         */
        POSITIVITY
    }

    /**
     * Class of constants representing the relation between a contraint and a
     * geometric object.
     */
    public static class RelationWithConstraint {
        /**
         * The geometric oject and the set of points satisfying the constraint are disjoint.
         */
        public static final int IS_DISJOINT = 1;
        /**
         * The geometric object intersects the set of points satisfying the constraint, but it
         * is not included in it.
         */
        public static final int STRICTLY_INTERSECTS = 2;
        /**
         * The geometric object is included in the set of points satisfying the constraint.
         */
        public static final int IS_INCLUDED = 4;
        /**
         * The geometric object is included in the set of points saturating the constraint.
         */
        public static final int SATURATES = 8;
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

    /**
     * Creates the new Constraint "le rel 0". The space dimension of the new
     * constraint is equal to the space dimension of le.
     */
    public Constraint(LinearExpression le, ConstraintType rel) {
        var pc = new PointerByReference();
        int result = ppl_new_Constraint(pc, le.pplObj, rel.ordinal());
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Creates a new zero-dimensional Constraint according to t.
     */
    public Constraint(ZeroDimConstraint t) {
        var pc = new PointerByReference();
        int result = (t == ZeroDimConstraint.FALSITY) ? ppl_new_Constraint_zero_dim_false(pc)
                : ppl_new_Constraint_zero_dim_positivity(pc);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Creates a copy of the Constraint c.
     */
    public Constraint(Constraint c) {
        var pc = new PointerByReference();
        int result = ppl_new_Constraint_from_Constraint(pc, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pc.getValue());
    }

    /**
     * Creates a Constraint obtained from the specified native object.
     */
    Constraint(Pointer pplObj) {
        init(pplObj);
    }

    /**
     * Set the value of this Constraint to a copy of the Constraint c.
     */
    public Constraint assign(Constraint c) {
        int result = ppl_assign_Constraint_from_Constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Returns the space dimension of this Constraint.
     */
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Constraint_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
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
     * Returns the Coefficient for the variable \(x_i\).
     */
    public Coefficient getCoefficient(long var) {
        var n = new Coefficient();
        int result = ppl_Constraint_coefficient(pplObj, new SizeT(var), n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    /**
     * Returns the inhomogeneous term of this Constraint.
     */
    public Coefficient getInhomogeneousTerm() {
        var n = new Coefficient();
        int result = ppl_Constraint_inhomogeneous_term(pplObj, n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    /**
     * Returns true if this Constraint satisfies all its implementation invariants;
     * returns false and perhaps makes some noise if it is broken. Useful for
     * debugging purposes.
     */
    public boolean isOK() {
        int result = ppl_Constraint_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Returns the string representation of this Constraint.
     */
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

    /**
     * Returns whether obj is the same as this Congruence. Two congruences are the
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
}
