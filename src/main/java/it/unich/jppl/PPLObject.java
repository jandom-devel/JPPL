package it.unich.jppl;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * Abstract parent for all Java classes that corresponds to C native PPL
 * objects.
 *
 * Almost all methods throw PPLError when the underlying PPL library generates
 * an error.
 */
public abstract class PPLObject<T extends PPLObject<T>> implements Cloneable {

    /**
     * Pointer to the PPL native object.
     */
    protected Pointer pplObj;

    /**
     * Assign to this object a copy of the object obj.
     */
    abstract T assign(T obj);

    /**
     * Returns true if the native PPL object satisfies all its implementation
     * invariants; returns false and perhaps makes some noise if it is broken.
     * Useful for debugging purposes.
     */
    abstract boolean isOK();

    /**
     * Returns in pstr the string representation of this object.
     *
     * <p>
     * The method provided by concrete subclasses should call the correct C function
     * from the family {@code ppl_io_asprint_}.
     * </p>
     */
    protected abstract int toStringByReference(PointerByReference pstr);

    /**
     * Create and returns a copy of this object.
     */
    public abstract T clone();

    /**
     * Returns the pointer to the PPL native object. Useless you plan to interface
     * JPPL with some native code, you do not need this method.
     */
    public Pointer getNative() {
        return pplObj;
    }

    /**
     * Returns a string representation of this object.
     */
    @Override
    public String toString() {
        var pstr = new PointerByReference();
        int result = toStringByReference(pstr);
        if (result < 0)
            throw new PPLError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

}
