package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.nativelib.LibPPL.SizeT;
import it.unich.jppl.nativelib.LibPPL.SizeTByReference;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

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

    public LinearExpression() {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression(ple);
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }

    // TODO: this methods causes exceptions during tests under the VSCode Test Runner
    public LinearExpression(long d) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_with_dimension(ple, new SizeT(d));
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }

    public LinearExpression(Constraint c) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_Constraint(ple, c.pplObj);
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }

    public LinearExpression(Generator g) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_Generator(ple, g.pplObj);
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }

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

    public LinearExpression(LinearExpression le) {
        var ple = new PointerByReference();
        int result = ppl_new_Linear_Expression_from_Linear_Expression(ple, le.pplObj);
        if (result < 0) throw new PPLError(result);
        init(ple.getValue());
    }

    public LinearExpression assign(LinearExpression le) {
        int result = ppl_assign_Linear_Expression_from_Linear_Expression(pplObj, le.pplObj);
        if (result < 0) throw new PPLError(result);
        return this;
    }

    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Linear_Expression_space_dimension(pplObj, m);
        if (result < 0) throw new PPLError(result);
        return m.getValue().longValue();
    }

    public Coefficient getCoefficient(long var) {
        var c = new Coefficient();
        int result = ppl_Linear_Expression_coefficient(pplObj, new SizeT(var), c.pplObj);
        if (result < 0) throw new PPLError(result);
        return c;
    }

    public Coefficient getCoefficient() {
        var c = new Coefficient();
        int result = ppl_Linear_Expression_inhomogeneous_term(pplObj, c.pplObj);
        if (result < 0) throw new PPLError(result);
        return c;
    }

    public boolean isOK() {
        int result = ppl_Linear_Expression_OK(pplObj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean isZero() {
        int result = ppl_Linear_Expression_is_zero(pplObj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public boolean allHomogeneousTermsAreZero() {
        int result = ppl_Linear_Expression_all_homogeneous_terms_are_zero(pplObj);
        if (result < 0) throw new PPLError(result);
        return result > 0;
    }

    public LinearExpression add(Coefficient c, long d) {
        int result = ppl_Linear_Expression_add_to_coefficient(pplObj, new SizeT(d), c.pplObj);
        if (result < 0) throw new PPLError(result);
        return this;
    }

    public LinearExpression add(Coefficient c) {
        int result = ppl_Linear_Expression_add_to_inhomogeneous(pplObj, c.pplObj);
        if (result < 0) throw new PPLError(result);
        return this;
    }

    public LinearExpression add(LinearExpression le) {
        int result = ppl_add_Linear_Expression_to_Linear_Expression(pplObj, le.pplObj);
        if (result < 0) throw new PPLError(result);
        return this;
    }

    public LinearExpression multiply(Coefficient c) {
        int result = ppl_multiply_Linear_Expression_by_Coefficient(pplObj, c.pplObj);
        if (result < 0) throw new PPLError(result);
        return this;
    }

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
