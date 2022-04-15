package it.unich.jppl.nativelib;

import com.sun.jna.Callback;

/**
 * Interface of a callback for handling errors generated during native excution.
 */
@FunctionalInterface
public interface PPLErrorHandler extends Callback {
    /**
     * The callback method. Once the callback is registered with
     * {@link LibPPL#ppl_set_error_handler}, this method is called by PPL functions
     * whenever an error occurs.
     */
    void callback(int code, String description);
}