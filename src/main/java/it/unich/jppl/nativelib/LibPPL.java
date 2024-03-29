package it.unich.jppl.nativelib;

import it.unich.jppl.PPL;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * This class contains all native methods of the PPL library.
 *
 * <p>
 * Unless you want to interface with native code, you may completely ignore this
 * class.
 * </p>
 */
public class LibPPL {

    /**
     * We declare the default constructor to be private, since this class should
     * never be instantiated.
     */
    private LibPPL() {
    }

    /**
     * Name of the native PPL library.
     */
    public static final String LIBNAME = "ppl_c";

    static {
        Native.register(LIBNAME);
        PPL.pplInitialize();
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

    public static native int ppl_set_error_handler(PPLErrorHandler h);

    // Timeout handling

    public static native int ppl_set_timeout(int csecs);

    public static native int ppl_reset_timeout();

    public static native int ppl_set_deterministic_timeout(NativeLong unscaled_weight, int scale);

    public static native int ppl_reset_deterministic_timeout();

    // Dimensions

    public static native int ppl_max_space_dimension(SizeTByReference m);

    public static native int ppl_not_a_dimension(SizeTByReference m);

    public static native int ppl_io_print_variable(SizeT var);

    public static native int ppl_io_fprint_variable(Pointer stream, SizeT var);

    public static native int ppl_io_asprint_variable(PointerByReference strp, SizeT var);

    public static native int ppl_io_set_variable_output_function(VariableOutputFunction p);

    public static native int ppl_io_get_variable_output_function(VariableOutputFunctionByReference pp);

    public static native Pointer ppl_io_wrap_string(String src, int indent_depth, int preferred_first_line_length,
            int preferred_line_length);

    // Coefficients

    public static native int ppl_new_Coefficient(PointerByReference pc);

    public static native int ppl_new_Coefficient_from_mpz_t(PointerByReference pc, Pointer z);

    public static native int ppl_new_Coefficient_from_Coefficient(PointerByReference pc, Pointer c);

    public static native int ppl_assign_Coefficient_from_mpz_t(Pointer dst, Pointer z);

    public static native int ppl_assign_Coefficient_from_Coefficient(Pointer dst, Pointer src);

    public static native int ppl_delete_Coefficient(Pointer c);

    public static native int ppl_Coefficient_to_mpz_t(Pointer c, Pointer z);

    public static native int ppl_Coefficient_OK(Pointer c);

    public static native int ppl_Coefficient_is_bounded();

    public static native int ppl_Coefficient_min(Pointer min);

    public static native int ppl_Coefficient_max(Pointer max);

    public static native int ppl_io_print_Coefficient(Pointer x);

    public static native int ppl_io_fprint_Coefficient(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Coefficient(PointerByReference strp, Pointer x);

    // Linear Expressions

    public static native int ppl_new_Linear_Expression(PointerByReference ple);

    public static native int ppl_new_Linear_Expression_with_dimension(PointerByReference ple, SizeT d);

    public static native int ppl_new_Linear_Expression_from_Linear_Expression(PointerByReference ple, Pointer le);

    public static native int ppl_new_Linear_Expression_from_Constraint(PointerByReference ple, Pointer c);

    public static native int ppl_new_Linear_Expression_from_Generator(PointerByReference ple, Pointer g);

    public static native int ppl_new_Linear_Expression_from_Congruence(PointerByReference ple, Pointer c);

    //public static native int ppl_new_Linear_Expression_from_Grid_Generator (PointerByReference ple, Pointer g);

    public static native int ppl_assign_Linear_Expression_from_Linear_Expression(Pointer dst, Pointer src);

    public static native int ppl_delete_Linear_Expression(Pointer le);

    public static native int ppl_Linear_Expression_space_dimension(Pointer le, SizeTByReference m);

    public static native int ppl_Linear_Expression_coefficient(Pointer le, SizeT var, Pointer n);

    public static native int ppl_Linear_Expression_inhomogeneous_term(Pointer le, Pointer n);

    public static native int ppl_Linear_Expression_OK(Pointer le);

    public static native int ppl_Linear_Expression_is_zero(Pointer le);

    public static native int ppl_Linear_Expression_all_homogeneous_terms_are_zero(Pointer le);

    public static native int ppl_Linear_Expression_add_to_coefficient(Pointer le, SizeT var, Pointer n);

    public static native int ppl_Linear_Expression_add_to_inhomogeneous(Pointer le, Pointer n);

    public static native int ppl_add_Linear_Expression_to_Linear_Expression(Pointer dst, Pointer src);

    public static native int ppl_multiply_Linear_Expression_by_Coefficient(Pointer le, Pointer n);

    public static native int ppl_io_print_Linear_Expression(Pointer x);

    public static native int ppl_io_fprint_Linear_Expression(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Linear_Expression(PointerByReference strp, Pointer x);

    public static native int ppl_Linear_Expression_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Linear_Expression_ascii_load(Pointer x, Pointer steam);

    // Constraint

    public static native int ppl_new_Constraint(PointerByReference pc, Pointer le, int rel);

    public static native int ppl_new_Constraint_zero_dim_false(PointerByReference pc);

    public static native int ppl_new_Constraint_zero_dim_positivity(PointerByReference pc);

    public static native int ppl_new_Constraint_from_Constraint(PointerByReference pc, Pointer c);

    public static native int ppl_assign_Constraint_from_Constraint(Pointer dst, Pointer src);

    public static native int ppl_delete_Constraint(Pointer c);

    public static native int ppl_Constraint_space_dimension(Pointer c, SizeTByReference m);

    public static native int ppl_Constraint_type(Pointer c);

    public static native int ppl_Constraint_coefficient(Pointer c, SizeT var, Pointer n);

    public static native int ppl_Constraint_inhomogeneous_term(Pointer c, Pointer n);

    public static native int ppl_Constraint_OK(Pointer c);

    public static native int ppl_io_print_Constraint(Pointer x);

    public static native int ppl_io_fprint_Constraint(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Constraint(PointerByReference strp, Pointer x);

    public static native int ppl_Constraint_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Constraint_ascii_load(Pointer x, Pointer steam);

    // Constraint System

    public static native int ppl_new_Constraint_System(PointerByReference pcs);

    public static native int ppl_new_Constraint_System_zero_dim_empty(PointerByReference pcs);

    public static native int ppl_new_Constraint_System_from_Constraint(PointerByReference pcs, Pointer c);

    public static native int ppl_new_Constraint_System_from_Constraint_System(PointerByReference pcs, Pointer cs);

    public static native int ppl_assign_Constraint_System_from_Constraint_System(Pointer dst, Pointer src);

    public static native int ppl_delete_Constraint_System(Pointer cs);

    public static native int ppl_Constraint_System_space_dimension(Pointer cs, SizeTByReference m);

    public static native int ppl_Constraint_System_empty(Pointer cs);

    public static native int ppl_Constraint_System_has_strict_inequalities(Pointer cs);

    public static native int ppl_Constraint_System_begin(Pointer cs, Pointer cit);

    public static native int ppl_Constraint_System_end(Pointer cs, Pointer cit);

    public static native int ppl_Constraint_System_OK(Pointer cs);

    public static native int ppl_Constraint_System_clear(Pointer cs);

    public static native int ppl_Constraint_System_insert_Constraint(Pointer cs, Pointer c);

    public static native int ppl_io_print_Constraint_System(Pointer x);

    public static native int ppl_io_fprint_Constraint_System(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Constraint_System(PointerByReference strp, Pointer x);

    public static native int ppl_Constraint_System_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Constraint_System_ascii_load(Pointer x, Pointer steam);

    // Constraint System Iterator

    public static native int ppl_new_Constraint_System_const_iterator(PointerByReference pcit);

    public static native int ppl_new_Constraint_System_const_iterator_from_Constraint_System_const_iterator(
            PointerByReference pcit, Pointer cit);

    public static native int ppl_assign_Constraint_System_const_iterator_from_Constraint_System_const_iterator(
            Pointer dst, Pointer src);

    public static native int ppl_delete_Constraint_System_const_iterator(Pointer cit);

    public static native int ppl_Constraint_System_const_iterator_dereference(Pointer cit, PointerByReference pc);

    public static native int ppl_Constraint_System_const_iterator_increment(Pointer cit);

    public static native int ppl_Constraint_System_const_iterator_equal_test(Pointer x, Pointer y);

    // Generator

    public static native int ppl_new_Generator(PointerByReference pg, Pointer le, int t, Pointer d);

    public static native int ppl_new_Generator_zero_dim_point(PointerByReference pg);

    public static native int ppl_new_Generator_zero_dim_closure_point(PointerByReference pg);

    public static native int ppl_new_Generator_from_Generator(PointerByReference pg, Pointer g);

    public static native int ppl_assign_Generator_from_Generator(Pointer dst, Pointer src);

    public static native int ppl_delete_Generator(Pointer g);

    public static native int ppl_Generator_space_dimension(Pointer g, SizeTByReference m);

    public static native int ppl_Generator_type(Pointer g);

    public static native int ppl_Generator_coefficient(Pointer g, SizeT var, Pointer n);

    public static native int ppl_Generator_divisor(Pointer g, Pointer d);

    public static native int ppl_Generator_OK(Pointer g);

    public static native int ppl_io_print_Generator(Pointer x);

    public static native int ppl_io_fprint_Generator(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Generator(PointerByReference strp, Pointer x);

    public static native int ppl_Generator_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Generator_ascii_load(Pointer x, Pointer steam);

    // Generator System

    public static native int ppl_new_Generator_System(PointerByReference pgs);

    public static native int ppl_new_Generator_System_from_Generator(PointerByReference pgs, Pointer g);

    public static native int ppl_new_Generator_System_from_Generator_System(PointerByReference pgs, Pointer gs);

    public static native int ppl_assign_Generator_System_from_Generator_System(Pointer dst, Pointer src);

    public static native int ppl_delete_Generator_System(Pointer gs);

    public static native int ppl_Generator_System_space_dimension(Pointer gs, SizeTByReference m);

    public static native int ppl_Generator_System_empty(Pointer gs);

    public static native int ppl_Generator_System_begin(Pointer gs, Pointer cit);

    public static native int ppl_Generator_System_end(Pointer gs, Pointer cit);

    public static native int ppl_Generator_System_OK(Pointer gs);

    public static native int ppl_Generator_System_clear(Pointer gs);

    public static native int ppl_Generator_System_insert_Generator(Pointer gs, Pointer g);

    public static native int ppl_io_print_Generator_System(Pointer x);

    public static native int ppl_io_fprint_Generator_System(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Generator_System(PointerByReference strp, Pointer x);

    public static native int ppl_Generator_System_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Generator_System_ascii_load(Pointer x, Pointer steam);

    // Generator System Iterator

    public static native int ppl_new_Generator_System_const_iterator(PointerByReference pcit);

    public static native int ppl_new_Generator_System_const_iterator_from_Generator_System_const_iterator(
            PointerByReference pcit, Pointer cit);

    public static native int ppl_assign_Generator_System_const_iterator_from_Generator_System_const_iterator(
            Pointer dst, Pointer src);

    public static native int ppl_delete_Generator_System_const_iterator(Pointer cit);

    public static native int ppl_Generator_System_const_iterator_dereference(Pointer cit, PointerByReference pc);

    public static native int ppl_Generator_System_const_iterator_increment(Pointer cit);

    public static native int ppl_Generator_System_const_iterator_equal_test(Pointer x, Pointer y);

    // Congruence

    public static native int ppl_new_Congruence(PointerByReference pc, Pointer le, Pointer m);

    public static native int ppl_new_Congruence_zero_dim_false(PointerByReference pc);

    public static native int ppl_new_Congruence_zero_dim_integrality(PointerByReference pc);

    public static native int ppl_new_Congruence_from_Congruence(PointerByReference pc, Pointer c);

    public static native int ppl_assign_Congruence_from_Congruence(Pointer dst, Pointer src);

    public static native int ppl_delete_Congruence(Pointer c);

    public static native int ppl_Congruence_space_dimension(Pointer c, SizeTByReference m);

    public static native int ppl_Congruence_coefficient(Pointer c, SizeT var, Pointer n);

    public static native int ppl_Congruence_inhomogeneous_term(Pointer c, Pointer n);

    public static native int ppl_Congruence_modulus(Pointer c, Pointer n);

    public static native int ppl_Congruence_OK(Pointer c);

    public static native int ppl_io_print_Congruence(Pointer x);

    public static native int ppl_io_fprint_Congruence(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Congruence(PointerByReference strp, Pointer x);

    public static native int ppl_Congruence_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Congruence_ascii_load(Pointer x, Pointer steam);

    // Congruence System

    public static native int ppl_new_Congruence_System(PointerByReference pcs);

    public static native int ppl_new_Congruence_System_zero_dim_empty(PointerByReference pcs);

    public static native int ppl_new_Congruence_System_from_Congruence(PointerByReference pcs, Pointer c);

    public static native int ppl_new_Congruence_System_from_Congruence_System(PointerByReference pcs, Pointer cs);

    public static native int ppl_assign_Congruence_System_from_Congruence_System(Pointer dst, Pointer src);

    public static native int ppl_delete_Congruence_System(Pointer cs);

    public static native int ppl_Congruence_System_space_dimension(Pointer cs, SizeTByReference m);

    public static native int ppl_Congruence_System_empty(Pointer cs);

    public static native int ppl_Congruence_System_begin(Pointer cs, Pointer git);

    public static native int ppl_Congruence_System_end(Pointer cs, Pointer git);

    public static native int ppl_Congruence_System_OK(Pointer cs);

    public static native int ppl_Congruence_System_clear(Pointer cs);

    public static native int ppl_Congruence_System_insert_Congruence(Pointer cs, Pointer c);

    public static native int ppl_io_print_Congruence_System(Pointer x);

    public static native int ppl_io_fprint_Congruence_System(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Congruence_System(PointerByReference strp, Pointer x);

    public static native int ppl_Congruence_System_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Congruence_System_ascii_load(Pointer x, Pointer steam);

    // Congruence System Iterator

    public static native int ppl_new_Congruence_System_const_iterator(PointerByReference pgit);

    public static native int ppl_new_Congruence_System_const_iterator_from_Congruence_System_const_iterator(
            PointerByReference pgit, Pointer git);

    public static native int ppl_assign_Congruence_System_const_iterator_from_Congruence_System_const_iterator(
            Pointer dst, Pointer src);

    public static native int ppl_delete_Congruence_System_const_iterator(Pointer git);

    public static native int ppl_Congruence_System_const_iterator_dereference(Pointer git, PointerByReference pg);

    public static native int ppl_Congruence_System_const_iterator_increment(Pointer git);

    public static native int ppl_Congruence_System_const_iterator_equal_test(Pointer x, Pointer y);

    // Grid Generator

    public static native int ppl_new_Grid_Generator(PointerByReference pg, Pointer le, int t, Pointer d);

    public static native int ppl_new_Grid_Generator_zero_dim_point(PointerByReference pg);

    public static native int ppl_new_Grid_Generator_from_Grid_Generator(PointerByReference pg, Pointer g);

    public static native int ppl_assign_Grid_Generator_from_Grid_Generator(Pointer dst, Pointer src);

    public static native int ppl_delete_Grid_Generator(Pointer g);

    public static native int ppl_Grid_Generator_space_dimension(Pointer g, SizeTByReference m);

    public static native int ppl_Grid_Generator_type(Pointer g);

    public static native int ppl_Grid_Generator_coefficient(Pointer g, SizeT var, Pointer n);

    public static native int ppl_Grid_Generator_divisor(Pointer g, Pointer d);

    public static native int ppl_Grid_Generator_OK(Pointer g);

    public static native int ppl_io_print_Grid_Generator(Pointer x);

    public static native int ppl_io_fprint_Grid_Generator(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Grid_Generator(PointerByReference strp, Pointer x);

    public static native int ppl_Grid_Generator_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Grid_Generator_ascii_load(Pointer x, Pointer steam);

    // Grid Generator System

    public static native int ppl_new_Grid_Generator_System(PointerByReference pgs);

    public static native int ppl_new_Grid_Generator_System_from_Grid_Generator(PointerByReference pgs, Pointer g);

    public static native int ppl_new_Grid_Generator_System_from_Grid_Generator_System(PointerByReference pgs,
            Pointer gs);

    public static native int ppl_assign_Grid_Generator_System_from_Grid_Generator_System(Pointer dst, Pointer src);

    public static native int ppl_delete_Grid_Generator_System(Pointer gs);

    public static native int ppl_Grid_Generator_System_space_dimension(Pointer gs, SizeTByReference m);

    public static native int ppl_Grid_Generator_System_empty(Pointer gs);

    public static native int ppl_Grid_Generator_System_begin(Pointer gs, Pointer git);

    public static native int ppl_Grid_Generator_System_end(Pointer gs, Pointer git);

    public static native int ppl_Grid_Generator_System_OK(Pointer gs);

    public static native int ppl_Grid_Generator_System_clear(Pointer gs);

    public static native int ppl_Grid_Generator_System_insert_Grid_Generator(Pointer gs, Pointer g);

    public static native int ppl_io_print_Grid_Generator_System(Pointer x);

    public static native int ppl_io_fprint_Grid_Generator_System(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Grid_Generator_System(PointerByReference strp, Pointer x);

    public static native int ppl_Grid_Generator_System_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Grid_Generator_System_ascii_load(Pointer x, Pointer steam);

    // Grid Generator System Iterator

    public static native int ppl_new_Grid_Generator_System_const_iterator(PointerByReference pgit);

    public static native int ppl_new_Grid_Generator_System_const_iterator_from_Grid_Generator_System_const_iterator(
            PointerByReference pgit, Pointer git);

    public static native int ppl_assign_Grid_Generator_System_const_iterator_from_Grid_Generator_System_const_iterator(
            Pointer dst, Pointer src);

    public static native int ppl_delete_Grid_Generator_System_const_iterator(Pointer git);

    public static native int ppl_Grid_Generator_System_const_iterator_dereference(Pointer git, PointerByReference pg);

    public static native int ppl_Grid_Generator_System_const_iterator_increment(Pointer git);

    public static native int ppl_Grid_Generator_System_const_iterator_equal_test(Pointer x, Pointer y);

    // CPolyhedron

    public static native int ppl_new_C_Polyhedron_from_space_dimension(PointerByReference pph, SizeT d, int empty);

    public static native int ppl_new_C_Polyhedron_from_C_Polyhedron(PointerByReference pph, Pointer ph);

    public static native int ppl_new_C_Polyhedron_from_C_Polyhedron_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    public static native int ppl_new_C_Polyhedron_from_Constraint_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_C_Polyhedron_recycle_Constraint_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_C_Polyhedron_from_Congruence_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_C_Polyhedron_recycle_Congruence_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_C_Polyhedron_from_Generator_System(PointerByReference pph, Pointer gs);

    public static native int ppl_new_C_Polyhedron_recycle_Generator_System(PointerByReference pph, Pointer gs);

    public static native int ppl_assign_C_Polyhedron_from_C_Polyhedron(Pointer dst, Pointer src);

    public static native int ppl_new_C_Polyhedron_from_NNC_Polyhedron(PointerByReference pph, Pointer ph);

    public static native int ppl_new_C_Polyhedron_from_NNC_Polyhedron_with_complexity(PointerByReference pph,
            Pointer ph, int complexity);

    public static native int ppl_new_C_Polyhedron_from_Double_Box(PointerByReference pph, Pointer ph);

    public static native int ppl_new_C_Polyhedron_from_Double_Box_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    // NNCPolyhedron

    public static native int ppl_new_NNC_Polyhedron_from_space_dimension(PointerByReference pph, SizeT d, int empty);

    public static native int ppl_new_NNC_Polyhedron_from_NNC_Polyhedron(PointerByReference pph, Pointer ph);

    public static native int ppl_new_NNC_Polyhedron_from_NNC_Polyhedron_with_complexity(PointerByReference pph,
            Pointer ph, int complexity);

    public static native int ppl_new_NNC_Polyhedron_from_Constraint_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_NNC_Polyhedron_recycle_Constraint_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_NNC_Polyhedron_from_Congruence_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_NNC_Polyhedron_recycle_Congruence_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_NNC_Polyhedron_from_Generator_System(PointerByReference pph, Pointer gs);

    public static native int ppl_new_NNC_Polyhedron_recycle_Generator_System(PointerByReference pph, Pointer gs);

    public static native int ppl_assign_NNC_Polyhedron_from_NNC_Polyhedron(Pointer dst, Pointer src);

    public static native int ppl_new_NNC_Polyhedron_from_C_Polyhedron(PointerByReference pph, Pointer ph);

    public static native int ppl_new_NNC_Polyhedron_from_C_Polyhedron_with_complexity(PointerByReference pph,
            Pointer ph, int complexity);

    public static native int ppl_new_NNC_Polyhedron_from_Double_Box(PointerByReference pph, Pointer ph);

    public static native int ppl_new_NNC_Polyhedron_from_Double_Box_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    // Polyhedron

    public static native int ppl_delete_Polyhedron(Pointer ph);

    public static native int ppl_Polyhedron_space_dimension(Pointer ph, SizeTByReference m);

    public static native int ppl_Polyhedron_affine_dimension(Pointer ph, SizeTByReference m);

    public static native int ppl_Polyhedron_relation_with_Constraint(Pointer ph, Pointer c);

    public static native int ppl_Polyhedron_relation_with_Generator(Pointer ph, Pointer g);

    public static native int ppl_Polyhedron_get_constraints(Pointer ph, PointerByReference pcs);

    public static native int ppl_Polyhedron_get_congruences(Pointer ph, PointerByReference pcs);

    public static native int ppl_Polyhedron_get_minimized_constraints(Pointer ph, PointerByReference pcs);

    public static native int ppl_Polyhedron_get_minimized_congruences(Pointer ph, PointerByReference pcs);

    public static native int ppl_Polyhedron_is_empty(Pointer ph);

    public static native int ppl_Polyhedron_is_universe(Pointer ph);

    public static native int ppl_Polyhedron_is_bounded(Pointer ph);

    public static native int ppl_Polyhedron_contains_integer_point(Pointer ph);

    public static native int ppl_Polyhedron_is_topologically_closed(Pointer ph);

    public static native int ppl_Polyhedron_is_discrete(Pointer ph);

    public static native int ppl_Polyhedron_constrains(Pointer ph, SizeT var);

    public static native int ppl_Polyhedron_bounds_from_above(Pointer ph, Pointer le);

    public static native int ppl_Polyhedron_bounds_from_below(Pointer ph, Pointer le);

    public static native int ppl_Polyhedron_maximize_with_point(Pointer ph, Pointer le, Pointer sup_n, Pointer sup_d,
            IntByReference pmaximum, Pointer point);

    public static native int ppl_Polyhedron_maximize(Pointer ph, Pointer le, Pointer sup_n, Pointer sup_d,
            IntByReference pmaximum);

    public static native int ppl_Polyhedron_minimize_with_point(Pointer ph, Pointer le, Pointer inf_n, Pointer inf_d,
            IntByReference pminimum, Pointer point);

    public static native int ppl_Polyhedron_minimize(Pointer ph, Pointer le, Pointer inf_n, Pointer inf_d,
            IntByReference pminimum);

    public static native int ppl_Polyhedron_contains_Polyhedron(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_strictly_contains_Polyhedron(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_is_disjoint_from_Polyhedron(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_equals_Polyhedron(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_OK(Pointer ph);

    public static native int ppl_Polyhedron_external_memory_in_bytes(Pointer ph, SizeTByReference sz);

    public static native int ppl_Polyhedron_total_memory_in_bytes(Pointer ph, SizeTByReference sz);

    public static native int ppl_Polyhedron_add_constraint(Pointer ph, Pointer c);

    public static native int ppl_Polyhedron_add_congruence(Pointer ph, Pointer c);

    public static native int ppl_Polyhedron_add_constraints(Pointer ph, Pointer cs);

    public static native int ppl_Polyhedron_add_congruences(Pointer ph, Pointer cs);

    public static native int ppl_Polyhedron_add_recycled_constraints(Pointer ph, Pointer cs);

    public static native int ppl_Polyhedron_add_recycled_congruences(Pointer ph, Pointer cs);

    public static native int ppl_Polyhedron_refine_with_constraint(Pointer ph, Pointer c);

    public static native int ppl_Polyhedron_refine_with_congruence(Pointer ph, Pointer c);

    public static native int ppl_Polyhedron_refine_with_constraints(Pointer ph, Pointer cs);

    public static native int ppl_Polyhedron_refine_with_congruences(Pointer ph, Pointer cs);

    public static native int ppl_Polyhedron_intersection_assign(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_upper_bound_assign(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_difference_assign(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_simplify_using_context_assign(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_time_elapse_assign(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_topological_closure_assign(Pointer ph);

    public static native int ppl_Polyhedron_unconstrain_space_dimension(Pointer ph, SizeT var);

    public static native int ppl_Polyhedron_unconstrain_space_dimensions(Pointer ph, Pointer ds, SizeT n);

    public static native int ppl_Polyhedron_affine_image(Pointer ph, SizeT var, Pointer le, Pointer d);

    public static native int ppl_Polyhedron_affine_preimage(Pointer ph, SizeT var, Pointer le, Pointer d);

    public static native int ppl_Polyhedron_bounded_affine_image(Pointer ph, SizeT var, Pointer lb, Pointer ub,
            Pointer d);

    public static native int ppl_Polyhedron_bounded_affine_preimage(Pointer ph, SizeT var, Pointer lb, Pointer ub,
            Pointer d);

    public static native int ppl_Polyhedron_generalized_affine_image(Pointer ph, SizeT var, int relsym, Pointer le,
            Pointer d);

    public static native int ppl_Polyhedron_generalized_affine_preimage(Pointer ph, SizeT var, int relsym, Pointer le,
            Pointer d);

    public static native int ppl_Polyhedron_generalized_affine_image_lhs_rhs(Pointer ph, Pointer lhs, int relsym,
            Pointer rhs);

    public static native int ppl_Polyhedron_generalized_affine_preimage_lhs_rhs(Pointer ph, Pointer lhs, int relsym,
            Pointer rhs);

    public static native int ppl_Polyhedron_concatenate_assign(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_add_space_dimensions_and_embed(Pointer ph, SizeT d);

    public static native int ppl_Polyhedron_add_space_dimensions_and_project(Pointer ph, SizeT d);

    public static native int ppl_Polyhedron_remove_space_dimensions(Pointer ph, Pointer ds, SizeT n);

    public static native int ppl_Polyhedron_remove_higher_space_dimensions(Pointer ph, SizeT d);

    public static native int ppl_Polyhedron_map_space_dimensions(Pointer ph, Pointer maps, SizeT n);

    public static native int ppl_Polyhedron_expand_space_dimension(Pointer ph, SizeT d, SizeT m);

    public static native int ppl_Polyhedron_fold_space_dimensions(Pointer ph, Pointer ds, SizeT n, SizeT d);

    public static native int ppl_io_print_Polyhedron(Pointer x);

    public static native int ppl_io_fprint_Polyhedron(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Polyhedron(PointerByReference strp, Pointer x);

    public static native int ppl_Polyhedron_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Polyhedron_ascii_load(Pointer x, Pointer stream);

    // Polyhedron specific operations

    public static native int ppl_Polyhedron_get_generators(Pointer ph, PointerByReference pgs);

    public static native int ppl_Polyhedron_get_minimized_generators(Pointer ph, PointerByReference pgs);

    public static native int ppl_Polyhedron_add_generator(Pointer ph, Pointer g);

    public static native int ppl_Polyhedron_add_generators(Pointer ph, Pointer gs);

    public static native int ppl_Polyhedron_add_recycled_generators(Pointer ph, Pointer gs);

    public static native int ppl_Polyhedron_poly_hull_assign(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_poly_difference_assign(Pointer x, Pointer y);

    //public static native int wrap_assign (Pointer ph, Pointer ds, Dimension n, int w, int r, int o, PointerByReference pcs, int complexity_threshold, int wrap_individually);

    public static native int ppl_Polyhedron_BHRZ03_widening_assign_with_tokens(Pointer x, Pointer y, IntByReference tp);

    public static native int ppl_Polyhedron_H79_widening_assign_with_tokens(Pointer x, Pointer y, IntByReference tp);

    public static native int ppl_Polyhedron_BHRZ03_widening_assign(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_H79_widening_assign(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_limited_BHRZ03_extrapolation_assign_with_tokens(Pointer x, Pointer y,
            Pointer cs, IntByReference tp);

    public static native int ppl_Polyhedron_limited_H79_extrapolation_assign_with_tokens(Pointer x, Pointer y,
            Pointer cs, IntByReference tp);

    public static native int ppl_Polyhedron_limited_BHRZ03_extrapolation_assign(Pointer x, Pointer y, Pointer cs);

    public static native int ppl_Polyhedron_limited_H79_extrapolation_assign(Pointer x, Pointer y, Pointer cs);

    public static native int ppl_Polyhedron_bounded_BHRZ03_extrapolation_assign_with_tokens(Pointer x, Pointer y,
            Pointer cs, IntByReference tp);

    public static native int ppl_Polyhedron_bounded_BHRZ03_extrapolation_assign(Pointer x, Pointer y, Pointer cs);

    public static native int ppl_Polyhedron_bounded_H79_extrapolation_assign_with_tokens(Pointer x, Pointer y,
            Pointer cs, IntByReference tp);

    public static native int ppl_Polyhedron_bounded_H79_extrapolation_assign(Pointer x, Pointer y, Pointer cs);

    // DoubleBox

    public static native int ppl_new_Double_Box_from_space_dimension(PointerByReference pph, SizeT d, int empty);

    public static native int ppl_new_Double_Box_from_Double_Box(PointerByReference pph, Pointer ph);

    public static native int ppl_new_Double_Box_from_Double_Box_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    public static native int ppl_new_Double_Box_from_Constraint_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_Double_Box_recycle_Constraint_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_Double_Box_from_Congruence_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_Double_Box_recycle_Congruence_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_Double_Box_from_Generator_System(PointerByReference pph, Pointer gs);

    public static native int ppl_new_Double_Box_recycle_Generator_System(PointerByReference pph, Pointer gs);

    public static native int ppl_assign_Double_Box_from_Double_Box(Pointer dst, Pointer src);

    public static native int ppl_new_Double_Box_from_C_Polyhedron(PointerByReference pph, Pointer ph);

    public static native int ppl_new_Double_Box_from_C_Polyhedron_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    public static native int ppl_new_Double_Box_from_NNC_Polyhedron(PointerByReference pph, Pointer ph);

    public static native int ppl_new_Double_Box_from_NNC_Polyhedron_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    public static native int ppl_delete_Double_Box(Pointer ph);

    public static native int ppl_Double_Box_space_dimension(Pointer ph, SizeTByReference m);

    public static native int ppl_Double_Box_affine_dimension(Pointer ph, SizeTByReference m);

    public static native int ppl_Double_Box_relation_with_Constraint(Pointer ph, Pointer c);

    public static native int ppl_Double_Box_relation_with_Generator(Pointer ph, Pointer g);

    public static native int ppl_Double_Box_get_constraints(Pointer ph, PointerByReference pcs);

    public static native int ppl_Double_Box_get_congruences(Pointer ph, PointerByReference pcs);

    public static native int ppl_Double_Box_get_minimized_constraints(Pointer ph, PointerByReference pcs);

    public static native int ppl_Double_Box_get_minimized_congruences(Pointer ph, PointerByReference pcs);

    public static native int ppl_Double_Box_is_empty(Pointer ph);

    public static native int ppl_Double_Box_is_universe(Pointer ph);

    public static native int ppl_Double_Box_is_bounded(Pointer ph);

    public static native int ppl_Double_Box_contains_integer_point(Pointer ph);

    public static native int ppl_Double_Box_is_topologically_closed(Pointer ph);

    public static native int ppl_Double_Box_is_discrete(Pointer ph);

    public static native int ppl_Double_Box_constrains(Pointer ph, SizeT var);

    public static native int ppl_Double_Box_bounds_from_above(Pointer ph, Pointer le);

    public static native int ppl_Double_Box_bounds_from_below(Pointer ph, Pointer le);

    public static native int ppl_Double_Box_maximize_with_point(Pointer ph, Pointer le, Pointer sup_n, Pointer sup_d,
            IntByReference pmaximum, Pointer point);

    public static native int ppl_Double_Box_maximize(Pointer ph, Pointer le, Pointer sup_n, Pointer sup_d,
            IntByReference pmaximum);

    public static native int ppl_Double_Box_minimize_with_point(Pointer ph, Pointer le, Pointer inf_n, Pointer inf_d,
            IntByReference pminimum, Pointer point);

    public static native int ppl_Double_Box_minimize(Pointer ph, Pointer le, Pointer inf_n, Pointer inf_d,
            IntByReference pminimum);

    public static native int ppl_Double_Box_contains_Double_Box(Pointer x, Pointer y);

    public static native int ppl_Double_Box_strictly_contains_Double_Box(Pointer x, Pointer y);

    public static native int ppl_Double_Box_is_disjoint_from_Double_Box(Pointer x, Pointer y);

    public static native int ppl_Double_Box_equals_Double_Box(Pointer x, Pointer y);

    public static native int ppl_Double_Box_OK(Pointer ph);

    public static native int ppl_Double_Box_external_memory_in_bytes(Pointer ph, SizeTByReference sz);

    public static native int ppl_Double_Box_total_memory_in_bytes(Pointer ph, SizeTByReference sz);

    public static native int ppl_Double_Box_add_constraint(Pointer ph, Pointer c);

    public static native int ppl_Double_Box_add_congruence(Pointer ph, Pointer c);

    public static native int ppl_Double_Box_add_constraints(Pointer ph, Pointer cs);

    public static native int ppl_Double_Box_add_congruences(Pointer ph, Pointer cs);

    public static native int ppl_Double_Box_add_recycled_constraints(Pointer ph, Pointer cs);

    public static native int ppl_Double_Box_add_recycled_congruences(Pointer ph, Pointer cs);

    public static native int ppl_Double_Box_refine_with_constraint(Pointer ph, Pointer c);

    public static native int ppl_Double_Box_refine_with_congruence(Pointer ph, Pointer c);

    public static native int ppl_Double_Box_refine_with_constraints(Pointer ph, Pointer cs);

    public static native int ppl_Double_Box_refine_with_congruences(Pointer ph, Pointer cs);

    public static native int ppl_Double_Box_intersection_assign(Pointer x, Pointer y);

    public static native int ppl_Double_Box_upper_bound_assign(Pointer x, Pointer y);

    public static native int ppl_Double_Box_difference_assign(Pointer x, Pointer y);

    public static native int ppl_Double_Box_simplify_using_context_assign(Pointer x, Pointer y);

    public static native int ppl_Double_Box_time_elapse_assign(Pointer x, Pointer y);

    public static native int ppl_Double_Box_topological_closure_assign(Pointer ph);

    public static native int ppl_Double_Box_unconstrain_space_dimension(Pointer ph, SizeT var);

    public static native int ppl_Double_Box_unconstrain_space_dimensions(Pointer ph, Pointer ds, SizeT n);

    public static native int ppl_Double_Box_affine_image(Pointer ph, SizeT var, Pointer le, Pointer d);

    public static native int ppl_Double_Box_affine_preimage(Pointer ph, SizeT var, Pointer le, Pointer d);

    public static native int ppl_Double_Box_bounded_affine_image(Pointer ph, SizeT var, Pointer lb, Pointer ub,
            Pointer d);

    public static native int ppl_Double_Box_bounded_affine_preimage(Pointer ph, SizeT var, Pointer lb, Pointer ub,
            Pointer d);

    public static native int ppl_Double_Box_generalized_affine_image(Pointer ph, SizeT var, int relsym, Pointer le,
            Pointer d);

    public static native int ppl_Double_Box_generalized_affine_preimage(Pointer ph, SizeT var, int relsym, Pointer le,
            Pointer d);

    public static native int ppl_Double_Box_generalized_affine_image_lhs_rhs(Pointer ph, Pointer lhs, int relsym,
            Pointer rhs);

    public static native int ppl_Double_Box_generalized_affine_preimage_lhs_rhs(Pointer ph, Pointer lhs, int relsym,
            Pointer rhs);

    public static native int ppl_Double_Box_concatenate_assign(Pointer x, Pointer y);

    public static native int ppl_Double_Box_add_space_dimensions_and_embed(Pointer ph, SizeT d);

    public static native int ppl_Double_Box_add_space_dimensions_and_project(Pointer ph, SizeT d);

    public static native int ppl_Double_Box_remove_space_dimensions(Pointer ph, Pointer ds, SizeT n);

    public static native int ppl_Double_Box_remove_higher_space_dimensions(Pointer ph, SizeT d);

    public static native int ppl_Double_Box_map_space_dimensions(Pointer ph, Pointer maps, SizeT n);

    public static native int ppl_Double_Box_expand_space_dimension(Pointer ph, SizeT d, SizeT m);

    public static native int ppl_Double_Box_fold_space_dimensions(Pointer ph, Pointer ds, SizeT n, SizeT d);

    public static native int ppl_Double_Box_CC76_widening_assign_with_tokens(Pointer x, Pointer y, IntByReference tp);

    public static native int ppl_Double_Box_CC76_widening_assign(Pointer x, Pointer y);

    public static native int ppl_Double_Box_widening_assign_with_tokens(Pointer x, Pointer y, IntByReference tp);

    public static native int ppl_Double_Box_widening_assign(Pointer x, Pointer y);

    public static native int ppl_Double_Box_limited_CC76_extrapolation_assign_with_tokens(Pointer x, Pointer y,
            Pointer cs, IntByReference tp);

    public static native int ppl_Double_Box_limited_CC76_extrapolation_assign(Pointer x, Pointer y, Pointer cs);

    public static native int ppl_Double_Box_CC76_narrowing_assign(Pointer x, Pointer y);

    public static native int ppl_io_print_Double_Box(Pointer x);

    public static native int ppl_io_fprint_Double_Box(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Double_Box(PointerByReference strp, Pointer x);

    public static native int ppl_Double_Box_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Double_Box_ascii_load(Pointer x, Pointer stream);

}
