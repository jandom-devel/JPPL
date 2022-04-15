package it.unich.jppl.nativelib;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

/**
 * A structure containing a {@link VariableOutputFunction} callback. This is
 * used to pass the callback by reference to native code.
 */
@FieldOrder({ "vof" })

public class VariableOutputFunctionByReference extends Structure {

    /**
     * Creates an instance of this structure.
     */
    public VariableOutputFunctionByReference() {
        super();
    }

    /**
     * The callback which is the only element of this structure.
     */
    public VariableOutputFunction vof;

}
