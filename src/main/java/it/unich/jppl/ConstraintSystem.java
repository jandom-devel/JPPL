package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.nativelib.LibPPL.DimensionByReference;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class ConstraintSystem implements Iterable<Constraint> {
    Pointer pplObj;

    public enum ZeroDimConstraintSystem {
        EMPTY, UNSATISFIABLE
    }

    private static class ConstraintSystemCleaner implements Runnable {
        private Pointer pplObj;

        ConstraintSystemCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Constraint_System(pplObj);
        }
    }

    private static class ConstraintSystemIteratorCleaner implements Runnable {
        private Pointer pplObj;

        ConstraintSystemIteratorCleaner(Pointer obj) {
            this.pplObj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Constraint_System_const_iterator(pplObj);
        }
    }

    public class ConstraintSystemIterator implements Iterator<Constraint> {
        private Pointer cit;
        private Pointer cend;

        ConstraintSystemIterator() {
            var pcsit = new PointerByReference();
            int result = ppl_new_Constraint_System_const_iterator(pcsit);
            if (result < 0)
                throw new PPLError(result);
            cit = pcsit.getValue();
            PPL.cleaner.register(this, new ConstraintSystemIteratorCleaner(cit));
            result = ppl_Constraint_System_begin(pplObj, cit);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_new_Constraint_System_const_iterator(pcsit);
            if (result < 0)
                throw new PPLError(result);
            cend = pcsit.getValue();
            PPL.cleaner.register(this, new ConstraintSystemIteratorCleaner(cend));
            result = ppl_Constraint_System_end(pplObj, cend);
            if (result < 0)
                throw new PPLError(result);
        }

        @Override
        public boolean hasNext() {
            int result = ppl_Constraint_System_const_iterator_equal_test(cit, cend);
            if (result < 0)
                throw new PPLError(result);
            return result == 0;
        }

        @Override
        public Constraint next() {
            if (!hasNext())
                throw new NoSuchElementException();
            var pc = new PointerByReference();
            int result = ppl_Constraint_System_const_iterator_dereference(cit, pc);
            if (result < 0)
                throw new PPLError(result);
            result = ppl_Constraint_System_const_iterator_increment(cit);
            if (result < 0)
                throw new PPLError(result);
            return new Constraint(pc.getValue());
        }
    }

    private void init(Pointer p) {
        pplObj = p;
        PPL.cleaner.register(this, new ConstraintSystemCleaner(pplObj));
    }

    public ConstraintSystem() {
        this(ZeroDimConstraintSystem.EMPTY);
    }

    public ConstraintSystem(ZeroDimConstraintSystem type) {
        var pcs = new PointerByReference();
        int result = (type == ZeroDimConstraintSystem.EMPTY) ? ppl_new_Constraint_System(pcs)
                : ppl_new_Constraint_System_zero_dim_empty(pcs);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    public ConstraintSystem(Constraint c) {
        var pcs = new PointerByReference();
        int result = ppl_new_Constraint_System_from_Constraint(pcs, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    public ConstraintSystem(ConstraintSystem cs) {
        var pcs = new PointerByReference();
        int result = ppl_new_Constraint_System_from_Constraint_System(pcs, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    ConstraintSystem(Pointer pplObj) {
        init(pplObj);
    }

    public ConstraintSystem assign(ConstraintSystem cs) {
        int result = ppl_assign_Constraint_System_from_Constraint_System(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public long getSpaceDimension() {
        var m = new DimensionByReference();
        int result = ppl_Constraint_System_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    public boolean isEmpty() {
        int result = ppl_Constraint_System_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean hasStrictInequalities() {
        int result = ppl_Constraint_System_has_strict_inequalities(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public boolean isOK() {
        int result = ppl_Constraint_System_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    public ConstraintSystem clear() {
        int result = ppl_Constraint_System_clear(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public ConstraintSystem add(Constraint c) {
        int result = ppl_Constraint_System_insert_Constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    public Iterator<Constraint> iterator() {
        return new ConstraintSystemIterator();
    }

    public String toString() {
        var pstr = new PointerByReference();
        int result = ppl_io_asprint_Constraint_System(pstr, pplObj);
        if (result < 0)
            throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
