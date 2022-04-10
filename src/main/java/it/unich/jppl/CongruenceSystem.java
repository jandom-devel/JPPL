package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeTByReference;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class CongruenceSystem implements Iterable<Congruence> {
    Pointer pplObj;

    public enum ZeroDimCongruenceSystem {
        EMPTY, UNSATISFIABLE
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

        @Override
        public boolean hasNext() {
            int result = ppl_Congruence_System_const_iterator_equal_test(cit, cend);
            if (result < 0)
                throw new PPLError(result);
            return result == 0;
        }

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

    public CongruenceSystem() {
        this(ZeroDimCongruenceSystem.EMPTY);
    }

    public CongruenceSystem(ZeroDimCongruenceSystem type) {
        var pcs = new PointerByReference();
        int result = (type == ZeroDimCongruenceSystem.EMPTY) ? ppl_new_Congruence_System(pcs)
                : ppl_new_Congruence_System_zero_dim_empty(pcs);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    public CongruenceSystem(Congruence c) {
        var pcs = new PointerByReference();
        int result = ppl_new_Congruence_System_from_Congruence(pcs, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    public CongruenceSystem(CongruenceSystem cs) {
        var pcs = new PointerByReference();
        int result = ppl_new_Congruence_System_from_Congruence_System(pcs, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    CongruenceSystem(Pointer pplObj) {
        init(pplObj);
    }

    public CongruenceSystem assign(CongruenceSystem cs) {
        int result = ppl_assign_Congruence_System_from_Congruence_System(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Congruence_System_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    public boolean isEmpty() {
        int result = ppl_Congruence_System_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isOK() {
        int result = ppl_Congruence_System_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public CongruenceSystem clear() {
        int result = ppl_Congruence_System_clear(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public CongruenceSystem add(Congruence c) {
        int result = ppl_Congruence_System_insert_Congruence(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public Iterator<Congruence> iterator() {
        return new CongruenceSystemIterator();
    }

    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Congruence_System(pstr, pplObj);
        if (result < 0)
            throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
