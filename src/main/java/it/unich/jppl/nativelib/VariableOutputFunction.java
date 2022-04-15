package it.unich.jppl.nativelib;

import com.sun.jna.Callback;

/**
 * Interface of a callback for obtaining the string representation of a variable.
 */
@FunctionalInterface
public interface VariableOutputFunction extends Callback {
    /**
     * The callback method. Once the callback is registered with
     * {@link LibPPL#ppl_io_set_variable_output_function}, this method is called by
     * PPL output functions whenever they need a string representation for a
     * variable \(x_i\).
     */
    String callback(long i);
}
