package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeTByReference;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A system of generators.
 *
 * <p>
 * A system of generators which is meant to define a non-empty abstract object
 * must include at least one point: the reason is that lines, rays and closure
 * points need a supporting point (lines and rays only specify directions while
 * closure points only specify points in the topological closure of the NNC
 * polyhedron).
 * </p>
 */
public class GeneratorSystem extends GeometricDescriptorsSystem<Generator, GeneratorSystem> {
    /**
     * Enumerates the possible zero-dimensional GeneratorSystem which it is possible
     * to build with the GeneratorSystem constructor.
     */
    public enum ZeroDimGeneratorSystem {
        /** Represents the empty zero-dimensional GeneratorSystem. */
        EMPTY, UNSATISFIABLE
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
     * Note that the generators extracted from the iterator are not going to survive
     * operations which manipulate the original GeneratorSystem.
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

    @Override
    public GeneratorSystem assign(GeneratorSystem gs) {
        int result = ppl_assign_Generator_System_from_Generator_System(pplObj, gs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Generator_System_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    @Override
    public boolean isEmpty() {
        int result = ppl_Generator_System_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean isOK() {
        int result = ppl_Generator_System_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public GeneratorSystem clear() {
        int result = ppl_Generator_System_clear(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public GeneratorSystem add(Generator g) {
        int result = ppl_Generator_System_insert_Generator(pplObj, g.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public Iterator<Generator> iterator() {
        return new GeneratorSystemIterator();
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Generator_System(pstr, pplObj);
    }
}
