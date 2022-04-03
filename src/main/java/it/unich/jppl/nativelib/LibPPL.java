/**
 * Created by amato on 17/03/16.
 */
package it.unich.jppl.nativelib;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.PointerType;
import com.sun.jna.IntegerType;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.*;

import it.unich.jppl.VariableOutputFunction;
import it.unich.jppl.ErrorHandler;

public final class LibPPL {

    static final class CPolyhedron extends PointerType { };

    private static final String LIBNAME = "ppl_c";

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

    // Error handling

    public static native int ppl_set_error_handler (ErrorHandler h);

    // Timeout handling

    public static native int ppl_set_timeout(int csecs);

    public static native int ppl_reset_timeout();

    public static native int ppl_set_deterministic_timeout(long unscaled_weight, int scale);

    public static native int ppl_reset_deterministic_timeout();

    // Dimensions

    public static class Dimension extends IntegerType {
        public Dimension() { this(0); }
        public Dimension(long value) { super(Native.SIZE_T_SIZE, value, true); }
    }

    public static class DimensionByReference extends ByReference {
        public DimensionByReference() {
          this(new Dimension());
        }

        public DimensionByReference(Dimension value) {
          super(Native.SIZE_T_SIZE);
          setValue(value);
        }

        public void setValue(Dimension value) {
          Pointer p = getPointer();
          if (Native.SIZE_T_SIZE == 8) {
            p.setLong(0, value.longValue());
          } else {
            p.setInt(0, value.intValue());
          }
        }

        public Dimension getValue() {
          Pointer p = getPointer();
          return new Dimension(Native.SIZE_T_SIZE == 8 ? p.getLong(0) : p.getInt(0));
        }
    }

    @FieldOrder({"f"})
    public static class VariableOutputFunctionByRef extends Structure {
        public VariableOutputFunction f;
    }

    public static native int ppl_max_space_dimension (DimensionByReference m);

    public static native int ppl_not_a_dimension (DimensionByReference m);

    public static native int ppl_io_print_variable (Dimension var);

    public static native int ppl_io_fprint_variable (Pointer stream, Dimension var);

    public static native int ppl_io_asprint_variable (PointerByReference strp, Dimension var);

    public static native int ppl_io_set_variable_output_function (VariableOutputFunction p);

    public static native int ppl_io_get_variable_output_function (VariableOutputFunctionByRef pp);

    public static native Pointer ppl_io_wrap_string(String src, int indent_depth, int preferred_first_line_length, int preferred_line_length);

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
