package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import java.math.BigInteger;

import it.unich.jppl.nativelib.LibPPL.SizeT;
import it.unich.jppl.nativelib.LibPPL.SizeTByReference;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * An object of the class Linear_Expression represents the linear expression
 * where <math>n</math> is the dimension of the vector space, each <math>a_i</math> is the integer coefficient of the
 * <math>i</math>-th variable <math>x<sub>i</sub></math> and <math>b</math> is the integer for the inhomogeneous term.
 * <p>
 * Linear expressions are the basic blocks for defining both constraints (i.e., linear equalities or inequalities)
 * and generators (i.e., lines, rays, points and closure points). A full set of functions is defined to provide a
 * convenient interface for building complex linear expressions starting from simpler ones and from objects of
 * class Coefficient. Available operators include unary negation, binary addition and subtraction, as well as
 * multiplication by a Coefficient. The space dimension of a linear expression is defined as the maximum space
 * dimension of the arguments used to build it: in particular, the space dimension of a variable <code>x</code>
 * is defined as <code>x.id()+1</code>, whereas all the objects of the class Coefficient have space dimension zero.
 */
public class LinearExpression {
    Pointer pplObj;

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

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new LinearExpressionCleaner(pplObj));
    }

    /**
     * Returns a new linear expression corresponding to the constant 0 in a zero-dimensional space.
     */
    public LinearExpression() {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression(ple);
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }

    /**
     * Returns a new linear expression corresponding the constant 0 in a d-dimensional space.
     */
    LinearExpression(long d) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_with_dimension(ple, new SizeT(d));
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }

    /**
     * Returns a linear expression corresponding to the Constraint c.
     */
    public LinearExpression(Constraint c) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_Constraint(ple, c.pplObj);
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }

    /**
     * Returns a linear expression corresponding to the Generator g.
     */
    public LinearExpression(Generator g) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_Generator(ple, g.pplObj);
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }

    /**
     * Returns a linear expression corresponding to the Conguence g.
     */
    public LinearExpression(Congruence c) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_Congruence(ple, c.pplObj);
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }

    /*
    public LinearExpression(GridGenerator g) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_GridGenerator(ple, g.pplObj);
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }
    */

    /**
     * Returns a copy of the linear expressione le.
     */
    public LinearExpression(LinearExpression le) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_Linear_Expression(ple, le.pplObj);
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }

    /**
     * Convenience constructor which builds a linear expression by a list of
     * coefficients, starting with the inhomogeneous term and continuing with
     * the coefficient of the variables <math>x<sub>0</sub>, x<sub>1</sub>, ....
     * </math>.
     */
    public LinearExpression(Coefficient c, Coefficient... args) {
        super();
        add(c);
        for (int i = 0; i < args.length; i++) {
            add(args[i], i);
        }
    }

    /**
     * Similar to the {@link #LinearExpression(Coefficient c, Coefficient... args)
     * LinearExpression(Coefficient c, Coefficient... args)} constructor but with BigInteger parameters.
     */
    public LinearExpression(BigInteger c, BigInteger... args) {
        super();
        add(new Coefficient(c));
        for (int i = 0; i < args.length; i++) {
            add(new Coefficient(args[i]), i);
        }
    }

    /**
     * Similar to {@link #LinearExpression(Coefficient c, Coefficient... args)
     * LinearExpression(Coefficient c, Coefficient... args)} constructor but with long parameters.
     */
    public LinearExpression(long c, long... args) {
        super();
        add(new Coefficient(c));
        for (int i = 0; i < args.length; i++) {
            add(new Coefficient(args[i]), i);
        }
    }

    /**
     * Assign to this linear expression a copy of the linear expressione le.
     */
    public LinearExpression set(LinearExpression le) {
        int result = ppl_assign_Linear_Expression_from_Linear_Expression(pplObj, le.pplObj);
        if (result < 0) throw new PPLError(result);
        return this;
    }

    /**
     * Returns the space dimension of this linear expression.
     */
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Linear_Expression_space_dimension(pplObj, m);
        if (result < 0) throw new PPLError(result);
        return m.getValue().longValue();
    }

    /**
     * Returns the Coefficient for to the variable <math>x<sub>i</sub><math>.
     */
    public Coefficient getCoefficient(long i) {
        var c = new Coefficient();
        int result = ppl_Linear_Expression_coefficient(pplObj, new SizeT(i), c.pplObj);
        if (result < 0) throw new PPLError(result);
        return c;
    }

    /**
     * Returns the inhomogeneous term.
     */
    public Coefficient getCoefficient() {
        var c = new Coefficient();
        int result = ppl_Linear_Expression_inhomogeneous_term(pplObj, c.pplObj);
        if (result < 0) throw new PPLError(result);
        return c;
    }

    /**
     * Returns true if this Coefficient satisfies all its implementation invariants; returns false and perhaps makes
     * some noise if it is broken. Useful for debugging purposes.
     */
    boolean isOK() {
        int result = ppl_Linear_Expression_OK(pplObj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    /**
     * Returns true if and only if this linear expression is zero.
     */
    public boolean isZero() {
        int result = ppl_Linear_Expression_is_zero(pplObj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    /**
     * Returns true if and only if this linear expression is a constant.
     */
    public boolean isConstant() {
        int result = ppl_Linear_Expression_all_homogeneous_terms_are_zero(pplObj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    /**
     * Add the linear expression <math>c &middot; x<sub>i</sub></math> to this linear expression.
     * @return this linear expression.
     */
    public LinearExpression add(Coefficient c, long i) {
        int result = ppl_Linear_Expression_add_to_coefficient(pplObj, new SizeT(i), c.pplObj);
        if (result < 0) throw new PPLError(result);
        return this;
    }

    /**
     * Add the inhomogeneous term <math>c</math> to this linear expression.
     * @return this linear expression.
     */
    public LinearExpression add(Coefficient c) {
        int result = ppl_Linear_Expression_add_to_inhomogeneous(pplObj, c.pplObj);
        if (result < 0) throw new PPLError(result);
        return this;
    }

    /**
     * Add the linear expression le to this linear expression.
     * @return this linear expression.
     */
    public LinearExpression add(LinearExpression le) {
        int result = ppl_add_Linear_Expression_to_Linear_Expression(pplObj, le.pplObj);
        if (result < 0) throw new PPLError(result);
        return this;
    }

    /**
     * Multiply this linear expression by the constant c.
     * @return this linear expression.
     */
    public LinearExpression multiply(Coefficient c) {
        int result = ppl_multiply_Linear_Expression_by_Coefficient(pplObj, c.pplObj);
        if (result < 0) throw new PPLError(result);
        return this;
    }

    /**
     * Returns the string representation of the linear expression.
     */
    @Override
    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Linear_Expression(pstr, pplObj);
        if (result < 0) throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

    /**
     * Returns whether obj is the sama as this linear expressions. Two linear expressions
     * are the same if they have the same coefficients, even if their space dimension differs.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof LinearExpression) {
            var le1 = (LinearExpression) obj;
            var le2 = new LinearExpression(this);
            le2.multiply(new Coefficient(-1));
            le2.add(le1);
            return le2.isZero();
        }
        return false;
    }
}
