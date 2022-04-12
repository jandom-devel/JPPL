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

    /** Creates an uninitialized PPLObject */
    protected PPLObject() { }

    /** Pointer to the PPL native object. */
    public Pointer pplObj;

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
     * This method should be provided by concrete subclasses. It must call the
     * correct C function from the family {@code ppl_io_asprint_}.
     */
    protected abstract int toStringByReference(PointerByReference pstr);

    /**
     * Returns the pointer to the PPL native object. Useless if you do not plan to
     * interface with some native library.
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
