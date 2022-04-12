package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;
import it.unich.jppl.LibPPL.SizeTByReference;

import java.math.BigInteger;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A linear expression.
 *
 * <p>
 * An object of the class LinearExpression represents the linear expression
 *
 * \[ \sum_{i=1}^n a_i x_i + b \]
 *
 * where \(n\) is the dimension of the vector space, each \(a_i\) is the integer
 * coefficient of the \(i\)-th variable \(x_i\) and \(b\) is the integer for the
 * inhomogeneous term.
 * </p>
 * <p>
 * Linear expressions are the basic blocks for defining both constraints (i.e.,
 * linear equalities or inequalities) and generators (i.e., lines, rays, points
 * and closure points). A full set of functions is defined to provide a
 * convenient interface for building complex linear expressions starting from
 * simpler ones and from objects of class Coefficient. Available operators
 * include unary negation, binary addition and subtraction, as well as
 * multiplication by a Coefficient. The space dimension of a linear expression
 * is defined as the maximum space dimension of the arguments used to build it:
 * in particular, the space dimension of a variable \(x_i\) is defined as
 * \(i+1\), whereas all the objects of the class Coefficient have space
 * dimension zero.
 * </p>
 * <p>
 * Almost all methods throw {@link PPLError} when the underlying PPL library
 * generates an error.
 * </p>
 */
public class LinearExpression extends GeometricDescriptor<LinearExpression> {

    private static class LinearExpressionCleaner implements Runnable {
        private Pointer pplObj;

        LinearExpressionCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Linear_Expression(pplObj);
        }
    }

    private LinearExpression(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new LinearExpressionCleaner(pplObj));
    }

    /**
     * Creates and returns a linear expression corresponding to the constant 0 in a
     * zero-dimensional space.
     */
    public static LinearExpression zero() {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression(ple);
        if (result < 0)
            throw new PPLError(result);
        return new LinearExpression(ple.getValue());
    }

    /**
     * Creates and returns a linear expression corresponding the constant 0 in a
     * d-dimensional space.
     */
    public static LinearExpression zero(long d) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_with_dimension(ple, new SizeT(d));
        if (result < 0)
            throw new PPLError(result);
        return new LinearExpression(ple.getValue());
    }

    /**
     * Copies and returns the linear expression contained in the constraint c.
     */
    public static LinearExpression of(Constraint c) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_Constraint(ple, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new LinearExpression(ple.getValue());
    }

    /**
     * Copies and returns the linear expression contained in the generator g.
     */
    public static LinearExpression of(Generator g) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_Generator(ple, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new LinearExpression(ple.getValue());
    }

    /**
     * Copies and returns the linear expression contained in the congruence c.
     */
    public static LinearExpression of(Congruence c) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_Congruence(ple, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new LinearExpression(ple.getValue());
    }

    /*
    public static LinearExpression of(GridGenerator g) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_GridGenerator(ple, g.pplObj);
        if (result < 0) throw new PPLError(result);
        return new LinearExpression(ple.getValue());
    }
    */

    @Override
    public LinearExpression clone() {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_Linear_Expression(ple, pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new LinearExpression(ple.getValue());
    }

    @Override
    public LinearExpression assign(LinearExpression le) {
        int result = ppl_assign_Linear_Expression_from_Linear_Expression(pplObj, le.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Linear_Expression_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    @Override
    public Coefficient getCoefficient(long i) {
        var c = Coefficient.zero();
        int result = ppl_Linear_Expression_coefficient(pplObj, new SizeT(i), c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return c;
    }

    /**
     * Returns the inhomogeneous term of this linear expression.
     */
    public Coefficient getInhomogeneousTerm() {
        var c = Coefficient.zero();
        int result = ppl_Linear_Expression_inhomogeneous_term(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return c;
    }

    @Override
    boolean isOK() {
        int result = ppl_Linear_Expression_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Returns true if and only if this linear expression is the constant zero.
     */
    public boolean isZero() {
        int result = ppl_Linear_Expression_is_zero(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Returns true if and only if this linear expression is a constant.
     */
    public boolean isConstant() {
        int result = ppl_Linear_Expression_all_homogeneous_terms_are_zero(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Adds the term \(c \cdot x_i\) to this linear expression.
     *
     * @return this linear expression.
     */
    public LinearExpression add(Coefficient c, long i) {
        int result = ppl_Linear_Expression_add_to_coefficient(pplObj, new SizeT(i), c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Add the inhomogeneous term \(c\) to this linear expression.
     *
     * @return this linear expression.
     */
    public LinearExpression add(Coefficient c) {
        int result = ppl_Linear_Expression_add_to_inhomogeneous(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Add the linear expression le to this linear expression.
     *
     * @return this linear expression.
     */
    public LinearExpression add(LinearExpression le) {
        int result = ppl_add_Linear_Expression_to_Linear_Expression(pplObj, le.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Multiply this linear expression by the constant c.
     *
     * @return this linear expression.
     */
    public LinearExpression multiply(Coefficient c) {
        int result = ppl_multiply_Linear_Expression_by_Coefficient(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Linear_Expression(pstr, pplObj);
    }

    /**
     * Returns whether obj is the same as this linear expressions. Two linear
     * expressions are the same if they have the same coefficients, even if their
     * space dimension differs.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof LinearExpression) {
            var le1 = (LinearExpression) obj;
            var le2 = this.clone();
            le2.multiply(Coefficient.MINUS_ONE);
            le2.add(le1);
            return le2.isZero();
        }
        return false;
    }

    /**
     * Convenience method which builds a linear expression given a list of
     * coefficients, starting with the inhomogeneous term and continuing with the
     * coefficient of the variables \(x_0, \x_1, \ldots\)
     */
    public static LinearExpression of(Coefficient c, Coefficient... args) {
        var le = LinearExpression.zero(args.length);
        le.add(c);
        for (int i = 0; i < args.length; i++) {
            le.add(args[i], i);
        }
        return le;
    }

    /**
     * Similar to the {@link #of(Coefficient c, Coefficient... args) of(Coefficient
     * c, Coefficient... args)} constructor but with BigInteger parameters.
     */
    public static LinearExpression of(BigInteger c, BigInteger... args) {
        var le = LinearExpression.zero(args.length);
        le.add(Coefficient.valueOf(c));
        for (int i = 0; i < args.length; i++) {
            le.add(Coefficient.valueOf(args[i]), i);
        }
        return le;
    }

    /**
     * Similar to {@link #of(Coefficient c, Coefficient... args) of(Coefficient c,
     * Coefficient... args)} constructor but with long parameters.
     */
    public static LinearExpression of(long c, long... args) {
        var le = LinearExpression.zero(args.length);
        le.add(Coefficient.valueOf(c));
        for (int i = 0; i < args.length; i++) {
            le.add(Coefficient.valueOf(args[i]), i);
        }
        return le;
    }

}
