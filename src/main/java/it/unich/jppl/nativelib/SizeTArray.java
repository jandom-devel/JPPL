package it.unich.jppl.nativelib;

import com.sun.jna.Native;
import com.sun.jna.Memory;

/**
 * A native array of {@code size_t} elements.
 */
public class SizeTArray extends Memory {
    /**
     * Creates a native array from the Java array {@code ls}.
     */
    public SizeTArray(long[] ls) {
        super(ls.length * Native.SIZE_T_SIZE);
        for (int i = 0; i < ls.length; i++)
            if (Native.SIZE_T_SIZE == 8)
                setLong(8 * i, ls[i]);
            else
                setInt(4 * i, (int) ls[i]);
    }
}
