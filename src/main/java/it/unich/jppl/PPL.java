package it.unich.jppl;

import static it.unich.jppl.nativelib.LibPPL.*;

import it.unich.jppl.nativelib.LibPPL.Dimension;

import java.lang.ref.Cleaner;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class PPL {

    static final Cleaner cleaner = Cleaner.create();

    static public enum BoundedIntegerTypeOverflow {
        WRAPS, UNDEFINED, IMPOSSIBLE
    }

    static public enum BoundedIntegerTypeRepresentation {
        UNSIGNED, SIGNED_2_COMPLEMENT
    };

    static public enum BoundedIntegerTypeWidth {
        PPL_BITS_8(8),
        PPL_BITS_16(16),
        PPL_BITS_32(32),
        PPL_BITS_64(64),
        PPL_BITS_128(128);

        int pplValue;

        BoundedIntegerTypeWidth (int pplValue) {
            this.pplValue = pplValue;
        }
    }

    static {
        pplInitialize();
    }

    public static void pplInitialize() {
        // according to documentation it seems ppl_initialize should generate an error if
        // called multiple times,  but it never happended to me
        int result = ppl_initialize();
        if (result < 0)
            throw new PPLError(result);
    }

    public static void pplFinalize() {
        // according to documentation it seems ppl_finalize should generate an error, if
        // called multiple times,  but it never happended to me
        int result = ppl_finalize();
        if (result < 0)
            throw new PPLError(result);
    }

    public static void setRoundingForPPL() {
        int result = ppl_set_rounding_for_PPL();
        if (result < 0)
            throw new PPLError(result);
    }

    public static void restorePrePPLRounding() {
        int result = ppl_restore_pre_PPL_rounding();
        if (result < 0)
            throw new PPLError(result);
    }

    public static void setIrrationalPrecision(int p) {
        int result = ppl_set_irrational_precision(p);
        if (result < 0)
            throw new PPLError(result);
    }

    public static int getIrrationalPrecision() {
        var pref = new IntByReference();
        int result = ppl_irrational_precision(pref);
        if (result < 0)
            throw new PPLError(result);
        return pref.getValue();
    }

    public static int getVersionMajor() {
        int result = ppl_version_major();
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    public static int getVersionMinor() {
        int result = ppl_version_minor();
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    public static int getVersionRevision() {
        int result = ppl_version_revision();
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    public static int getVersionBeta() {
        int result = ppl_version_beta();
        if (result < 0)
            throw new PPLError(result);
        return result;
    }

    public static String getVersion() {
        var p = new PointerByReference();
        int result = ppl_version(p);
        if (result < 0)
            throw new PPLError(result);
        return p.getValue().getString(0);
    }

    public static String getBanner() {
        var p = new PointerByReference();
        int result = ppl_banner(p);
        if (result < 0)
            throw new PPLError(result);
        return p.getValue().getString(0);
    }

    public static long getMaxSpaceDimension() {
        var mref = new DimensionByReference();
        int result = ppl_max_space_dimension(mref);
        if (result < 0)
            throw new PPLError(result);
        return mref.getValue().longValue();
    }

    public static long getNotADimension() {
        var mref = new DimensionByReference();
        int result = ppl_not_a_dimension(mref);
        if (result < 0)
            throw new PPLError(result);
        return mref.getValue().longValue();
    }

    public static void ioPrintVariable(long var) {
        int result = ppl_io_print_variable(new Dimension(var));
        if (result < 0)
            throw new PPLError(result);
    }

    public static String ioASPrintVariable(long var) {
        var strp = new PointerByReference();
        int result = ppl_io_asprint_variable(strp, new Dimension(var));
        if (result < 0)
            throw new PPLError(result);
        var p = strp.getValue();
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

    public static void ioSetVariableOutputFunction(VariableOutputFunction p) {
        int result = ppl_io_set_variable_output_function(p);
        if (result < 0)
            throw new PPLError(result);
    }

    public static VariableOutputFunction ioGetVariableOutputFunction() {
        var pp = new VariableOutputFunctionByRef();
        int result = ppl_io_get_variable_output_function(pp);
        if (result < 0)
            throw new PPLError(result);
        return pp.f;
    }

    public static String ioWrapString(String src, int indent_depth, int preferred_first_line_length,
            int preferred_line_length) {
        var p = ppl_io_wrap_string(src, indent_depth, preferred_first_line_length, preferred_line_length);
        var s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
