package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;
import it.unich.jppl.LibPPL.SizeTByReference;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class Generator {
    Pointer pplObj;

    public enum GeneratorType {
        LINE, RAY, POINT, CLOSURE_POINT;

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

    public static enum ZeroDimGenerator {
        POINT, CLOSURE_POINT
    }

    public static class RelationWithGenerator {
        public static final int SUBSUMES = 1;
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

    public Generator(LinearExpression le, GeneratorType t, Coefficient d) {
        var pg = new PointerByReference();
        int result = ppl_new_Generator(pg, le.pplObj, t.ordinal(), d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    public Generator(ZeroDimGenerator t) {
        var pg = new PointerByReference();
        int result = t == ZeroDimGenerator.CLOSURE_POINT ? ppl_new_Generator_zero_dim_closure_point(pg)
                : ppl_new_Generator_zero_dim_point(pg);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    public Generator(Generator g) {
        var pg = new PointerByReference();
        int result = ppl_new_Generator_from_Generator(pg, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    public Generator() {
        this(ZeroDimGenerator.POINT);
    }

    public Generator(LinearExpression le, GeneratorType t) {
        this(le, t, new Coefficient(1));
    }

    Generator(Pointer pplObj) {
        init(pplObj);
    }

    public Generator assign(Generator g) {
        int result = ppl_assign_Generator_from_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Generator_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    public GeneratorType getType() {
        int result = ppl_Generator_type(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return GeneratorType.valueOf(result);
    }

    public Coefficient getCoefficient(long var) {
        var n = new Coefficient();
        int result = ppl_Generator_coefficient(pplObj, new SizeT(var), n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    public Coefficient getDivisor() {
        var d = new Coefficient();
        int result = ppl_Generator_divisor(pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return d;
    }

    public boolean isOK() {
        int result = ppl_Generator_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

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
