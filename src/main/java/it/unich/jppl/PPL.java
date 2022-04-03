package it.unich.jppl;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.*;

import static it.unich.jppl.nativelib.LibPPL.*;

final class PPL {
    static {
        pplInitialize();
    }

    static void pplInitialize() {
        // according to documentation it seems ppl_initialize could generate an error,
        // but it never happended to me
        int pplError = ppl_initialize();
        if (pplError != 0) throw new PPLError(pplError);
    }

    static void pplFinalize() {
        // according to documentation it seems ppl_finalize could generate an error,
        // but it never happended to me
        int pplError = ppl_finalize();
        if (pplError != 0) throw new PPLError(pplError);
    }

    static void setRoundingForPPL() {
        ppl_set_rounding_for_PPL();
    }

    static void restorePrePPLRounding() {
        ppl_restore_pre_PPL_rounding();
    }

    static void setIrrationalPrecision(int p) {
        ppl_set_irrational_precision(p);
    }

    static int getIrrationalPrecision() {
        IntByReference p  = new IntByReference();
        ppl_irrational_precision(p);
        return p.getValue();
    }

    static int getVersionMajor() {
        return ppl_version_major();
    }

    static int getVersionMinor() {
        return ppl_version_minor();
    }

    static int getVersionRevision() {
        return ppl_version_revision();
    }

    static int getVersionBeta() {
        return ppl_version_beta();
    }

    static String getVersion() {
        PointerByReference pref = new PointerByReference();
        ppl_version(pref);
        Pointer p = pref.getValue();
        return p.getString(0);
    }

    static String getBanner() {
        PointerByReference pref = new PointerByReference();
        ppl_banner(pref);
        Pointer p = pref.getValue();
        return p.getString(0);
    }
}
