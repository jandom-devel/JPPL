package it.unich.jppl;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.*;

import it.unich.jppl.nativelib.LibPPL;
import static it.unich.jppl.nativelib.LibPPL.*;

import java.lang.ref.Cleaner;

final class PPL {

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

    public static final Cleaner cleaner = Cleaner.create();

    static {
        pplInitialize();
    }

    public static void pplInitialize() {
        // according to documentation it seems ppl_initialize could generate an error,
        // but it never happended to me
        int pplError = ppl_initialize();
        if (pplError != 0) throw new PPLError(pplError);
    }

    public static void pplFinalize() {
        // according to documentation it seems ppl_finalize could generate an error,
        // but it never happended to me
        int pplError = ppl_finalize();
        if (pplError != 0) throw new PPLError(pplError);
    }

    public static void setRoundingForPPL() {
        ppl_set_rounding_for_PPL();
    }

    public static void restorePrePPLRounding() {
        ppl_restore_pre_PPL_rounding();
    }

    public static void setIrrationalPrecision(int p) {
        ppl_set_irrational_precision(p);
    }

    public static int getIrrationalPrecision() {
        IntByReference p  = new IntByReference();
        ppl_irrational_precision(p);
        return p.getValue();
    }

    public static int getVersionMajor() {
        return ppl_version_major();
    }

    public static int getVersionMinor() {
        return ppl_version_minor();
    }

    public static int getVersionRevision() {
        return ppl_version_revision();
    }

    public static int getVersionBeta() {
        return ppl_version_beta();
    }

    public static String getVersion() {
        PointerByReference pref = new PointerByReference();
        ppl_version(pref);
        Pointer p = pref.getValue();
        return p.getString(0);
    }

    public static String getBanner() {
        PointerByReference pref = new PointerByReference();
        ppl_banner(pref);
        Pointer p = pref.getValue();
        return p.getString(0);
    }

    public static long getMaxSpaceDimension() {
        LibPPL.DimensionByReference mref = new LibPPL.DimensionByReference();
        ppl_max_space_dimension(mref);
        return mref.getValue().longValue();
    }

    public static long getNotADimension() {
        LibPPL.DimensionByReference mref = new LibPPL.DimensionByReference();
        ppl_not_a_dimension(mref);
        return mref.getValue().longValue();
    }

    public static void IOPrintVariable(long var) {
        ppl_io_print_variable(new LibPPL.Dimension(var));
    }

    public static String IOASPrintVariable(long var) {
        PointerByReference pref = new PointerByReference();
        ppl_io_asprint_variable(pref, new Dimension(var));
        Pointer p = pref.getValue();
        String s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }

    public static void IOSetVariableOutputFunction(VariableOutputFunction p) {
        ppl_io_set_variable_output_function(p);
    }

    public static VariableOutputFunction IOGetVariableOutputFunction() {
        LibPPL.VariableOutputFunctionByRef fref = new LibPPL.VariableOutputFunctionByRef();
        ppl_io_get_variable_output_function(fref);
        return fref.f;
    }

    public static String IOWrapString(String src, int indent_depth, int preferred_first_line_length, int preferred_line_length) {
        Pointer p = ppl_io_wrap_string(src, indent_depth, preferred_first_line_length, preferred_line_length);
        String s = p.getString(0);
        Native.free(Pointer.nativeValue(p));
        return s;
    }
}
