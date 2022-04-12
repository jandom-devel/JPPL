package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;
import it.unich.jppl.LibPPL.SizeTByReference;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A grid line, parameter or grid point.
 *
 * <p>
 * An object of the class GridGenerator is one of the following:
 * </p>
 * <ul>
 * <li>a grid line \(\vec{l} = (a_0, \ldots, a_{n-1})^T\);
 * <li>a parameter \(\vec{q} = (\frac{a_0}{d}, \ldots, \frac{a_{n-1}}{d})^T\);
 * <li>a grid point \(\vec{p} = (\frac{a_0}{d}, \ldots, \frac{a_{n-1}}{d})^T\);
 * </ul>
 * <p>
 * where \(n\) is the dimension of the space and, for grid_points and
 * parameters, \(d &gt; 0\) is the divisor.
 * </p>
 */
public class GridGenerator extends GeometricDescriptor<GridGenerator> {

    /**
     * Enumerates the possible types of a grid generator.
     */
    public enum GridGeneratorType {
        LINE, PARAMETER, POINT;

        /**
         * Returns the enum corrisponding to its ordinal value.
         *
         * @throws IllegalArgumentException if t is not a valid ordinal value.
         */
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

    /**
     * Creates a new grid generator of direction le and type t. If the grid
     * generator to be created is a point or a parameter, the divisor d is applied
     * to le. If it is a line, d is simply disregarded. The space dimension of the
     * new grid generator is equal to the space dimension of le.
     */
    public GridGenerator(LinearExpression le, GridGeneratorType t, Coefficient d) {
        var pg = new PointerByReference();
        int result = ppl_new_Grid_Generator(pg, le.pplObj, t.ordinal(), d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    /**
     * Creates a new grid generator of direction le and type t. It is equivalent to
     * {@code GridGenerator(le, t, Coefficient.ONE)}.
     */
    public GridGenerator(LinearExpression le, GridGeneratorType t) {
        this(le, t, new Coefficient(1));
    }

    /**
     * Creates the point that is the origin of the zero-dimensional space
     * \(\mathbb{R}^0\).
     */
    public GridGenerator() {
        var pg = new PointerByReference();
        int result = ppl_new_Grid_Generator_zero_dim_point(pg);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    /**
     * Creates a copy of the GridGenerator g.
     */
    public GridGenerator(GridGenerator g) {
        var pg = new PointerByReference();
        int result = ppl_new_Grid_Generator_from_Grid_Generator(pg, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pg.getValue());
    }

    /**
     * Creates a GridGenerator obtained from the specified native object.
     */
    GridGenerator(Pointer pplObj) {
        init(pplObj);
    }

    @Override
    GridGenerator assign(GridGenerator g) {
        int result = ppl_assign_Grid_Generator_from_Grid_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Grid_Generator_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    @Override
    public Coefficient getCoefficient(long var) {
        var n = new Coefficient();
        int result = ppl_Grid_Generator_coefficient(pplObj, new SizeT(var), n.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return n;
    }

    @Override
    public boolean isOK() {
        int result = ppl_Grid_Generator_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Grid_Generator(pstr, pplObj);
    }

    /**
     * Returns whether obj is the same as this GridGenerator. Two grid generators
     * are the same if they have the same type, space dimensions and coefficients.
     * For points and parametes, the divisor should also be equal.
     */
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

    /**
     * Returns the type of this GridGenerator.
     */
    public GridGeneratorType getType() {
        int result = ppl_Grid_Generator_type(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return GridGeneratorType.valueOf(result);
    }

    /**
     * Returns the divisor of this GridGenerator.
     */
    public Coefficient getDivisor() {
        var d = new Coefficient();
        int result = ppl_Grid_Generator_divisor(pplObj, d.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return d;
    }

}
