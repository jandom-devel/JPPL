package it.unich.jppl;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * Abstract parent for all Java classes dealing with native PPL objects.
 */
public abstract class AbstractPPLObject<T extends AbstractPPLObject<T>> implements PPLObject<T> {

    /**
     * Pointer to the native PPL object.
     */
    protected Pointer pplObj;

    /**
     * Assigns to this object a copy of the object {@code obj}.
     */
    abstract T assign(T obj);

    /**
     * Returns true if the native PPL object satisfies all its implementation
     * invariants. Returns false and perhaps makes some noise if it is broken.
     * Useful for debugging purposes.
     */
    abstract boolean isOK();

    /**
     * Returns in {@code pstr} a pointer to the string representation of this object.
     *
     * <p>
     * The method provided by concrete subclasses should call the correct C function
     * from the family {@code ppl_io_asprint_}.
     * </p>
     */
    protected abstract int toStringByReference(PointerByReference pstr);

    @Override
    public abstract T clone();

    @Override
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
            PPLRuntimeException.checkError(result);
        var p = pstr.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

}
