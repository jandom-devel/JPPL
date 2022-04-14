package it.unich.jppl;

import static it.unich.jppl.LibPPL.*;

import it.unich.jppl.LibPPL.SizeT;

import java.lang.ref.Cleaner;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * This class is a collection of several utility methods.
 */
public class PPL {

    /**
     * Default constructor. It is private, since there is never the need to
     * instantiate the class.
     */
    private PPL() {
    }

    /**
     * The {@link Cleaner} used to deallocate the memory of native PPL object during
     * garbage collection.
     */
    static final Cleaner cleaner = Cleaner.create();

    /**
     * Behavior of bounded integer types.
     */
    public static enum BoundedIntegerTypeOverflow {
        /**
         * On overflow, wrapping takes place. This means that, for a \(w\)-bit bounded
         * integer, the computation happens modulo \(2^w\).
         */
        WRAPS,
        /**
         * On overflow, the result is undefined. This simply means that the result of
         * the operation resulting in an overflow can take any value.
         */
        UNDEFINED,
        /**
         * Overflow is impossible. This is for the analysis of languages where overflow
         * is trapped before it affects the state, for which, thus, any indication that
         * an overflow may have affected the state is necessarily due to the imprecision
         * of the analysis.
         */
        IMPOSSIBLE
    }

    /** Representation of bounded integer types. */
    public static enum BoundedIntegerTypeRepresentation {
        /** Unsigned binary. */
        UNSIGNED,
        /**
         * Signed binary where negative values are represented by the two's complement
         * of the absolute value.
         */
        SIGNED_2_COMPLEMENT
    };

    /** Widths of bounded integer types. */
    public static enum BoundedIntegerTypeWidth {
        PPL_BITS_8(8), PPL_BITS_16(16), PPL_BITS_32(32), PPL_BITS_64(64), PPL_BITS_128(128);

        int pplValue;

        BoundedIntegerTypeWidth(int pplValue) {
            this.pplValue = pplValue;
        }
    }

    /**
     * An error handler which saves the code and description of natve PPL error. Uses
     * the Singleton design pattern.
     */
    static class JPPLErrorHandler implements Callback {
        private static JPPLErrorHandler instance = null;
        static int code;
        static String description;

        private JPPLErrorHandler() {
        }

        static JPPLErrorHandler getInstance() {
            // Crea l'oggetto solo se NON esiste:
            if (instance == null) {
                instance = new JPPLErrorHandler();
            }
            return instance;
        }

        static void reset() {
            code = 0;
            description = "";
        }

        public void callback(int code, String description) {
            JPPLErrorHandler.code = code;
            JPPLErrorHandler.description = description;
        }
    }

    /** Version of JPPL. */
    public static final String JPPL_VERSION = "0.2-SNAPSHOT";

    static {
        pplInitialize();
    }

    /**
     * Initializes the Parma Polyhedra Library. This mathods must be called before
     * operating with the PPL. The static initializer of the JPPL class
     * automatically calls this method.
     */
    public static void pplInitialize() {
        // according to documentation it seems ppl_initialize should generate an error if
        // called multiple times,  but it never happended to me
        ppl_set_error_handler(JPPLErrorHandler.getInstance());
        int result = ppl_initialize();
        if (result < 0)
            throw new PPLError(result);
    }

    /**
     * Finalizes the Parma Polyhedra Library. After this method is called, JPPL
     * should not be used anymore.
     */
    public static void pplFinalize() {
        // according to documentation it seems ppl_finalize should generate an error, if
        // called multiple times,  but it never happended to me
        int result = ppl_finalize();
        if (result < 0)
            throw new PPLError(result);
    }

    /**
     * Sets the FPU rounding mode so that the PPL abstractions based on floating
     * point numbers work correctly.
     *
     * This is performed automatically at initialization-time. Calling this method
     * is needed only if {@link #restorePrePPLRounding} has been previously called.
     */
    public static void setRoundingForPPL() {
        int result = ppl_set_rounding_for_PPL();
        if (result < 0)
            throw new PPLError(result);
    }

    /**
     * Sets the FPU rounding mode as it was before initialization of the PPL.
     *
     * After calling this method it is absolutely necessary to call
     * {@link #setRoundingForPPL} before using any JPPL abstractions based on
     * floating point numbers. This is performed automatically by
     * {@link #pplFinalize()}.
     */
    public static void restorePrePPLRounding() {
        int result = ppl_restore_pre_PPL_rounding();
        if (result < 0)
            throw new PPLError(result);
    }

    /**
     * Sets the precision parameter used for irrational calculations.
     *
     * If {@code p} is less than or equal to {@link Integer#MAX_VALUE}, sets the
     * precision parameter used for irrational calculations to \(p\). Then, in the
     * irrational calculations returning an unbounded rational, (e.g., when
     * computing a square root), the lesser between numerator and denominator will
     * be limited to \(2^p\).
     */
    public static void setIrrationalPrecision(int p) {
        int result = ppl_set_irrational_precision(p);
        if (result < 0)
            throw new PPLError(result);
    }

