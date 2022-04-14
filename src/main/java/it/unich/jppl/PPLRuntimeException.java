package it.unich.jppl;

import it.unich.jppl.PPL.JPPLErrorHandler;

/**
 * Exception which encapsulates a native PPL error.
 */
public class PPLRuntimeException extends RuntimeException {

    public static final int OUT_OF_MEMORY = -2;
    public static final int INVALID_ARGUMENT = -3;
    public static final int DOMAIN_ERROR = -4;
    public static final int LENGTH_ERROR = -5;
    public static final int ARITHMETIC_OVERFLOW = -6;
    public static final int STDIO_ERROR = -7;
    public static final int INTERNAL_ERROR = -8;
    public static final int UNKNOWN_STANDARD_EXCEPTION = -9;
    public static final int UNEXPECTED_ERROR = -10;
    public static final int TIMEOUT_EXCEPTION = -11;
    public static final int LOGIC_ERROR = -12;

    private static final String[] ERROR_STRINGS = { "", "", "OUT OF MEMORY", "INVALID ARGUMENT", "DOMAIN_ERROR",
            "LENGTH ERROR", "ARITHMETIC OVERFLOW", "STDIO ERROR", "INTERNAL ERROR", "UNKNOWNS STANDARD EXCEPTION",
            "UNEXPECTED ERRRO", "TIMEOUT EXCEPTION", "LOGIC ERROR" };

    private final int code;

    private PPLRuntimeException(int code, String description) {
        super(description);
        this.code = code;
    }

    static void checkError(int code) {
        if (code >= 0)
            return;
        if (code != JPPLErrorHandler.code) {
            var msg = "The error code passed to the checkError method (" + code + ") does not corresponds to the one "
                    + "captured by the error handler (" + JPPLErrorHandler.code + ")";
            JPPLErrorHandler.reset();
            throw new IllegalStateException(msg);
        }
        var msg = (code > -2 || code < -12) ?
            "UNKNOWN PPL ERROR (" + code + "): " : ERROR_STRINGS[-code];
        msg += ": ";
        msg += JPPLErrorHandler.description;
        JPPLErrorHandler.reset();
        throw new PPLRuntimeException(code, msg);
    }

    /**
     * Returns the PPL error code which generated this exception.
     */
    public int getCode() {
        return code;
    }
}
