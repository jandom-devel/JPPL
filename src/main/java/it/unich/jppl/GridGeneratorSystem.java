package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeTByReference;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A system of grid generators.
 *
 * <p>
 * An object of the class Grid_Generator_System is a system of grid generators,
 * i.e., a multiset of objects of the class Grid_Generator (lines, parameters
 * and points). When inserting generators in a system, space dimensions are
 * automatically adjusted so that all the generators in the system are defined
 * on the same vector space. A system of grid generators which is meant to
 * define a non-empty grid must include at least one point: the reason is that
 * lines and parameters need a supporting point (lines only specify directions
 * while parameters only specify direction and distance.
 * </p>
 */
public class GridGeneratorSystem implements Iterable<Generator> {
    Pointer pplObj;

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
     * An iterator over a GridGeneratorSystem.
     *
     * <p>
     * Note that the grid generators extracted from the iterator are not going to
     * survive operations which manipulate the original GridGeneratorSystem.
     * </p>
     */
    public class GridGeneratorSystemIterator implements Iterator<Generator> {
        private Pointer cit;
        private Pointer cend;

        GridGeneratorSystemIterator() {
            var pgsit = new PointerByReference();
            int result = ppl_new_Grid_Generator_System_const_iterator(pgsit);
            if (result < 0)
                throw new PPLError(result);
            cit = pgsit.getValue();
            PPL.cleaner.register(this, new GridGeneratorSystemIteratorCleaner(cit));
            result = ppl_Grid_Generator_System_begin(pplObj, cit);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_new_Grid_Generator_System_const_iterator(pgsit);
            if (result < 0)
                throw new PPLError(result);
            cend = pgsit.getValue();
            PPL.cleaner.register(this, new GridGeneratorSystemIteratorCleaner(cend));
            result = ppl_Grid_Generator_System_end(pplObj, cend);
            if (result < 0)
                throw new PPLError(result);
        }

        /**
         * Returns true if the iteration has more elements.
         */
        @Override
        public boolean hasNext() {
            int result = ppl_Grid_Generator_System_const_iterator_equal_test(cit, cend);
            if (result < 0)
                throw new PPLError(result);
            return result == 0;
        }

        /**
         * Returns the next element in the iteration. Note that the grid generators
         * extracted from the iterator are not going to survive operations which
         * manipulate the original GridGeneratorSystem.
         */
        @Override
        public Generator next() {
            if (!hasNext())
                throw new NoSuchElementException();
            var pc = new PointerByReference();
            int result = ppl_Grid_Generator_System_const_iterator_dereference(cit, pc);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_Grid_Generator_System_const_iterator_increment(cit);
            if (result < 0)
                throw new PPLError(result);
            return new Generator(pc.getValue());
        }
    }

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new GridGeneratorSystemCleaner(pplObj));
    }

    /**
     * Returns an empty zero-dimensional GridGeneratorSystem.
     */
    public GridGeneratorSystem() {
        var pgs = new PointerByReference();
        int result = ppl_new_Grid_Generator_System(pgs);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    /**
     * Returns a new GeneratorSystem containing only a copy of the GridGenerator c.
     */
    public GridGeneratorSystem(GridGenerator g) {
        var pgs = new PointerByReference();
        int result = ppl_new_Grid_Generator_System_from_Grid_Generator(pgs, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    /**
     * Returns a copy of the GridGeneratorSystem gs.
     */
    public GridGeneratorSystem(GridGeneratorSystem gs) {
        var pgs = new PointerByReference();
        int result = ppl_new_Grid_Generator_System_from_Grid_Generator_System(pgs, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    /**
     * Returns a GridGeneratorSystem obtained from the specified native object.
     */
    GridGeneratorSystem(Pointer pplObj) {
        init(pplObj);
    }

    /**
     * Set the value of this GridGeneratorSystem to a copy of the
     * GridGeneratorSystem gs.
     */
    public GridGeneratorSystem assign(GridGeneratorSystem gs) {
        int result = ppl_assign_Grid_Generator_System_from_Grid_Generator_System(pplObj, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Returns the space dimension of this GridGeneratorSystem.
     */
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Grid_Generator_System_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    /**
     * Return true if and only if this GridGeneratorSystem contains no generators.
     */
    public boolean isEmpty() {
        int result = ppl_Grid_Generator_System_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Returns true if this GridGeneratorSystem satisfies all its implementation
     * invariants; returns false and perhaps makes some noise if it is broken.
     * Useful for debugging purposes.
     */
    public boolean isOK() {
        int result = ppl_Grid_Generator_System_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Removes all the generators from this GridGeneratorSystem.
     */
    public GridGeneratorSystem clear() {
        int result = ppl_Grid_Generator_System_clear(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Add the GridGenerator g to this GridGeneratorSystem.
     */
    public GridGeneratorSystem add(GridGenerator g) {
        int result = ppl_Grid_Generator_System_insert_Grid_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Returns an iterator for this GridGeneratorSystem.
     */
    public Iterator<Generator> iterator() {
        return new GridGeneratorSystemIterator();
    }

    /**
     * Returns a string representation of this GeneratorSystem.
     */
    @Override
    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Grid_Generator_System(pstr, pplObj);
        if (result < 0)
            throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
