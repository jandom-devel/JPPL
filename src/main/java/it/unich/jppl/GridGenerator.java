package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;
import it.unich.jppl.LibPPL.SizeTByReference;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class GridGenerator {
    Pointer pplObj;

    public enum GridGeneratorType {
        LINE, PARAMETER, POINT;

        static GridGeneratorType valueOf(int t) {
            switch (t) {
            case 0:
                return LINE;
            case 1:
                return PARAMETER;
            case 2:
                return POINT;
            }
            throw new IllegalStateException("Unexpected GridGenerator type " + t);
        }
    }

    public static enum ZeroDimGridGenerator {
        POINT, CLOSURE_POINT
    }

    public static class RelationWithGridGenerator {
        public static final int SUBSUMES = 1;
    }

    private static class GridGeneratorCleaner implements Runnable {
        private Pointer pplObj;

        GridGeneratorCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Grid_Generator(pplObj);
        }
    }

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new GridGeneratorCleaner(pplObj));
    }

    public GridGenerator(LinearExpression le, GridGeneratorType t, Coefficient d) {
        var pg = new PointerByReference();
        int result = ppl_new_Grid_Generator(pg, le.pplObj, t.ordinal(), d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    public GridGenerator() {
        var pg = new PointerByReference();
        int result = ppl_new_Grid_Generator_zero_dim_point(pg);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    public GridGenerator(GridGenerator g) {
        var pg = new PointerByReference();
        int result = ppl_new_Grid_Generator_from_Grid_Generator(pg, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    public GridGenerator(LinearExpression le, GridGeneratorType t) {
        this(le, t, new Coefficient(1));
    }

    GridGenerator(Pointer pplObj) {
        init(pplObj);
    }

    public GridGenerator assign(GridGenerator g) {
        int result = ppl_assign_Grid_Generator_from_Grid_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Grid_Generator_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    public GridGeneratorType getType() {
        int result = ppl_Grid_Generator_type(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return GridGeneratorType.valueOf(result);
    }

    public Coefficient getCoefficient(long var) {
        var n = new Coefficient();
        int result = ppl_Grid_Generator_coefficient(pplObj, new SizeT(var), n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    public Coefficient getDivisor() {
        var d = new Coefficient();
        int result = ppl_Grid_Generator_divisor(pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return d;
    }

    public boolean isOK() {
        int result = ppl_Grid_Generator_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Grid_Generator(pstr, pplObj);
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
        if (obj instanceof GridGenerator) {
            var g = (GridGenerator) obj;
            var t = getType();
            if (g.getType() != t)
                return false;
            if (g.getSpaceDimension() != getSpaceDimension())
                return false;
            if ((t == GridGeneratorType.POINT || t == GridGeneratorType.PARAMETER)
                    && !g.getDivisor().equals(getDivisor()))
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
