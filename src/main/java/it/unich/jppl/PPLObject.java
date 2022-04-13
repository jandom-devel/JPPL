
package it.unich.jppl;

import com.sun.jna.Pointer;

/**
 * Interface implemented by all Java classes which deals with native PPL
 * objects.
 */
public interface PPLObject<T extends PPLObject<T>> extends Cloneable {

    /**
     * Create and returns a copy of this object.
     */
    T clone();

    /**
     * Returns the pointer to the PPL native object. Useless you plan to interface
     * JPPL with some native code, you do not need this method.
     */
    Pointer getNative();

}
