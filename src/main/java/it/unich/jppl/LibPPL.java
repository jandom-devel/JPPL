package it.unich.jppl;

import com.sun.jna.IntegerType;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * This class contains all native methods which are used by the other classes of
 * the package to interface with the C library.
 */
public final class LibPPL {

    private static final String LIBNAME = "ppl_c";

    static {
        Native.register(LIBNAME);
    }

    // Library Initialization and Finalization

    static native int ppl_initialize();

    static native int ppl_finalize();

    static native int ppl_set_rounding_for_PPL();

    static native int ppl_restore_pre_PPL_rounding();

    static native int ppl_irrational_precision(IntByReference p);

    static native int ppl_set_irrational_precision(int p);

    // Version Checking

    static native int ppl_version_major();

    static native int ppl_version_minor();

    static native int ppl_version_revision();

    static native int ppl_version_beta();

    static native int ppl_version(PointerByReference p);

    static native int ppl_banner(PointerByReference p);

    // Error handling

    static native int ppl_set_error_handler(ErrorHandler h);

    // Timeout handling

    static native int ppl_set_timeout(int csecs);

    static native int ppl_reset_timeout();

    static native int ppl_set_deterministic_timeout(NativeLong unscaled_weight, int scale);

    static native int ppl_reset_deterministic_timeout();

    // Dimensions

    protected static class SizeT extends IntegerType {
        public SizeT() {
            this(0);
        }

        SizeT(long value) {
            super(Native.SIZE_T_SIZE, value, true);
        }
    }

    protected static class SizeTArray extends Memory {
        public SizeTArray(long[] ds) {
            super(ds.length * Native.SIZE_T_SIZE);
            for (int i = 0; i < ds.length; i++)
                if (Native.SIZE_T_SIZE == 8)
                    setLong(8 * i, ds[i]);
                else
                    setInt(4 * i, (int) ds[i]);
        }
    }

    protected static class SizeTByReference extends ByReference {
        public SizeTByReference() {
            this(new SizeT());
        }

        SizeTByReference(SizeT value) {
            super(Native.SIZE_T_SIZE);
            setValue(value);
        }

        void setValue(SizeT value) {
            Pointer p = getPointer();
            if (Native.SIZE_T_SIZE == 8) {
                p.setLong(0, value.longValue());
            } else {
                p.setInt(0, value.intValue());
            }
        }

        SizeT getValue() {
            Pointer p = getPointer();
            return new SizeT(Native.SIZE_T_SIZE == 8 ? p.getLong(0) : p.getInt(0));
        }
    }

    @FieldOrder({ "f" })
    protected static class VariableOutputFunctionByRef extends Structure {
        public VariableOutputFunction f;
    }

    static native int ppl_max_space_dimension(SizeTByReference m);

    static native int ppl_not_a_dimension(SizeTByReference m);

    static native int ppl_io_print_variable(SizeT var);

    static native int ppl_io_fprint_variable(Pointer stream, SizeT var);

    static native int ppl_io_asprint_variable(PointerByReference strp, SizeT var);

    static native int ppl_io_set_variable_output_function(VariableOutputFunction p);

    static native int ppl_io_get_variable_output_function(VariableOutputFunctionByRef pp);

    static native Pointer ppl_io_wrap_string(String src, int indent_depth, int preferred_first_line_length,
            int preferred_line_length);

    // Coefficients

    static native int ppl_new_Coefficient(PointerByReference pc);

    static native int ppl_new_Coefficient_from_mpz_t(PointerByReference pc, Pointer z);

    static native int ppl_new_Coefficient_from_Coefficient(PointerByReference pc, Pointer c);

    static native int ppl_assign_Coefficient_from_mpz_t(Pointer dst, Pointer z);

    static native int ppl_assign_Coefficient_from_Coefficient(Pointer dst, Pointer src);

    static native int ppl_delete_Coefficient(Pointer c);

    static native int ppl_Coefficient_to_mpz_t(Pointer c, Pointer z);

    static native int ppl_Coefficient_OK(Pointer c);

    static native int ppl_Coefficient_is_bounded();

    static native int ppl_Coefficient_min(Pointer min);

    static native int ppl_Coefficient_max(Pointer max);

    static native int ppl_io_print_Coefficient(Pointer x);

