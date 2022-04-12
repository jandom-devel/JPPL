package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeTByReference;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A system of linear congruences.
 */
public class CongruenceSystem extends GeometricDescriptorsSystem<Congruence, CongruenceSystem> {

    /**
     * Enumerates the zero-dimensional congruence systems which it is possible to
     * build with the CongruenceSystem constructor.
     */
    public enum ZeroDimCongruenceSystem {
        /**
         * Represents the empty zero-dimensional congruence system.
         */
        EMPTY,
        /**
         * Represents the zero-dimensional congruence system that contains only the
         * falsity zero-dimensional congruence.
         *
         * @see Congruence#Congruence(ZeroDimCongruence type)
         */
        UNSATISFIABLE
    }

    private static class CongruenceSystemCleaner implements Runnable {
        private Pointer pplObj;

        CongruenceSystemCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Congruence_System(pplObj);
        }
    }

    private static class CongruenceSystemIteratorCleaner implements Runnable {
        private Pointer pplObj;

        CongruenceSystemIteratorCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Congruence_System_const_iterator(pplObj);
        }
    }

    /**
     * An iterator over a congruence system.
     *
     * <p>
     * Note that the congruences extracted from the iterator are not going to
     * survive operations which manipulate the original congruence system.
     * </p>
     */
    public class CongruenceSystemIterator implements Iterator<Congruence> {
        private Pointer cit;
        private Pointer cend;

        CongruenceSystemIterator() {
            var pcsit = new PointerByReference();
            int result = ppl_new_Congruence_System_const_iterator(pcsit);
            if (result < 0)
                throw new PPLError(result);
            cit = pcsit.getValue();
            PPL.cleaner.register(this, new CongruenceSystemIteratorCleaner(cit));
            result = ppl_Congruence_System_begin(pplObj, cit);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_new_Congruence_System_const_iterator(pcsit);
            if (result < 0)
                throw new PPLError(result);
            cend = pcsit.getValue();
            PPL.cleaner.register(this, new CongruenceSystemIteratorCleaner(cend));
            result = ppl_Congruence_System_end(pplObj, cend);
            if (result < 0)
                throw new PPLError(result);
        }

        /**
         * Returns true if the iteration has more elements.
         */
        @Override
        public boolean hasNext() {
            int result = ppl_Congruence_System_const_iterator_equal_test(cit, cend);
            if (result < 0)
                throw new PPLError(result);
            return result == 0;
        }

        /**
         * Returns the next element in the iteration. Note that the congruence extracted
         * from the iterator are not going to survive operations which manipulate the
         * original congruence system.
         */
        @Override
        public Congruence next() {
            if (!hasNext())
                throw new NoSuchElementException();
            var pc = new PointerByReference();
            int result = ppl_Congruence_System_const_iterator_dereference(cit, pc);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_Congruence_System_const_iterator_increment(cit);
            if (result < 0)
                throw new PPLError(result);
            return new Congruence(pc.getValue());
        }
    }

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new CongruenceSystemCleaner(pplObj));
    }

    /**
     * Creates an empty zero-dimensional congruence system.
     */
    public CongruenceSystem() {
        this(ZeroDimCongruenceSystem.EMPTY);
    }

    /**
     * Creates a zero-dimensional congruence system of the specified type.
     */
    public CongruenceSystem(ZeroDimCongruenceSystem type) {
        var pcs = new PointerByReference();
        int result = (type == ZeroDimCongruenceSystem.EMPTY) ? ppl_new_Congruence_System(pcs)
                : ppl_new_Congruence_System_zero_dim_empty(pcs);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    /**
     * Create a congruence system containing only a copy of the congruence c.
     */
    public CongruenceSystem(Congruence c) {
        var pcs = new PointerByReference();
        int result = ppl_new_Congruence_System_from_Congruence(pcs, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    /**
     * Create a copy of the congruence system cs.
     */
    public CongruenceSystem(CongruenceSystem cs) {
        var pcs = new PointerByReference();
        int result = ppl_new_Congruence_System_from_Congruence_System(pcs, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    /**
     * Create a congruence system from a native object.
     */
    CongruenceSystem(Pointer pplObj) {
        init(pplObj);
    }

    @Override
    CongruenceSystem assign(CongruenceSystem cs) {
        int result = ppl_assign_Congruence_System_from_Congruence_System(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Congruence_System_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    @Override
    public boolean isEmpty() {
        int result = ppl_Congruence_System_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    boolean isOK() {
        int result = ppl_Congruence_System_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public CongruenceSystem clear() {
        int result = ppl_Congruence_System_clear(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public CongruenceSystem add(Congruence c) {
        int result = ppl_Congruence_System_insert_Congruence(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public Iterator<Congruence> iterator() {
        return new CongruenceSystemIterator();
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Congruence_System(pstr, pplObj);
    }

}
