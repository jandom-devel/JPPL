package it.unich.jppl;

public class PPLError extends Error {
    public static final int PPL_ERROR_OUT_OF_MEMORY = -2;
    public static final int PPL_ERROR_INVALID_ARGUMENT = -3;
    public static final int PPL_ERROR_DOMAIN_ERROR = -4;
    public static final int PPL_ERROR_LENGTH_ERROR = -5;
    public static final int PPL_ARITHMETIC_OVERFLOW = -6;
    public static final int PPL_STDIO_ERROR = -7;
    public static final int PPL_ERROR_INTERNAL_ERROR = -8;
    public static final int PPL_ERROR_UNKNOWN_STANDARD_EXCEPTION = -9;
    public static final int PPL_ERROR_UNEXPECTED_ERROR = -10;
    public static final int PPL_TIMEOUT_EXCEPTION = -11;
    public static final int PPL_ERROR_LOGIC_ERROR = -12;

    private final int pplError;

    PPLError(int pplError) {
        super("PPL error: " + pplError);
        this.pplError = pplError;
    }

    public int getPPLError() {
        return pplError;
    }
}