    static native int ppl_io_fprint_Coefficient(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Coefficient(PointerByReference strp, Pointer x);

    // Linear Expressions

    static native int ppl_new_Linear_Expression(PointerByReference ple);

    static native int ppl_new_Linear_Expression_with_dimension(PointerByReference ple, SizeT d);

    static native int ppl_new_Linear_Expression_from_Linear_Expression(PointerByReference ple, Pointer le);

    static native int ppl_new_Linear_Expression_from_Constraint(PointerByReference ple, Pointer c);

    static native int ppl_new_Linear_Expression_from_Generator(PointerByReference ple, Pointer g);

    static native int ppl_new_Linear_Expression_from_Congruence(PointerByReference ple, Pointer c);

    //static native int ppl_new_Linear_Expression_from_Grid_Generator (PointerByReference ple, Pointer g);

    static native int ppl_assign_Linear_Expression_from_Linear_Expression(Pointer dst, Pointer src);

    static native int ppl_delete_Linear_Expression(Pointer le);

    static native int ppl_Linear_Expression_space_dimension(Pointer le, SizeTByReference m);

    static native int ppl_Linear_Expression_coefficient(Pointer le, SizeT var, Pointer n);

    static native int ppl_Linear_Expression_inhomogeneous_term(Pointer le, Pointer n);

    static native int ppl_Linear_Expression_OK(Pointer le);

    static native int ppl_Linear_Expression_is_zero(Pointer le);

    static native int ppl_Linear_Expression_all_homogeneous_terms_are_zero(Pointer le);

    static native int ppl_Linear_Expression_add_to_coefficient(Pointer le, SizeT var, Pointer n);

    static native int ppl_Linear_Expression_add_to_inhomogeneous(Pointer le, Pointer n);

    static native int ppl_add_Linear_Expression_to_Linear_Expression(Pointer dst, Pointer src);

    static native int ppl_multiply_Linear_Expression_by_Coefficient(Pointer le, Pointer n);

    static native int ppl_io_print_Linear_Expression(Pointer x);

    static native int ppl_io_fprint_Linear_Expression(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Linear_Expression(PointerByReference strp, Pointer x);

    static native int ppl_Linear_Expression_ascii_dump(Pointer x, Pointer stream);

    static native int ppl_Linear_Expression_ascii_load(Pointer x, Pointer steam);

    // Constraint

    static native int ppl_new_Constraint(PointerByReference pc, Pointer le, int rel);

    static native int ppl_new_Constraint_zero_dim_false(PointerByReference pc);

    static native int ppl_new_Constraint_zero_dim_positivity(PointerByReference pc);

    static native int ppl_new_Constraint_from_Constraint(PointerByReference pc, Pointer c);

    static native int ppl_assign_Constraint_from_Constraint(Pointer dst, Pointer src);

    static native int ppl_delete_Constraint(Pointer c);

    static native int ppl_Constraint_space_dimension(Pointer c, SizeTByReference m);

    static native int ppl_Constraint_type(Pointer c);

    static native int ppl_Constraint_coefficient(Pointer c, SizeT var, Pointer n);

    static native int ppl_Constraint_inhomogeneous_term(Pointer c, Pointer n);

    static native int ppl_Constraint_OK(Pointer c);

    static native int ppl_io_print_Constraint(Pointer x);

    static native int ppl_io_fprint_Constraint(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Constraint(PointerByReference strp, Pointer x);

    static native int ppl_Constraint_ascii_dump(Pointer x, Pointer stream);

    static native int ppl_Constraint_ascii_load(Pointer x, Pointer steam);

    // Constraint System

    static native int ppl_new_Constraint_System(PointerByReference pcs);

    static native int ppl_new_Constraint_System_zero_dim_empty(PointerByReference pcs);

    static native int ppl_new_Constraint_System_from_Constraint(PointerByReference pcs, Pointer c);

    static native int ppl_new_Constraint_System_from_Constraint_System(PointerByReference pcs, Pointer cs);

    static native int ppl_assign_Constraint_System_from_Constraint_System(Pointer dst, Pointer src);

    static native int ppl_delete_Constraint_System(Pointer cs);

    static native int ppl_Constraint_System_space_dimension(Pointer cs, SizeTByReference m);

    static native int ppl_Constraint_System_empty(Pointer cs);

    static native int ppl_Constraint_System_has_strict_inequalities(Pointer cs);

    static native int ppl_Constraint_System_begin(Pointer cs, Pointer cit);

    static native int ppl_Constraint_System_end(Pointer cs, Pointer cit);

    static native int ppl_Constraint_System_OK(Pointer cs);

    static native int ppl_Constraint_System_clear(Pointer cs);

    static native int ppl_Constraint_System_insert_Constraint(Pointer cs, Pointer c);

    static native int ppl_io_print_Constraint_System(Pointer x);

    static native int ppl_io_fprint_Constraint_System(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Constraint_System(PointerByReference strp, Pointer x);

    static native int ppl_Constraint_System_ascii_dump(Pointer x, Pointer stream);

    static native int ppl_Constraint_System_ascii_load(Pointer x, Pointer steam);

    // Constraint System Iterator

    static native int ppl_new_Constraint_System_const_iterator(PointerByReference pcit);

    static native int ppl_new_Constraint_System_const_iterator_from_Constraint_System_const_iterator(
            PointerByReference pcit, Pointer cit);

    static native int ppl_assign_Constraint_System_const_iterator_from_Constraint_System_const_iterator(Pointer dst,
            Pointer src);

    static native int ppl_delete_Constraint_System_const_iterator(Pointer cit);

    static native int ppl_Constraint_System_const_iterator_dereference(Pointer cit, PointerByReference pc);

    static native int ppl_Constraint_System_const_iterator_increment(Pointer cit);

    static native int ppl_Constraint_System_const_iterator_equal_test(Pointer x, Pointer y);

    // Generator

    static native int ppl_new_Generator(PointerByReference pg, Pointer le, int t, Pointer d);

    static native int ppl_new_Generator_zero_dim_point(PointerByReference pg);

    static native int ppl_new_Generator_zero_dim_closure_point(PointerByReference pg);

    static native int ppl_new_Generator_from_Generator(PointerByReference pg, Pointer g);

    static native int ppl_assign_Generator_from_Generator(Pointer dst, Pointer src);

    static native int ppl_delete_Generator(Pointer g);

    static native int ppl_Generator_space_dimension(Pointer g, SizeTByReference m);

    static native int ppl_Generator_type(Pointer g);

    static native int ppl_Generator_coefficient(Pointer g, SizeT var, Pointer n);

    static native int ppl_Generator_divisor(Pointer g, Pointer d);

    static native int ppl_Generator_OK(Pointer g);

    static native int ppl_io_print_Generator(Pointer x);

    static native int ppl_io_fprint_Generator(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Generator(PointerByReference strp, Pointer x);

    static native int ppl_Generator_ascii_dump(Pointer x, Pointer stream);

    static native int ppl_Generator_ascii_load(Pointer x, Pointer steam);

    // Generator System

    static native int ppl_new_Generator_System(PointerByReference pgs);

    static native int ppl_new_Generator_System_from_Generator(PointerByReference pgs, Pointer g);

    static native int ppl_new_Generator_System_from_Generator_System(PointerByReference pgs, Pointer gs);

    static native int ppl_assign_Generator_System_from_Generator_System(Pointer dst, Pointer src);

    static native int ppl_delete_Generator_System(Pointer gs);

    static native int ppl_Generator_System_space_dimension(Pointer gs, SizeTByReference m);

    static native int ppl_Generator_System_empty(Pointer gs);

    static native int ppl_Generator_System_begin(Pointer gs, Pointer cit);

    static native int ppl_Generator_System_end(Pointer gs, Pointer cit);

    static native int ppl_Generator_System_OK(Pointer gs);

    static native int ppl_Generator_System_clear(Pointer gs);

    static native int ppl_Generator_System_insert_Generator(Pointer gs, Pointer g);

    static native int ppl_io_print_Generator_System(Pointer x);

    static native int ppl_io_fprint_Generator_System(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Generator_System(PointerByReference strp, Pointer x);

    static native int ppl_Generator_System_ascii_dump(Pointer x, Pointer stream);

    static native int ppl_Generator_System_ascii_load(Pointer x, Pointer steam);

    // Generator System Iterator

    static native int ppl_new_Generator_System_const_iterator(PointerByReference pcit);

    static native int ppl_new_Generator_System_const_iterator_from_Generator_System_const_iterator(
            PointerByReference pcit, Pointer cit);

    static native int ppl_assign_Generator_System_const_iterator_from_Generator_System_const_iterator(Pointer dst,
            Pointer src);

    static native int ppl_delete_Generator_System_const_iterator(Pointer cit);

    static native int ppl_Generator_System_const_iterator_dereference(Pointer cit, PointerByReference pc);

    static native int ppl_Generator_System_const_iterator_increment(Pointer cit);

    static native int ppl_Generator_System_const_iterator_equal_test(Pointer x, Pointer y);

    // Congruence

    static native int ppl_new_Congruence(PointerByReference pc, Pointer le, Pointer m);

    static native int ppl_new_Congruence_zero_dim_false(PointerByReference pc);

    static native int ppl_new_Congruence_zero_dim_integrality(PointerByReference pc);

    static native int ppl_new_Congruence_from_Congruence(PointerByReference pc, Pointer c);

    static native int ppl_assign_Congruence_from_Congruence(Pointer dst, Pointer src);

    static native int ppl_delete_Congruence(Pointer c);

    static native int ppl_Congruence_space_dimension(Pointer c, SizeTByReference m);

    static native int ppl_Congruence_coefficient(Pointer c, SizeT var, Pointer n);

    static native int ppl_Congruence_inhomogeneous_term(Pointer c, Pointer n);

    static native int ppl_Congruence_modulus(Pointer c, Pointer n);

    static native int ppl_Congruence_OK(Pointer c);

    static native int ppl_io_print_Congruence(Pointer x);

    static native int ppl_io_fprint_Congruence(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Congruence(PointerByReference strp, Pointer x);

    static native int ppl_Congruence_ascii_dump(Pointer x, Pointer stream);

    static native int ppl_Congruence_ascii_load(Pointer x, Pointer steam);

    // Congruence System

    static native int ppl_new_Congruence_System(PointerByReference pcs);

    static native int ppl_new_Congruence_System_zero_dim_empty(PointerByReference pcs);

    static native int ppl_new_Congruence_System_from_Congruence(PointerByReference pcs, Pointer c);

    static native int ppl_new_Congruence_System_from_Congruence_System(PointerByReference pcs, Pointer cs);

    static native int ppl_assign_Congruence_System_from_Congruence_System(Pointer dst, Pointer src);

    static native int ppl_delete_Congruence_System(Pointer cs);

    static native int ppl_Congruence_System_space_dimension(Pointer cs, SizeTByReference m);

    static native int ppl_Congruence_System_empty(Pointer cs);

    static native int ppl_Congruence_System_begin(Pointer cs, Pointer git);

    static native int ppl_Congruence_System_end(Pointer cs, Pointer git);

    static native int ppl_Congruence_System_OK(Pointer cs);

    static native int ppl_Congruence_System_clear(Pointer cs);

    static native int ppl_Congruence_System_insert_Congruence(Pointer cs, Pointer c);

    static native int ppl_io_print_Congruence_System(Pointer x);

    static native int ppl_io_fprint_Congruence_System(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Congruence_System(PointerByReference strp, Pointer x);

    static native int ppl_Congruence_System_ascii_dump(Pointer x, Pointer stream);

    static native int ppl_Congruence_System_ascii_load(Pointer x, Pointer steam);

    // Congruence System Iterator

    static native int ppl_new_Congruence_System_const_iterator(PointerByReference pgit);

    static native int ppl_new_Congruence_System_const_iterator_from_Congruence_System_const_iterator(
            PointerByReference pgit, Pointer git);

    static native int ppl_assign_Congruence_System_const_iterator_from_Congruence_System_const_iterator(Pointer dst,
            Pointer src);

    static native int ppl_delete_Congruence_System_const_iterator(Pointer git);

    static native int ppl_Congruence_System_const_iterator_dereference(Pointer git, PointerByReference pg);

    static native int ppl_Congruence_System_const_iterator_increment(Pointer git);

    static native int ppl_Congruence_System_const_iterator_equal_test(Pointer x, Pointer y);

    // Grid Generator

    static native int ppl_new_Grid_Generator(PointerByReference pg, Pointer le, int t, Pointer d);

    static native int ppl_new_Grid_Generator_zero_dim_point(PointerByReference pg);

    static native int ppl_new_Grid_Generator_from_Grid_Generator(PointerByReference pg, Pointer g);

    static native int ppl_assign_Grid_Generator_from_Grid_Generator(Pointer dst, Pointer src);

    static native int ppl_delete_Grid_Generator(Pointer g);

    static native int ppl_Grid_Generator_space_dimension(Pointer g, SizeTByReference m);

    static native int ppl_Grid_Generator_type(Pointer g);

    static native int ppl_Grid_Generator_coefficient(Pointer g, SizeT var, Pointer n);

    static native int ppl_Grid_Generator_divisor(Pointer g, Pointer d);

    static native int ppl_Grid_Generator_OK(Pointer g);

    static native int ppl_io_print_Grid_Generator(Pointer x);

    static native int ppl_io_fprint_Grid_Generator(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Grid_Generator(PointerByReference strp, Pointer x);

    static native int ppl_Grid_Generator_ascii_dump(Pointer x, Pointer stream);

    static native int ppl_Grid_Generator_ascii_load(Pointer x, Pointer steam);

    // Grid Generator System

    static native int ppl_new_Grid_Generator_System(PointerByReference pgs);

    static native int ppl_new_Grid_Generator_System_from_Grid_Generator(PointerByReference pgs, Pointer g);

    static native int ppl_new_Grid_Generator_System_from_Grid_Generator_System(PointerByReference pgs, Pointer gs);

    static native int ppl_assign_Grid_Generator_System_from_Grid_Generator_System(Pointer dst, Pointer src);

    static native int ppl_delete_Grid_Generator_System(Pointer gs);

    static native int ppl_Grid_Generator_System_space_dimension(Pointer gs, SizeTByReference m);

    static native int ppl_Grid_Generator_System_empty(Pointer gs);

    static native int ppl_Grid_Generator_System_begin(Pointer gs, Pointer git);

    static native int ppl_Grid_Generator_System_end(Pointer gs, Pointer git);

    static native int ppl_Grid_Generator_System_OK(Pointer gs);

    static native int ppl_Grid_Generator_System_clear(Pointer gs);

    static native int ppl_Grid_Generator_System_insert_Grid_Generator(Pointer gs, Pointer g);

    static native int ppl_io_print_Grid_Generator_System(Pointer x);

    static native int ppl_io_fprint_Grid_Generator_System(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Grid_Generator_System(PointerByReference strp, Pointer x);

    static native int ppl_Grid_Generator_System_ascii_dump(Pointer x, Pointer stream);

    static native int ppl_Grid_Generator_System_ascii_load(Pointer x, Pointer steam);

    // Grid Generator System Iterator

    static native int ppl_new_Grid_Generator_System_const_iterator(PointerByReference pgit);

    static native int ppl_new_Grid_Generator_System_const_iterator_from_Grid_Generator_System_const_iterator(
            PointerByReference pgit, Pointer git);

    static native int ppl_assign_Grid_Generator_System_const_iterator_from_Grid_Generator_System_const_iterator(
            Pointer dst, Pointer src);

    static native int ppl_delete_Grid_Generator_System_const_iterator(Pointer git);

    static native int ppl_Grid_Generator_System_const_iterator_dereference(Pointer git, PointerByReference pg);

    static native int ppl_Grid_Generator_System_const_iterator_increment(Pointer git);

    static native int ppl_Grid_Generator_System_const_iterator_equal_test(Pointer x, Pointer y);

    // CPolyhedron

    static native int ppl_new_C_Polyhedron_from_space_dimension(PointerByReference pph, SizeT d, int empty);

    static native int ppl_new_C_Polyhedron_from_C_Polyhedron(PointerByReference pph, Pointer ph);

    static native int ppl_new_C_Polyhedron_from_C_Polyhedron_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    static native int ppl_new_C_Polyhedron_from_Constraint_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_C_Polyhedron_recycle_Constraint_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_C_Polyhedron_from_Congruence_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_C_Polyhedron_recycle_Congruence_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_C_Polyhedron_from_Generator_System(PointerByReference pph, Pointer gs);

    static native int ppl_new_C_Polyhedron_recycle_Generator_System(PointerByReference pph, Pointer gs);

    static native int ppl_assign_C_Polyhedron_from_C_Polyhedron(Pointer dst, Pointer src);

    static native int ppl_new_C_Polyhedron_from_NNC_Polyhedron(PointerByReference pph, Pointer ph);

    static native int ppl_new_C_Polyhedron_from_NNC_Polyhedron_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    static native int ppl_new_C_Polyhedron_from_Double_Box(PointerByReference pph, Pointer ph);

    static native int ppl_new_C_Polyhedron_from_Double_Box_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    // NNCPolyhedron

    static native int ppl_new_NNC_Polyhedron_from_space_dimension(PointerByReference pph, SizeT d, int empty);

    static native int ppl_new_NNC_Polyhedron_from_NNC_Polyhedron(PointerByReference pph, Pointer ph);

    static native int ppl_new_NNC_Polyhedron_from_NNC_Polyhedron_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    static native int ppl_new_NNC_Polyhedron_from_Constraint_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_NNC_Polyhedron_recycle_Constraint_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_NNC_Polyhedron_from_Congruence_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_NNC_Polyhedron_recycle_Congruence_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_NNC_Polyhedron_from_Generator_System(PointerByReference pph, Pointer gs);

    static native int ppl_new_NNC_Polyhedron_recycle_Generator_System(PointerByReference pph, Pointer gs);

    static native int ppl_assign_NNC_Polyhedron_from_NNC_Polyhedron(Pointer dst, Pointer src);

    static native int ppl_new_NNC_Polyhedron_from_C_Polyhedron(PointerByReference pph, Pointer ph);

    static native int ppl_new_NNC_Polyhedron_from_C_Polyhedron_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    static native int ppl_new_NNC_Polyhedron_from_Double_Box(PointerByReference pph, Pointer ph);

    static native int ppl_new_NNC_Polyhedron_from_Double_Box_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    // Polyhedron

    static native int ppl_delete_Polyhedron(Pointer ph);

    static native int ppl_Polyhedron_space_dimension(Pointer ph, SizeTByReference m);

    static native int ppl_Polyhedron_affine_dimension(Pointer ph, SizeTByReference m);

    static native int ppl_Polyhedron_relation_with_Constraint(Pointer ph, Pointer c);

    static native int ppl_Polyhedron_relation_with_Generator(Pointer ph, Pointer g);

    static native int ppl_Polyhedron_get_constraints(Pointer ph, PointerByReference pcs);

    static native int ppl_Polyhedron_get_congruences(Pointer ph, PointerByReference pcs);

    static native int ppl_Polyhedron_get_minimized_constraints(Pointer ph, PointerByReference pcs);

    static native int ppl_Polyhedron_get_minimized_congruences(Pointer ph, PointerByReference pcs);

    static native int ppl_Polyhedron_is_empty(Pointer ph);

    static native int ppl_Polyhedron_is_universe(Pointer ph);

    static native int ppl_Polyhedron_is_bounded(Pointer ph);

    static native int ppl_Polyhedron_contains_integer_point(Pointer ph);

    static native int ppl_Polyhedron_is_topologically_closed(Pointer ph);

    static native int ppl_Polyhedron_is_discrete(Pointer ph);

    static native int ppl_Polyhedron_constrains(Pointer ph, SizeT var);

    static native int ppl_Polyhedron_bounds_from_above(Pointer ph, Pointer le);

    static native int ppl_Polyhedron_bounds_from_below(Pointer ph, Pointer le);

    static native int ppl_Polyhedron_maximize_with_point(Pointer ph, Pointer le, Pointer sup_n, Pointer sup_d,
            IntByReference pmaximum, Pointer point);

    static native int ppl_Polyhedron_maximize(Pointer ph, Pointer le, Pointer sup_n, Pointer sup_d,
            IntByReference pmaximum);

    static native int ppl_Polyhedron_minimize_with_point(Pointer ph, Pointer le, Pointer inf_n, Pointer inf_d,
            IntByReference pminimum, Pointer point);

    static native int ppl_Polyhedron_minimize(Pointer ph, Pointer le, Pointer inf_n, Pointer inf_d,
            IntByReference pminimum);

    static native int ppl_Polyhedron_contains_Polyhedron(Pointer x, Pointer y);

    static native int ppl_Polyhedron_strictly_contains_Polyhedron(Pointer x, Pointer y);

    static native int ppl_Polyhedron_is_disjoint_from_Polyhedron(Pointer x, Pointer y);

    static native int ppl_Polyhedron_equals_Polyhedron(Pointer x, Pointer y);

    static native int ppl_Polyhedron_OK(Pointer ph);

    static native int ppl_Polyhedron_external_memory_in_bytes(Pointer ph, SizeTByReference sz);

    static native int ppl_Polyhedron_total_memory_in_bytes(Pointer ph, SizeTByReference sz);

    static native int ppl_Polyhedron_add_constraint(Pointer ph, Pointer c);

    static native int ppl_Polyhedron_add_congruence(Pointer ph, Pointer c);

    static native int ppl_Polyhedron_add_constraints(Pointer ph, Pointer cs);

    static native int ppl_Polyhedron_add_congruences(Pointer ph, Pointer cs);

    static native int ppl_Polyhedron_add_recycled_constraints(Pointer ph, Pointer cs);

    static native int ppl_Polyhedron_add_recycled_congruences(Pointer ph, Pointer cs);

    static native int ppl_Polyhedron_refine_with_constraint(Pointer ph, Pointer c);

    static native int ppl_Polyhedron_refine_with_congruence(Pointer ph, Pointer c);

    static native int ppl_Polyhedron_refine_with_constraints(Pointer ph, Pointer cs);

    static native int ppl_Polyhedron_refine_with_congruences(Pointer ph, Pointer cs);

    static native int ppl_Polyhedron_intersection_assign(Pointer x, Pointer y);

    static native int ppl_Polyhedron_upper_bound_assign(Pointer x, Pointer y);

    static native int ppl_Polyhedron_difference_assign(Pointer x, Pointer y);

    static native int ppl_Polyhedron_simplify_using_context_assign(Pointer x, Pointer y);

    static native int ppl_Polyhedron_time_elapse_assign(Pointer x, Pointer y);

    static native int ppl_Polyhedron_topological_closure_assign(Pointer ph);

    static native int ppl_Polyhedron_unconstrain_space_dimension(Pointer ph, SizeT var);

    static native int ppl_Polyhedron_unconstrain_space_dimensions(Pointer ph, Pointer ds, SizeT n);

    static native int ppl_Polyhedron_affine_image(Pointer ph, SizeT var, Pointer le, Pointer d);

    static native int ppl_Polyhedron_affine_preimage(Pointer ph, SizeT var, Pointer le, Pointer d);

    static native int ppl_Polyhedron_bounded_affine_image(Pointer ph, SizeT var, Pointer lb, Pointer ub, Pointer d);

    static native int ppl_Polyhedron_bounded_affine_preimage(Pointer ph, SizeT var, Pointer lb, Pointer ub, Pointer d);

    static native int ppl_Polyhedron_generalized_affine_image(Pointer ph, SizeT var, int relsym, Pointer le, Pointer d);

    static native int ppl_Polyhedron_generalized_affine_preimage(Pointer ph, SizeT var, int relsym, Pointer le,
            Pointer d);

    static native int ppl_Polyhedron_generalized_affine_image_lhs_rhs(Pointer ph, Pointer lhs, int relsym, Pointer rhs);

    static native int ppl_Polyhedron_generalized_affine_preimage_lhs_rhs(Pointer ph, Pointer lhs, int relsym,
            Pointer rhs);

    static native int ppl_Polyhedron_concatenate_assign(Pointer x, Pointer y);

    static native int ppl_Polyhedron_add_space_dimensions_and_embed(Pointer ph, SizeT d);

    static native int ppl_Polyhedron_add_space_dimensions_and_project(Pointer ph, SizeT d);

    static native int ppl_Polyhedron_remove_space_dimensions(Pointer ph, Pointer ds, SizeT n);

    static native int ppl_Polyhedron_remove_higher_space_dimensions(Pointer ph, SizeT d);

    static native int ppl_Polyhedron_map_space_dimensions(Pointer ph, Pointer maps, SizeT n);

    static native int ppl_Polyhedron_expand_space_dimension(Pointer ph, SizeT d, SizeT m);

    static native int ppl_Polyhedron_fold_space_dimensions(Pointer ph, Pointer ds, SizeT n, SizeT d);

    static native int ppl_io_print_Polyhedron(Pointer x);

    static native int ppl_io_fprint_Polyhedron(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Polyhedron(PointerByReference strp, Pointer x);

    static native int ppl_Polyhedron_ascii_dump(Pointer x, Pointer stream);

    static native int ppl_Polyhedron_ascii_load(Pointer x, Pointer stream);

    // Polyhedron specific operations

    static native int ppl_Polyhedron_get_generators(Pointer ph, PointerByReference pgs);

    static native int ppl_Polyhedron_get_minimized_generators(Pointer ph, PointerByReference pgs);

    static native int ppl_Polyhedron_add_generator(Pointer ph, Pointer g);

    static native int ppl_Polyhedron_add_generators(Pointer ph, Pointer gs);

    static native int ppl_Polyhedron_add_recycled_generators(Pointer ph, Pointer gs);

    static native int ppl_Polyhedron_poly_hull_assign(Pointer x, Pointer y);

    static native int ppl_Polyhedron_poly_difference_assign(Pointer x, Pointer y);

    //static native int wrap_assign (Pointer ph, Pointer ds, Dimension n, int w, int r, int o, PointerByReference pcs, int complexity_threshold, int wrap_individually);

    static native int ppl_Polyhedron_BHRZ03_widening_assign_with_tokens(Pointer x, Pointer y, IntByReference tp);

    static native int ppl_Polyhedron_H79_widening_assign_with_tokens(Pointer x, Pointer y, IntByReference tp);

    static native int ppl_Polyhedron_BHRZ03_widening_assign(Pointer x, Pointer y);

    static native int ppl_Polyhedron_H79_widening_assign(Pointer x, Pointer y);

    static native int ppl_Polyhedron_limited_BHRZ03_extrapolation_assign_with_tokens(Pointer x, Pointer y, Pointer cs,
            IntByReference tp);

    static native int ppl_Polyhedron_limited_H79_extrapolation_assign_with_tokens(Pointer x, Pointer y, Pointer cs,
            IntByReference tp);

    static native int ppl_Polyhedron_limited_BHRZ03_extrapolation_assign(Pointer x, Pointer y, Pointer cs);

    static native int ppl_Polyhedron_limited_H79_extrapolation_assign(Pointer x, Pointer y, Pointer cs);

    static native int ppl_Polyhedron_bounded_BHRZ03_extrapolation_assign_with_tokens(Pointer x, Pointer y, Pointer cs,
            IntByReference tp);

    static native int ppl_Polyhedron_bounded_BHRZ03_extrapolation_assign(Pointer x, Pointer y, Pointer cs);

    static native int ppl_Polyhedron_bounded_H79_extrapolation_assign_with_tokens(Pointer x, Pointer y, Pointer cs,
            IntByReference tp);

    static native int ppl_Polyhedron_bounded_H79_extrapolation_assign(Pointer x, Pointer y, Pointer cs);

    // DoubleBox

    static native int ppl_new_Double_Box_from_space_dimension(PointerByReference pph, SizeT d, int empty);

    static native int ppl_new_Double_Box_from_Double_Box(PointerByReference pph, Pointer ph);

    static native int ppl_new_Double_Box_from_Double_Box_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    static native int ppl_new_Double_Box_from_Constraint_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_Double_Box_recycle_Constraint_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_Double_Box_from_Congruence_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_Double_Box_recycle_Congruence_System(PointerByReference pph, Pointer cs);

    static native int ppl_new_Double_Box_from_Generator_System(PointerByReference pph, Pointer gs);

    static native int ppl_new_Double_Box_recycle_Generator_System(PointerByReference pph, Pointer gs);

    static native int ppl_assign_Double_Box_from_Double_Box(Pointer dst, Pointer src);

    static native int ppl_new_Double_Box_from_C_Polyhedron(PointerByReference pph, Pointer ph);

    static native int ppl_new_Double_Box_from_C_Polyhedron_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    static native int ppl_new_Double_Box_from_NNC_Polyhedron(PointerByReference pph, Pointer ph);

    static native int ppl_new_Double_Box_from_NNC_Polyhedron_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    static native int ppl_delete_Double_Box(Pointer ph);

    static native int ppl_Double_Box_space_dimension(Pointer ph, SizeTByReference m);

    static native int ppl_Double_Box_affine_dimension(Pointer ph, SizeTByReference m);

    static native int ppl_Double_Box_relation_with_Constraint(Pointer ph, Pointer c);

    static native int ppl_Double_Box_relation_with_Generator(Pointer ph, Pointer g);

    static native int ppl_Double_Box_get_constraints(Pointer ph, PointerByReference pcs);

    static native int ppl_Double_Box_get_congruences(Pointer ph, PointerByReference pcs);

    static native int ppl_Double_Box_get_minimized_constraints(Pointer ph, PointerByReference pcs);

    static native int ppl_Double_Box_get_minimized_congruences(Pointer ph, PointerByReference pcs);

    static native int ppl_Double_Box_is_empty(Pointer ph);

    static native int ppl_Double_Box_is_universe(Pointer ph);

    static native int ppl_Double_Box_is_bounded(Pointer ph);

    static native int ppl_Double_Box_contains_integer_point(Pointer ph);

    static native int ppl_Double_Box_is_topologically_closed(Pointer ph);

    static native int ppl_Double_Box_is_discrete(Pointer ph);

    static native int ppl_Double_Box_constrains(Pointer ph, SizeT var);

    static native int ppl_Double_Box_bounds_from_above(Pointer ph, Pointer le);

    static native int ppl_Double_Box_bounds_from_below(Pointer ph, Pointer le);

    static native int ppl_Double_Box_maximize_with_point(Pointer ph, Pointer le, Pointer sup_n, Pointer sup_d,
            IntByReference pmaximum, Pointer point);

    static native int ppl_Double_Box_maximize(Pointer ph, Pointer le, Pointer sup_n, Pointer sup_d,
            IntByReference pmaximum);

    static native int ppl_Double_Box_minimize_with_point(Pointer ph, Pointer le, Pointer inf_n, Pointer inf_d,
            IntByReference pminimum, Pointer point);

    static native int ppl_Double_Box_minimize(Pointer ph, Pointer le, Pointer inf_n, Pointer inf_d,
            IntByReference pminimum);

    static native int ppl_Double_Box_contains_Double_Box(Pointer x, Pointer y);

    static native int ppl_Double_Box_strictly_contains_Double_Box(Pointer x, Pointer y);

    static native int ppl_Double_Box_is_disjoint_from_Double_Box(Pointer x, Pointer y);

    static native int ppl_Double_Box_equals_Double_Box(Pointer x, Pointer y);

    static native int ppl_Double_Box_OK(Pointer ph);

    static native int ppl_Double_Box_external_memory_in_bytes(Pointer ph, SizeTByReference sz);

    static native int ppl_Double_Box_total_memory_in_bytes(Pointer ph, SizeTByReference sz);

    static native int ppl_Double_Box_add_constraint(Pointer ph, Pointer c);

    static native int ppl_Double_Box_add_congruence(Pointer ph, Pointer c);

    static native int ppl_Double_Box_add_constraints(Pointer ph, Pointer cs);

    static native int ppl_Double_Box_add_congruences(Pointer ph, Pointer cs);

    static native int ppl_Double_Box_add_recycled_constraints(Pointer ph, Pointer cs);

    static native int ppl_Double_Box_add_recycled_congruences(Pointer ph, Pointer cs);

    static native int ppl_Double_Box_refine_with_constraint(Pointer ph, Pointer c);

    static native int ppl_Double_Box_refine_with_congruence(Pointer ph, Pointer c);

    static native int ppl_Double_Box_refine_with_constraints(Pointer ph, Pointer cs);

    static native int ppl_Double_Box_refine_with_congruences(Pointer ph, Pointer cs);

    static native int ppl_Double_Box_intersection_assign(Pointer x, Pointer y);

    static native int ppl_Double_Box_upper_bound_assign(Pointer x, Pointer y);

    static native int ppl_Double_Box_difference_assign(Pointer x, Pointer y);

    static native int ppl_Double_Box_simplify_using_context_assign(Pointer x, Pointer y);

    static native int ppl_Double_Box_time_elapse_assign(Pointer x, Pointer y);

    static native int ppl_Double_Box_topological_closure_assign(Pointer ph);

    static native int ppl_Double_Box_unconstrain_space_dimension(Pointer ph, SizeT var);

    static native int ppl_Double_Box_unconstrain_space_dimensions(Pointer ph, Pointer ds, SizeT n);

    static native int ppl_Double_Box_affine_image(Pointer ph, SizeT var, Pointer le, Pointer d);

    static native int ppl_Double_Box_affine_preimage(Pointer ph, SizeT var, Pointer le, Pointer d);

    static native int ppl_Double_Box_bounded_affine_image(Pointer ph, SizeT var, Pointer lb, Pointer ub, Pointer d);

    static native int ppl_Double_Box_bounded_affine_preimage(Pointer ph, SizeT var, Pointer lb, Pointer ub, Pointer d);

    static native int ppl_Double_Box_generalized_affine_image(Pointer ph, SizeT var, int relsym, Pointer le, Pointer d);

    static native int ppl_Double_Box_generalized_affine_preimage(Pointer ph, SizeT var, int relsym, Pointer le,
            Pointer d);

    static native int ppl_Double_Box_generalized_affine_image_lhs_rhs(Pointer ph, Pointer lhs, int relsym, Pointer rhs);

    static native int ppl_Double_Box_generalized_affine_preimage_lhs_rhs(Pointer ph, Pointer lhs, int relsym,
            Pointer rhs);

    static native int ppl_Double_Box_concatenate_assign(Pointer x, Pointer y);

    static native int ppl_Double_Box_add_space_dimensions_and_embed(Pointer ph, SizeT d);

    static native int ppl_Double_Box_add_space_dimensions_and_project(Pointer ph, SizeT d);

    static native int ppl_Double_Box_remove_space_dimensions(Pointer ph, Pointer ds, SizeT n);

    static native int ppl_Double_Box_remove_higher_space_dimensions(Pointer ph, SizeT d);

    static native int ppl_Double_Box_map_space_dimensions(Pointer ph, Pointer maps, SizeT n);

    static native int ppl_Double_Box_expand_space_dimension(Pointer ph, SizeT d, SizeT m);

    static native int ppl_Double_Box_fold_space_dimensions(Pointer ph, Pointer ds, SizeT n, SizeT d);

    static native int ppl_io_print_Double_Box(Pointer x);

    static native int ppl_io_fprint_Double_Box(Pointer stream, Pointer x);

    static native int ppl_io_asprint_Double_Box(PointerByReference strp, Pointer x);

    static native int ppl_Double_Box_ascii_dump(Pointer x, Pointer stream);

    static native int ppl_Double_Box_ascii_load(Pointer x, Pointer stream);

}
