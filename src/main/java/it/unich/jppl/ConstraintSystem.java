package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeTByReference;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A system of constraints.
 * 
 * <p>
 * An object of the class ConstraintSystem is a system of constraints, i.e., a
 * multiset of objects of the class Constraint. When inserting constraints in a
 * system, space dimensions are automatically adjusted so that all the
 * constraints in the system are defined on the same vector space.
 * </p>
 */
public class ConstraintSystem implements Iterable<Constraint> {
    Pointer pplObj;

    /**
     * Enumerates the possible zero-dimensional ConstraintSystem which it is
     * possible to build with the ConstraintSystem constructor.
     */
    public enum ZeroDimConstraintSystem {
        /** Represents the empty zero-dimensional ConstraintSystem. */
        EMPTY,
        /**
         * Represents the zero-dimensional ConstraintSystem that contains only the
         * falsity zero-dimensionality congruence.
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
     * An iterator over a ConstraintSystem.
     *
     * <p>
     * Note that the constraints extracted from the iterator are not going to
     * survive operations which manipulate the original CongruenceSystem.
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
         * manipulate the original ConstraintSystem.
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
     * Returns an empty zero-dimensional ConstraintSystem.
     */
    public ConstraintSystem() {
        this(ZeroDimConstraintSystem.EMPTY);
    }

    /**
     * Returns a zero-dimensional ConstraintSystem according of the specified type.
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
     * Returns a new ConstraintSystem containing only a copy of the Constraint c.
     */
    public ConstraintSystem(Constraint c) {
        var pcs = new PointerByReference();
        int result = ppl_new_Constraint_System_from_Constraint(pcs, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    /**
     * Returns a copy of the ConstraintSystem cs.
     */
    public ConstraintSystem(ConstraintSystem cs) {
        var pcs = new PointerByReference();
        int result = ppl_new_Constraint_System_from_Constraint_System(pcs, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        init(pcs.getValue());
    }

    /**
     * Returns a ConstraintSystem obtained from the specified native object.
     */
    ConstraintSystem(Pointer pplObj) {
        init(pplObj);
    }

    /**
     * Set the value of this ConstraintSystem to a copy of the ConstraintSystem cs.
     */
    public ConstraintSystem assign(ConstraintSystem cs) {
        int result = ppl_assign_Constraint_System_from_Constraint_System(pplObj, cs.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Returns the space dimension of this ConstraintSystem.
     */
    public long getSpaceDimension() {
        var m = new SizeTByReference();
        int result = ppl_Constraint_System_space_dimension(pplObj, m);
        if (result < 0)
            throw new PPLError(result);
        return m.getValue().longValue();
    }

    /**
     * Return true if and only if this ConstraintSystem contains no (non-trivial)
     * constraints.
     */
    public boolean isEmpty() {
        int result = ppl_Constraint_System_empty(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Returns true if and only if this ConstraintSystem contains any (non-trivial)
     * strict inequality.
     */
    public boolean hasStrictInequalities() {
        int result = ppl_Constraint_System_has_strict_inequalities(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Returns true if this ConstraintSystem satisfies all its implementation
     * invariants; returns false and perhaps makes some noise if it is broken.
     * Useful for debugging purposes.
     */
    public boolean isOK() {
        int result = ppl_Constraint_System_OK(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return result > 0;
    }

    /**
     * Removes all the constraints from this ConstraintSystem.
     */
    public ConstraintSystem clear() {
        int result = ppl_Constraint_System_clear(pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Add the Constraint c to this ConstraintSystem.
     */
    public ConstraintSystem add(Constraint c) {
        int result = ppl_Constraint_System_insert_Constraint(pplObj, c.pplObj);
        if (result < 0)
            throw new PPLError(result);
        return this;
    }

    /**
     * Returns an iterator for this ConstraintSystem.
     */
    public Iterator<Constraint> iterator() {
        return new ConstraintSystemIterator();
    }

    /**
     * Returns a string representation of this ConstraintSystem.
     */
    @Override
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
