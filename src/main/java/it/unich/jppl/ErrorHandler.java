package it.unich.jppl;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface ErrorHandler extends Callback {
    public Pointer callback(int pplError, String description);
}
