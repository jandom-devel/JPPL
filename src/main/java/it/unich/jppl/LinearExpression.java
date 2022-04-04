package it.unich.jppl;

import com.sun.jna.Pointer;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;

import static it.unich.jppl.nativelib.LibPPL.*;

/**
 * Created by amato on 17/03/16.
 */
public class LinearExpression {
    Pointer obj;

    private static class LinearExpressionCleaner implements Runnable {
        private Pointer obj;

        LinearExpressionCleaner(Pointer obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Linear_Expression(obj);
        }
    }

    public Pointer getNative() {
        return obj;
    }

    public LinearExpression() {
        PointerByReference ple = new PointerByReference();
        ppl_new_Linear_Expression(ple);
        obj = ple.getValue();
        PPL.cleaner.register(this, new LinearExpressionCleaner(obj));
    }

    public LinearExpression(long d) {
        PointerByReference ple = new PointerByReference();
        ppl_new_Linear_Expression_with_dimension(ple, new Dimension(d));
        obj = ple.getValue();
        PPL.cleaner.register(this, new LinearExpressionCleaner(obj));
    }

    public LinearExpression(Constraint c) {
        PointerByReference ple = new PointerByReference();
        ppl_new_Linear_Expression_from_Constraint(ple, c.obj);
        obj = ple.getValue();
        PPL.cleaner.register(this, new LinearExpressionCleaner(obj));
    }

    /*
    public LinearExpression(Generator g) {
        PointerByReference ple = new PointerByReference();
        ppl_new_Linear_Expression_from_Generator(ple, g.obj);
        obj = ple.getValue();
        PPL.cleaner.register(this, new LinearExpressionCleaner(obj));
    }
    */

    /*
    public LinearExpression(Congruence c) {
        PointerByReference ple = new PointerByReference();
        ppl_new_Linear_Expression_from_Congruence(ple, c.obj);
        obj = ple.getValue();
        PPL.cleaner.register(this, new LinearExpressionCleaner(obj));
    }
    */

    /*
    public LinearExpression(GriGenerator g) {
        PointerByReference ple = new PointerByReference();
        ppl_new_Linear_Expression_from_GridGenerator(ple, g.obj);
        obj = ple.getValue();
        PPL.cleaner.register(this, new LinearExpressionCleaner(obj));
    }
    */

    public LinearExpression(LinearExpression le) {
        PointerByReference ple = new PointerByReference();
        ppl_new_Linear_Expression_from_Linear_Expression(ple, le.obj);
        obj = ple.getValue();
        PPL.cleaner.register(this, new LinearExpressionCleaner(obj));
    }

    public LinearExpression assign(LinearExpression le) {
        ppl_assign_Linear_Expression_from_Linear_Expression(obj, le.obj);
        return this;
    }

    public long getSpaceDimension() {
        var dref = new DimensionByReference();
        ppl_Linear_Expression_space_dimension(obj, dref);
        return dref.getValue().longValue();
    }

    public Coefficient getCoefficient(long var) {
        var c = new Coefficient();
        ppl_Linear_Expression_coefficient(obj, new Dimension(var), c.obj);
        return c;
    }

    public Coefficient getCoefficient() {
        var c = new Coefficient();
        ppl_Linear_Expression_inhomogeneous_term(obj, c.obj);
        return c;
    }

    public boolean isOK() {
        return ppl_Linear_Expression_OK(obj) > 0;
    }

    public boolean isZero() {
        return ppl_Linear_Expression_is_zero(obj);
    }

    public boolean allHomogeneousTermsAreZero() {
        return ppl_Linear_Expression_all_homogeneous_terms_are_zero(obj);
    }

    public LinearExpression add(Coefficient c, long d) {
        ppl_Linear_Expression_add_to_coefficient(obj, new Dimension(d), c.obj);
        return this;
    }

    public LinearExpression add(Coefficient c) {
        ppl_Linear_Expression_add_to_inhomogeneous(obj, c.obj);
        return this;
    }

    public LinearExpression add(LinearExpression le) {
        ppl_add_Linear_Expression_to_Linear_Expression(obj, le.obj);
        return this;
    }

    public LinearExpression multiply(Coefficient c) {
        ppl_multiply_Linear_Expression_by_Coefficient (obj, c.obj);
        return this;
    }

    @Override
    public String toString() {
        PointerByReference pstr = new PointerByReference();
        ppl_io_asprint_Linear_Expression(pstr, obj);
        Pointer p = pstr.getValue();
        String s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof LinearExpression) {
            var le = (LinearExpression) obj;
            var le2 = new LinearExpression(this);
            le2.multiply(new Coefficient(-1));
            le2.add(le);
            return le2.isZero();
        }
        return false;
    }
}