    /**
     * Returns the current irrational precision for computations returning an
     * unbounded rational.
     *
     * @see #setIrrationalPrecision(int)
     */
    public static int getIrrationalPrecision() {
        var pref = new IntByReference();
        int result = ppl_irrational_precision(pref);
        if (result < 0)
            throw new PPLError(result);
        return pref.getValue();
    }

    /**
     * Returns the major number of the native PPL version.
     */
    public static int getVersionMajor() {
        int result = ppl_version_major();
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    /**
     * Returns the minor number of the native PPL version.
     */
    public static int getVersionMinor() {
        int result = ppl_version_minor();
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    /**
     * Returns the revision number of the native PPL version.
     */
    public static int getVersionRevision() {
        int result = ppl_version_revision();
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    /** Returns the beta number of the native PPL version. */
    public static int getVersionBeta() {
        int result = ppl_version_beta();
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    /**
     * Returns a string containing the native PPL version.
     *
     * Let M and m denote the numbers returned by {@link getVersionMajor} and
     * {@link getVersionMinor}, respectively. The format of {@link getVersion} is M
     * "." m if both {@link getVersionRevision} (r) and {@link getVersionBeta}
     * (b)are zero, M "." m "pre" b if {@link getVersionRevision} is zero and
     * {@link getVersionBeta} is not zero, M "." m "." r if
     * {@link getVersionRevision} is not zero and {@link getVersionBeta} is zero, M
     * "." m "." r "pre" b if neither {@link getVersionRevision} nor
     * {@link getVersionBeta} are zero.
     */
    public static String getVersion() {
        var p = new PointerByReference();
        int result = ppl_version(p);
        if (result < 0)
            throw new PPLError(result);
        return p.getValue().getString(0);
    }

    /**
     * Returns a string containing the native PPL banner.
     *
     * The banner provides information about the PPL version, the licensing, the
     * lack of any warranty whatsoever, the C++ compiler used to build the library,
     * where to report bugs and where to look for further information.
     */
    public static String getBanner() {
        var p = new PointerByReference();
        int result = ppl_banner(p);
        if (result < 0)
            throw new PPLError(result);
        return p.getValue().getString(0);
    }

    /** Returns the maximum space dimension this library can handle. */
    public static long getMaxSpaceDimension() {
        var mref = new SizeTByReference();
        int result = ppl_max_space_dimension(mref);
        if (result < 0)
            throw new PPLError(result);
        return mref.getValue().longValue();
    }

    /** Returns a value that does not designate a valid dimension. */
    public static long getNotADimension() {
        var mref = new SizeTByReference();
        int result = ppl_not_a_dimension(mref);
        if (result < 0)
            throw new PPLError(result);
        return mref.getValue().longValue();
    }

    /**
     * Sends the string representatio of {@code var} to standard output.
     */
    static void ioPrintVariable(long var) {
        int result = ppl_io_print_variable(new SizeT(var));
        if (result < 0)
            throw new PPLError(result);
    }

    /**
     * Returns the string representation of {@code var}.
     */
    public static String ioASPrintVariable(long var) {
        var strp = new PointerByReference();
        int result = ppl_io_asprint_variable(strp, new SizeT(var));
        if (result < 0)
            throw new PPLError(result);
        var p = strp.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

    /**
     * Sets the output function to be used for printing variables to {@code vof}.
     *
     * This callback is used every time a {@code toString()} method need to compute
     * the string representation of
     */
    public static void ioSetVariableOutputFunction(VariableOutputFunction vof) {
        int result = ppl_io_set_variable_output_function(vof);
        if (result < 0)
            throw new PPLError(result);
    }

    /**
     * Returns the current output function for printing variables.
     */
    public static VariableOutputFunction ioGetVariableOutputFunction() {
        var pp = new VariableOutputFunctionByRef();
        int result = ppl_io_get_variable_output_function(pp);
        if (result < 0)
            throw new PPLError(result);
        return pp.f;
    }

    /**
     * Utility function for the wrapping of lines of text.
     *
     * @param src                         The source string holding the text to
     *                                    wrap.
     * @param indent_depth                The indentation depth.
     * @param preferred_first_line_length The preferred length for the first line of
     *                                    text.
     * @param preferred_line_length       The preferred length for all the lines but
     *                                    the first one.
     * @return The indented string.
     */
    public static String ioWrapString(String src, int indent_depth, int preferred_first_line_length,
            int preferred_line_length) {
        var p = ppl_io_wrap_string(src, indent_depth, preferred_first_line_length, preferred_line_length);
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
