/**
 * Created by amato on 17/03/16.
 */
package it.unich.jppl.nativeppl;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.ptr.*;

public final class LibPPL {

    static final class CPolyhedron extends PointerType { };

    private static final String LIBNAME = "libppl_c";

    static {
        Native.register(LIBNAME);
    }

     // Library Initialization and Finalization

    public static native int ppl_initialize();

    public static native int ppl_finalize();

    public static native int ppl_set_rounding_for_PPL();

    public static native int ppl_restore_pre_PPL_rounding();

    public static native int ppl_irrational_precision(IntByReference p);

    public static native int ppl_set_irrational_precision(int p);

    // Version Checking

    public static native int ppl_version_major();

    public static native int ppl_version_minor();

    public static native int ppl_version_revision();

    public static native int ppl_version_beta();

    public static native int ppl_version(PointerByReference p);

    public static native int ppl_banner(PointerByReference p);

    // Polyhedron

    public static native int ppl_new_C_Polyhedron_from_space_dimension(PointerByReference pph, long d, int empty);

    public static native int ppl_io_print_Polyhedron(Pointer x);

    public static native int ppl_Polyhedron_add_constraint (Pointer ph, Pointer c);

    public static native int ppl_Polyhedron_refine_with_constraint (Pointer ph, Pointer c);

    public static native int ppl_io_asprint_Polyhedron (PointerByReference strp, Pointer x);

    // Coefficients

    public static native int ppl_new_Coefficient(PointerByReference pc);

    public static native int ppl_new_Coefficient_from_mpz_t (PointerByReference pc, Pointer z);

    public static native int ppl_io_print_Coefficient (Pointer x);

    public static native int ppl_io_asprint_Coefficient (PointerByReference strp, Pointer x);

    // Linear Expressions

    public static native int ppl_new_Linear_Expression(PointerByReference ple);

    public static native int ppl_Linear_Expression_add_to_coefficient(Pointer le, long variable, Pointer n);

    public static native int ppl_io_print_Linear_Expression (Pointer le);

    public static native int ppl_io_asprint_Linear_Expression (PointerByReference le, Pointer x);

    public static native int ppl_Linear_Expression_add_to_inhomogeneous (Pointer le, Pointer n);

    // Constraint

    public static native int ppl_new_Constraint (PointerByReference pc, Pointer le, int rel);

    public static native int ppl_io_print_Constraint (Pointer c);

    public static native int ppl_io_asprint_Constraint (PointerByReference le, Pointer x);

}
