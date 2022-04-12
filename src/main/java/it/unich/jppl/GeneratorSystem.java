package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeTByReference;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A system of generators.
 *
 * <p>
 * An object of the class GeneratorSystem is a system of generators, i.e., a
 * multiset of objects of the class Generator (lines, rays, points and closure
 * points). When inserting generators in a system, space dimensions are
 * automatically adjusted so that all the generators in the system are defined
 * on the same vector space. A system of generators which is meant to define a
 * non-empty polyhedron must include at least one point: the reason is that
 * lines, rays and closure points need a supporting point (lines and rays only
 * specify directions while closure points only specify points in the
 * topological closure of the NNC polyhedron).
 * </p>
 */
public class GeneratorSystem implements Iterable<Generator> {
    Pointer pplObj;

    /**
     * Enumerates the possible zero-dimensional GeneratorSystem which it is
     * possible to build with the GeneratorSystem constructor.
     */
    public enum ZeroDimGeneratorSystem {
        /** Represents the empty zero-dimensional GeneratorSystem. */
        EMPTY,
        UNSATISFIABLE
    }

    private static class GeneratorSystemCleaner implements Runnable {
        private Pointer pplObj;

        GeneratorSystemCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Generator_System(pplObj);
        }
    }

    private static class GeneratorSystemIteratorCleaner implements Runnable {
        private Pointer pplObj;

        GeneratorSystemIteratorCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Generator_System_const_iterator(pplObj);
        }
    }

    /**
     * An iterator over a GeneratorSystem.
     *
     * <p>
     * Note that the generators extracted from the iterator are not going to
     * survive operations which manipulate the original GeneratorSystem.
     * </p>
     */
    public class GeneratorSystemIterator implements Iterator<Generator> {
        private Pointer cit;
        private Pointer cend;

        GeneratorSystemIterator() {
            var pgsit = new PointerByReference();
            int result = ppl_new_Generator_System_const_iterator(pgsit);
            if (result < 0)
                throw new PPLError(result);
            cit = pgsit.getValue();
            PPL.cleaner.register(this, new GeneratorSystemIteratorCleaner(cit));
            result = ppl_Generator_System_begin(pplObj, cit);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_new_Generator_System_const_iterator(pgsit);
            if (result < 0)
                throw new PPLError(result);
            cend = pgsit.getValue();
            PPL.cleaner.register(this, new GeneratorSystemIteratorCleaner(cend));
            result = ppl_Generator_System_end(pplObj, cend);
            if (result < 0)
                throw new PPLError(result);
        }

        /**
         * Returns true if the iteration has more elements.
         */
        @Override
        public boolean hasNext() {
            int result = ppl_Generator_System_const_iterator_equal_test(cit, cend);
            if (result < 0)
                throw new PPLError(result);
            return result == 0;
        }

        /**
         * Returns the next element in the iteration. Note that the generators extracted
         * from the iterator are not going to survive operations which manipulate the
         * original ConstraintSystem.
         */
        @Override
        public Generator next() {
            if (!hasNext())
                throw new NoSuchElementException();
            var pc = new PointerByReference();
            int result = ppl_Generator_System_const_iterator_dereference(cit, pc);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_Generator_System_const_iterator_increment(cit);
            if (result < 0)
                throw new PPLError(result);
            return new Generator(pc.getValue());
        }
    }

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new GeneratorSystemCleaner(pplObj));
    }

    /**
     * Returns an empty zero-dimensional GeneratorSystem.
     */
    public GeneratorSystem() {
        var pgs = new PointerByReference();
        int result = ppl_new_Generator_System(pgs);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    /**
     * Returns a new GeneratorSystem containing only a copy of the Constraint c.
     */
    public GeneratorSystem(Generator g) {
        var pgs = new PointerByReference();
        int result = ppl_new_Generator_System_from_Generator(pgs, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    /**
     * Returns a copy of the GeneratorSystem gs.
     */
    public GeneratorSystem(GeneratorSystem gs) {
        var pgs = new PointerByReference();
        int result = ppl_new_Generator_System_from_Generator_System(pgs, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pgs.getValue());
    }

    /**
     * Returns a GeneratorSystem obtained from the specified native object.
     */
    GeneratorSystem(Pointer pplObj) {
        init(pplObj);
    }

    /**
     * Set the value of this GeneratorSystem to a copy of the GeneratorSystem gs.
     */
    public GeneratorSystem assign(GeneratorSystem gs) {
        int result = ppl_assign_Generator_System_from_Generator_System(pplObj, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Returns the space dimension of this GeneratorSystem.
     */
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Generator_System_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    /**
     * Return true if and only if this GeneratorSystem contains no generators.
     */
    public boolean isEmpty() {
        int result = ppl_Generator_System_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Returns true if this GeneratorSystem satisfies all its implementation
     * invariants; returns false and perhaps makes some noise if it is broken.
     * Useful for debugging purposes.
     */
    public boolean isOK() {
        int result = ppl_Generator_System_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Removes all the generators from this GeneratorSystem.
     */
    public GeneratorSystem clear() {
        int result = ppl_Generator_System_clear(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Add the Generator g to this GeneratorSystem.
     */
    public GeneratorSystem add(Generator g) {
        int result = ppl_Generator_System_insert_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Returns an iterator for this GeneratorSystem.
     */
    public Iterator<Generator> iterator() {
        return new GeneratorSystemIterator();
    }

    /**
     * Returns a string representation of this GeneratorSystem.
     */
    @Override
    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Generator_System(pstr, pplObj);
        if (result < 0)
            throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
