package it.unich.jppl;

import com.sun.jna.Pointer;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;

import static it.unich.jppl.nativelib.LibPPL.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ConstraintSystem implements Iterable<Constraint> {

    Pointer obj;

    public enum ZeroDimConstraintSystem {
        EMPTY,
        UNSATISFIABLE
    }

    private static class ConstraintSystemCleaner implements Runnable {
        private Pointer obj;

        ConstraintSystemCleaner(Pointer obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            ppl_delete_Constraint_System(obj);
        }
    }

    public class ConstraintSystemIterator implements Iterator<Constraint> {
        private Pointer cit;
        private Pointer cend;

        ConstraintSystemIterator() {
            var pcit = new PointerByReference();
            ppl_new_Constraint_System_const_iterator(pcit);
            cit = pcit.getValue();
            ppl_Constraint_System_begin(obj, cit);
            ppl_new_Constraint_System_const_iterator(pcit);
            cend = pcit.getValue();
            ppl_Constraint_System_end(obj, cend);
        }

        @Override
        public boolean hasNext() {
            return ppl_Constraint_System_const_iterator_equal_test(cit, cend) == 0;
        }

        @Override
        public Constraint next() {
            if (! hasNext())
                throw new NoSuchElementException();
            var pc = new PointerByReference();
            ppl_Constraint_System_const_iterator_dereference(cit, pc);
            ppl_Constraint_System_const_iterator_increment(cit);
            return new Constraint(pc.getValue());
        }
    }

    private void init(Pointer p) {
        obj = p;
        PPL.cleaner.register(this, new ConstraintSystemCleaner(obj));
    }

    public ConstraintSystem() {
        PointerByReference pcs = new PointerByReference();
        ppl_new_Constraint_System(pcs);
        init(pcs.getValue());
    }

    public ConstraintSystem(ZeroDimConstraintSystem type) {
        PointerByReference pcs = new PointerByReference();
        if (type == ZeroDimConstraintSystem.EMPTY)
            ppl_new_Constraint_System(pcs);
        else
            ppl_new_Constraint_System_zero_dim_empty(pcs);
        init(pcs.getValue());
    }

    public ConstraintSystem(Constraint c) {
        PointerByReference pcs = new PointerByReference();
        ppl_new_Constraint_System_from_Constraint(pcs, c.obj);
        init(pcs.getValue());
    }

    public ConstraintSystem(ConstraintSystem cs) {
        this(cs.obj);
    }

    ConstraintSystem(Pointer cs) {
        PointerByReference pcs = new PointerByReference();
        ppl_new_Constraint_System_from_Constraint_System(pcs, obj);
        init(pcs.getValue());
    }

    public ConstraintSystem assign(ConstraintSystem cs) {
        ppl_assign_Constraint_System_from_Constraint_System(obj, cs.obj);
        return this;
    }

    public long getSpaceDimension() {
        var dref = new DimensionByReference();
        ppl_Constraint_System_space_dimension(obj, dref);
        return dref.getValue().longValue();
    }

    public boolean isEmpty() {
        return ppl_Constraint_System_empty(obj) > 0;
    }

    public boolean hasStrictInequalities() {
        return ppl_Constraint_System_has_strict_inequalities(obj) > 0;
    }

    public boolean isOK() {
        return ppl_Constraint_System_OK(obj) > 0;
    }

    public ConstraintSystem clear() {
        ppl_Constraint_System_clear(obj);
        return this;
    }

    public ConstraintSystem add(Constraint c) {
        ppl_Constraint_System_insert_Constraint(obj, c.obj);
        return this;
    }

    public Iterator<Constraint> iterator() {
        return new ConstraintSystemIterator();
    }

    public String toString() {
        PointerByReference pstr = new PointerByReference();
        ppl_io_asprint_Constraint_System(pstr,obj);
        Pointer p = pstr.getValue();
        String s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}

