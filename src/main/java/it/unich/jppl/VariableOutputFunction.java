package it.unich.jppl;

import com.sun.jna.Callback;

public interface VariableOutputFunction extends Callback {
    public String callback(long var);
}
