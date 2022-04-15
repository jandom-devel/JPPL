package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.nativelib.SizeTByReference;

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
public class GeneratorSystem extends AbstractPPLObject<GeneratorSystem>
        implements GeometricDescriptorsSystem<Generator, GeneratorSystem> {

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
     * An iterator over a generator system.
     *
     * <p>
     * Note that the generators extracted from the iterator are not going to survive
     * operations which manipulate the original generator system.
     * </p>
     */
    public class GeneratorSystemIterator implements Iterator<Generator> {
        private Pointer cit;
        private Pointer cend;

        GeneratorSystemIterator() {
            var pgsit = new PointerByReference();
            int result = ppl_new_Generator_System_const_iterator(pgsit);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            cit = pgsit.getValue();
            PPL.cleaner.register(this, new GeneratorSystemIteratorCleaner(cit));
            result = ppl_Generator_System_begin(pplObj, cit);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            result = ppl_new_Generator_System_const_iterator(pgsit);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            cend = pgsit.getValue();
            PPL.cleaner.register(this, new GeneratorSystemIteratorCleaner(cend));
            result = ppl_Generator_System_end(pplObj, cend);
            if (result < 0)
                PPLRuntimeException.checkError(result);
        }

        /**
         * Returns true if the iteration has more elements.
         */
        @Override
        public boolean hasNext() {
            int result = ppl_Generator_System_const_iterator_equal_test(cit, cend);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            return result == 0;
        }

        /**
         * Returns the next element in the iteration. Note that the generators extracted
         * from the iterator are not going to survive operations which manipulate the
         * original generator system.
         */
        @Override
        public Generator next() {
            if (!hasNext())
                throw new NoSuchElementException();
            var pc = new PointerByReference();
            int result = ppl_Generator_System_const_iterator_dereference(cit, pc);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            result = ppl_Generator_System_const_iterator_increment(cit);
            if (result < 0)
                PPLRuntimeException.checkError(result);
            return new Generator(pc.getValue(), false);
        }
    }

    /**
     * Creates a generator system obtained from the specified native object.
     *
     * @param registerCleaner if true, the native object is registered for deletion
     *                        when the generator system is garbage collected.
     */
    GeneratorSystem(Pointer p, boolean registerCleaner) {
        pplObj = p;
        if (registerCleaner)
            PPL.cleaner.register(this, new GeneratorSystemCleaner(pplObj));
    }

    /**
     * Creates a generator system obtained from the specified native object. It is
     * equivalent to {@code GeneratorSystem(p, true)}.
     */
    private GeneratorSystem(Pointer p) {
        this(p, false);
    }

    /**
     * Creates and returns an empty zero-dimensional generator system.
     */
    public static GeneratorSystem empty() {
        var pgs = new PointerByReference();
        int result = ppl_new_Generator_System(pgs);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new GeneratorSystem(pgs.getValue());
    }

    /**
     * Creates and returns a generator system containing only a copy of the
     * generator {@code g}.
     */
    public static GeneratorSystem of(Generator g) {
        var pgs = new PointerByReference();
        int result = ppl_new_Generator_System_from_Generator(pgs, g.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new GeneratorSystem(pgs.getValue());
    }

    @Override
    public GeneratorSystem clone() {
        var pgs = new PointerByReference();
        int result = ppl_new_Generator_System_from_Generator_System(pgs, pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return new GeneratorSystem(pgs.getValue());
    }

    @Override
    public GeneratorSystem assign(GeneratorSystem gs) {
        int result = ppl_assign_Generator_System_from_Generator_System(pplObj, gs.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Generator_System_space_dimension(pplObj, m);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return m.getValue().longValue();
    }

    @Override
    public boolean isEmpty() {
        int result = ppl_Generator_System_empty(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public boolean isOK() {
        int result = ppl_Generator_System_OK(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return result > 0;
    }

    @Override
    public GeneratorSystem clear() {
        int result = ppl_Generator_System_clear(pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
        return this;
    }

    @Override
    public GeneratorSystem add(Generator g) {
        int result = ppl_Generator_System_insert_Generator(pplObj, g.pplObj);
        if (result < 0)
            PPLRuntimeException.checkError(result);
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
