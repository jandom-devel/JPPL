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
public class CongruenceSystem extends AbstractPPLObject<CongruenceSystem>
        implements GeometricDescriptorsSystem<Congruence, CongruenceSystem> {

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
            return new Congruence(pc.getValue(), false);
        }
    }

    /**
     * Creates a congruence system from a native object.
     *
     * @param registerCleaner if true, the native object is registered for deletion
     *                        when the congruence system is garbage collected.
     */
    CongruenceSystem(Pointer p, boolean registerCleaner) {
        pplObj = p;
        if (registerCleaner)
            PPL.cleaner.register(this, new CongruenceSystemCleaner(pplObj));
    }

    /**
     * Creates a congruence system from a native object. It is equivalent to
     * {@code CongruenceSystem(p, true}
     */
    private CongruenceSystem(Pointer p) {
        this(p, false);
    }

    /**
     * Creates and returns an empty congruence system.
     */
    public static CongruenceSystem empty() {
        var pcs = new PointerByReference();
        int result = ppl_new_Congruence_System(pcs);
        if (result < 0)
            throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue());
    }

    /**
     * Creates and returns a congruence system that contains only the false
     * zero-dimensional congruence.
     *
     * @see Congruence#zeroDimFalse
     */
    public CongruenceSystem zeroDimFalse() {
        var pcs = new PointerByReference();
        int result = ppl_new_Congruence_System_zero_dim_empty(pcs);
        if (result < 0)
            throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue());
    }

    /**
     * Create and returns a congruence system containing only a copy of the
     * congruence c.
     */
    public static CongruenceSystem of(Congruence c) {
        var pcs = new PointerByReference();
        int result = ppl_new_Congruence_System_from_Congruence(pcs, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue());
    }

    @Override
    public CongruenceSystem clone() {
        var pcs = new PointerByReference();
        int result = ppl_new_Congruence_System_from_Congruence_System(pcs, pplObj);
        if (result < 0)
            throw new PPLError(result);
        return new CongruenceSystem(pcs.getValue());
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
