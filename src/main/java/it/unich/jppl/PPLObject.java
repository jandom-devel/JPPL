
package it.unich.jppl;

import com.sun.jna.Pointer;

/**
 * A Java object corresponding to a native PPL object.
 */
public interface PPLObject<T extends PPLObject<T>> extends Cloneable {

    /**
     * Create and returns a copy of this object. It also copies the native PPL
     * counterpart.
     */
    T clone();

    /**
     * Returns the pointer to the PPL native object. Useless you plan to interface
     * JPPL with some native code, you do not need the result of this method.
     */
    Pointer getNative();

}
