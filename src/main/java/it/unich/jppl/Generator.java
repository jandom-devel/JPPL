package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;
import it.unich.jppl.LibPPL.SizeTByReference;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A line, ray, point or closure point.
 * 
 * <p>
 * An object of the class Generator is one of the following:
 * </p>
 * <ul>
 * <li>a line \(\vec{l} = (a_0, \ldots, a_{n-1})^T\);
 * <li>a ray \(\vec{r} = (a_0, \ldots, a_{n-1})^T\);
 * <li>a point \(\vec{p} = (\frac{a_0}{d}, \ldots, \frac{a_{n-1}}{d})^T\);
 * <li>a closure point \(\vec{c} = (\frac{a_0}{d}, \ldots, \frac{a_{n-1}}{d})^T
 * \);
 * </ul>
 * <p>
 * where \(n\) is the dimension of the space and, for points and closure points,
 * \(d &gt; 0\) is the divisor.
 * </p>
 * 
 */
public class Generator {
    Pointer pplObj;

    /**
     * Enumerates the possible types of a generator.
     */
    public enum GeneratorType {
        LINE, RAY, POINT, CLOSURE_POINT;

        /**
         * Returns the enum corrisponding to its ordinal value.
         *
         * @throws IllegalArgumentException if t is not a valid ordinal value.
         */
        static GeneratorType valueOf(int t) {
            switch (t) {
                case 0:
                    return LINE;
                case 1:
                    return RAY;
                case 2:
                    return POINT;
                case 3:
                    return CLOSURE_POINT;
            }
            throw new IllegalStateException("Unexpected generator type " + t);
        }
    }

    /**
     * Enumeration for the two possible generators of space dimension 0. It is used
     * by the constructor of Generator.
     */
    public static enum ZeroDimGenerator {
        /**
         * Represents the point that is the origin of the zero-dimensional space
         * \(\mathbb{R}^0\).
         */
        POINT,
        /**
         * Represents the point that is the origin of the zero-dimensional space
         * \(\mathbb{R}^0\), viewed as a closure point.
         */
        CLOSURE_POINT
    }

    private static class GeneratorCleaner implements Runnable {
        private Pointer pplObj;

        GeneratorCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Generator(pplObj);
        }
    }

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new GeneratorCleaner(pplObj));
    }

    /**
     * Creates a new generator of direction le and type t. If the generator to be
     * created is a point or a closure point, the divisor d is applied to le. For
     * other types of generators d is simply disregarded. The space dimension of the
     * new generator is equal to the space dimension of le.
     */
    public Generator(LinearExpression le, GeneratorType t, Coefficient d) {
        var pg = new PointerByReference();
        int result = ppl_new_Generator(pg, le.pplObj, t.ordinal(), d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    /**
     * Creates a new generator of direction le and type t. It is equivalent to
     * {@code Generator(le, t, Coefficient.ONE)}.
     */
    public Generator(LinearExpression le, GeneratorType t) {
        this(le, t, Coefficient.ONE);
    }

    /**
     * Creates a new zero-dimensional Generator according to type.
     */
    public Generator(ZeroDimGenerator type) {
        var pg = new PointerByReference();
        int result = type == ZeroDimGenerator.CLOSURE_POINT ? ppl_new_Generator_zero_dim_closure_point(pg)
                : ppl_new_Generator_zero_dim_point(pg);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    /**
     * Creates a new zero-dimensional Generator.
     */
    Generator() {
        this(ZeroDimGenerator.CLOSURE_POINT);
    }

    /**
     * Creates a copy of the Generator g.
     */
    public Generator(Generator g) {
        var pg = new PointerByReference();
        int result = ppl_new_Generator_from_Generator(pg, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    /**
     * Creates a Generator obtained from the specified native object.
     */
    Generator(Pointer pplObj) {
        init(pplObj);
    }

    /**
     * Set the value of this Generator to a copy of the Generator g.
     */
    public Generator assign(Generator g) {
        int result = ppl_assign_Generator_from_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Returns the space dimension of this Generator.
     */
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Generator_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    /**
     * Returns the type of this Generator.
     */
    public GeneratorType getType() {
        int result = ppl_Generator_type(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return GeneratorType.valueOf(result);
    }

    /**
     * Returns the Coefficient for the variable \(x_i\).
     */
    public Coefficient getCoefficient(long var) {
        var n = new Coefficient();
        int result = ppl_Generator_coefficient(pplObj, new SizeT(var), n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    /**
     * Returns the divisor of this Generator.
     */
    public Coefficient getDivisor() {
        var d = new Coefficient();
        int result = ppl_Generator_divisor(pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return d;
    }

    /**
     * Returns true if this Generator satisfies all its implementation invariants;
     * returns false and perhaps makes some noise if it is broken. Useful for
     * debugging purposes.
     */
    public boolean isOK() {
        int result = ppl_Generator_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Returns the string representation of this Generator.
     */
    @Override
    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Generator(pstr, pplObj);
        if (result < 0)
            throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

    /**
     * Returns whether obj is the same as this Generator. Two generators are the
     * same if they have the same space type, dimension, coefficients. For points and
     * closure point, the divisor should also be equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Generator) {
            var g = (Generator) obj;
            var t = getType();
            if (g.getType() != t)
                return false;
            if (g.getSpaceDimension() != getSpaceDimension())
                return false;
            if ((t == GeneratorType.POINT || t == GeneratorType.CLOSURE_POINT) && !g.getDivisor().equals(getDivisor()))
                return false;
            for (long d = 0; d < getSpaceDimension(); d++) {
                if (!g.getCoefficient(d).equals(getCoefficient(d)))
                    return false;
            }
            return true;
        }
        return false;
    }
}
