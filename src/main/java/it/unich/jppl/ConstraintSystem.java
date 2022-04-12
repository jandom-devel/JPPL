package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeTByReference;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A system of constraints.
 */
public class ConstraintSystem extends GeometricDescriptorsSystem<Constraint, ConstraintSystem> {

    /**
     * Enumerates the zero-dimensional constraint systems which it is possible to
     * build with the ConstraintSystem constructor.
     */
    public enum ZeroDimConstraintSystem {
        /**
         * Represents the empty zero-dimensional constraint system.
         */
        EMPTY,
        /**
         * Represents the zero-dimensional constraint system that contains only the
         * falsity zero-dimensionality constraint.
         *
         * @see Constraint#Constraint(ZeroDimConstraint type)
         */
        UNSATISFIABLE
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

    /**
     * An iterator over a constraint system.
     *
     * <p>
     * Note that the constraints extracted from the iterator are not going to
     * survive operations which manipulate the original constraint system.
     * </p>
     */
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

        /**
         * Returns true if the iteration has more elements.
         */
        @Override
        public boolean hasNext() {
            int result = ppl_Constraint_System_const_iterator_equal_test(cit, cend);
            if (result < 0)
                throw new PPLError(result);
            return result == 0;
        }

        /**
         * Returns the next element in the iteration. Note that the constraints
         * extracted from the iterator are not going to survive operations which
         * manipulate the original constraint system.
         */
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

    /**
     * Creates an empty zero-dimensional constraint system.
     */
    public ConstraintSystem() {
        this(ZeroDimConstraintSystem.EMPTY);
    }

    /**
     * Creates a zero-dimensional constraint system of the specified type.
     */
    public ConstraintSystem(ZeroDimConstraintSystem type) {
        var pcs = new PointerByReference();
        int result = (type == ZeroDimConstraintSystem.EMPTY) ? ppl_new_Constraint_System(pcs)
                : ppl_new_Constraint_System_zero_dim_empty(pcs);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    /**
     * Create a constraint system containing only a copy of the constraint c.
     */
    public ConstraintSystem(Constraint c) {
        var pcs = new PointerByReference();
        int result = ppl_new_Constraint_System_from_Constraint(pcs, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    /**
     * Creates a copy of the constraint system cs.
     */
    public ConstraintSystem(ConstraintSystem cs) {
        var pcs = new PointerByReference();
        int result = ppl_new_Constraint_System_from_Constraint_System(pcs, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    /**
     * Creates a constraint system from a native object.
     */
    ConstraintSystem(Pointer pplObj) {
        init(pplObj);
    }

    @Override
    public ConstraintSystem assign(ConstraintSystem cs) {
        int result = ppl_assign_Constraint_System_from_Constraint_System(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Constraint_System_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    @Override
    public boolean isEmpty() {
        int result = ppl_Constraint_System_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public boolean isOK() {
        int result = ppl_Constraint_System_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    @Override
    public ConstraintSystem clear() {
        int result = ppl_Constraint_System_clear(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public ConstraintSystem add(Constraint c) {
        int result = ppl_Constraint_System_insert_Constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    @Override
    public Iterator<Constraint> iterator() {
        return new ConstraintSystemIterator();
    }

    @Override
    protected int toStringByReference(PointerByReference pstr) {
        return ppl_io_asprint_Constraint_System(pstr, pplObj);
    }

    /**
     * Returns true if and only if this constraint system contains any (non-trivial)
     * strict inequality.
     */
    public boolean hasStrictInequalities() {
        int result = ppl_Constraint_System_has_strict_inequalities(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }
}
