package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.nativelib.SizeTByReference;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A system of grid generators.
 *
 * <p>
 * A system of grid generators which is meant to define a non-empty grid must
 * include at least one point: the reason is that lines and parameters need a
 * supporting point (lines only specify directions while parameters only specify
 * direction and distance.
 * </p>
 */
public class GridGeneratorSystem extends AbstractPPLObject<GridGeneratorSystem>
        implements GeometricDescriptorsSystem<GridGenerator, GridGeneratorSystem> {

    private static class GridGeneratorSystemCleaner implements Runnable {
        private Pointer pplObj;

        GridGeneratorSystemCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Grid_Generator_System(pplObj);
        }
    }

    private static class GridGeneratorSystemIteratorCleaner implements Runnable {
        private Pointer pplObj;

        GridGeneratorSystemIteratorCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Grid_Generator_System_const_iterator(pplObj);
        }
    }

    /**
     * An iterator over a grid generator system.
     *
     * <p>
     * Note that the grid generators extracted from the iterator are not going to
     * survive operations which manipulate the original grid generator system.
     * </p>
     */
    public class GridGeneratorSystemIterator implements Iterator<GridGenerator> {
        private Pointer cit;
        private Pointer cend;

        GridGeneratorSystemIterator() {
            var pgsit = new PointerByReference();
            int result = ppl_new_Grid_Generator_System_const_iterator(pgsit);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            cit = pgsit.getValue();
            PPL.cleaner.register(this, new GridGeneratorSystemIteratorCleaner(cit));
            result = ppl_Grid_Generator_System_begin(pplObj, cit);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            result = ppl_new_Grid_Generator_System_const_iterator(pgsit);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            cend = pgsit.getValue();
            PPL.cleaner.register(this, new GridGeneratorSystemIteratorCleaner(cend));
            result = ppl_Grid_Generator_System_end(pplObj, cend);
            if (result < 0)
                PPLRuntimeException.checkError(result);
        }

        /**
         * Returns true if the iteration has more elements.
         */
        @Override
        public boolean hasNext() {
            int result = ppl_Grid_Generator_System_const_iterator_equal_test(cit, cend);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            return result == 0;
        }

        /**
         * Returns the next element in the iteration. Note that the grid generators
         * extracted from the iterator are not going to survive operations which
         * manipulate the original grid generator system.
         */
        @Override
        public GridGenerator next() {
            if (!hasNext())
                throw new NoSuchElementException();
            var pc = new PointerByReference();
            int result = ppl_Grid_Generator_System_const_iterator_dereference(cit, pc);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            result = ppl_Grid_Generator_System_const_iterator_increment(cit);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            return new GridGenerator(pc.getValue(), false);
        }
    }

    /**
     * Creates a grid generator system obtained from the specified native object.
     *
     * @param registerCleaner if true, the native object is registered for deletion
     *                        when the frid generator system is garbage collected.
     */
    GridGeneratorSystem(Pointer p, boolean registerCleaner) {
        pplObj = p;
        if (registerCleaner)
            PPL.cleaner.register(this, new GridGeneratorSystemCleaner(pplObj));
    }

    /**
     * Creates a grid generator system obtained from the specified native object. It
     * is equivalent to {@code GridGeneratorSystem(p, true)}.
     */
    private GridGeneratorSystem(Pointer p) {
        this(p, false);
    }

    /**
     * Creates and returns an empty zero-dimensional grid generator system.
     */
    public static GridGeneratorSystem empty() {
        var pgs = new PointerByReference();
        int result = ppl_new_Grid_Generator_System(pgs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new GridGeneratorSystem(pgs.getValue());
    }

    /**
     * Creates and returns a new grid generator system containing only a copy of the
     * grid generator {@code g}.
     */
    public static GridGeneratorSystem of(GridGenerator g) {
        var pgs = new PointerByReference();
        int result = ppl_new_Grid_Generator_System_from_Grid_Generator(pgs, g.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new GridGeneratorSystem(pgs.getValue());
    }

    @Override
    public GridGeneratorSystem clone() {
        var pgs = new PointerByReference();
        int result = ppl_new_Grid_Generator_System_from_Grid_Generator_System(pgs, pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new GridGeneratorSystem(pgs.getValue());
    }

    @Override
    GridGeneratorSystem assign(GridGeneratorSystem gs) {
        int result = ppl_assign_Grid_Generator_System_from_Grid_Generator_System(pplObj, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Grid_Generator_System_space_dimension(pplObj, m);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return m.getValue().longValue();
    }

    @Override
    public boolean isEmpty() {
        int result = ppl_Grid_Generator_System_empty(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    boolean isOK() {
        int result = ppl_Grid_Generator_System_OK(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public GridGeneratorSystem clear() {
        int result = ppl_Grid_Generator_System_clear(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public GridGeneratorSystem add(GridGenerator g) {
        int result = ppl_Grid_Generator_System_insert_Grid_Generator(pplObj, g.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public Iterator<GridGenerator> iterator() {
        return new GridGeneratorSystemIterator();
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Grid_Generator_System(pstr, pplObj);
    }

}
