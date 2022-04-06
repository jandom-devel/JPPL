package it.unich.jppl.nativelib;

import it.unich.jppl.ErrorHandler;
import it.unich.jppl.VariableOutputFunction;

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

public final class LibPPL {

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

    public static native int ppl_set_error_handler(ErrorHandler h);

    // Timeout handling

    public static native int ppl_set_timeout(int csecs);

    public static native int ppl_reset_timeout();

    public static native int ppl_set_deterministic_timeout(NativeLong unscaled_weight, int scale);

    public static native int ppl_reset_deterministic_timeout();

    // Dimensions

    public static class Dimension extends IntegerType {
        public Dimension() {
            this(0);
        }

        public Dimension(long value) {
            super(Native.SIZE_T_SIZE, value, true);
        }
    }

    public static class DimensionArray extends Memory {
        public DimensionArray(long[] ds) {
            super(ds.length * Native.SIZE_T_SIZE);
            for (int i = 0; i < ds.length; i++)
                if (Native.SIZE_T_SIZE == 8)
                    setLong(8*i, ds[i]);
                else
                    setInt(4*i, (int) ds[i]);
        }
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

    @FieldOrder({ "f" })
    public static class VariableOutputFunctionByRef extends Structure {
        public VariableOutputFunction f;
    }

    public static native int ppl_max_space_dimension(DimensionByReference m);

    public static native int ppl_not_a_dimension(DimensionByReference m);

    public static native int ppl_io_print_variable(Dimension var);

    public static native int ppl_io_fprint_variable(Pointer stream, Dimension var);

    public static native int ppl_io_asprint_variable(PointerByReference strp, Dimension var);

    public static native int ppl_io_set_variable_output_function(VariableOutputFunction p);

    public static native int ppl_io_get_variable_output_function(VariableOutputFunctionByRef pp);

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

    public static native int ppl_new_Linear_Expression_with_dimension(PointerByReference ple, Dimension d);

    public static native int ppl_new_Linear_Expression_from_Linear_Expression(PointerByReference ple, Pointer le);

    public static native int ppl_new_Linear_Expression_from_Constraint(PointerByReference ple, Pointer c);

    public static native int ppl_new_Linear_Expression_from_Generator(PointerByReference ple, Pointer g);

    public static native int ppl_new_Linear_Expression_from_Congruence(PointerByReference ple, Pointer c);

    //public static native int ppl_new_Linear_Expression_from_Grid_Generator (PointerByReference ple, Pointer g);

    public static native int ppl_assign_Linear_Expression_from_Linear_Expression(Pointer dst, Pointer src);

    public static native int ppl_delete_Linear_Expression(Pointer le);

    public static native int ppl_Linear_Expression_space_dimension(Pointer le, DimensionByReference m);

    public static native int ppl_Linear_Expression_coefficient(Pointer le, Dimension var, Pointer n);

    public static native int ppl_Linear_Expression_inhomogeneous_term(Pointer le, Pointer n);

    public static native int ppl_Linear_Expression_OK(Pointer le);

    public static native int ppl_Linear_Expression_is_zero(Pointer le);

    public static native int ppl_Linear_Expression_all_homogeneous_terms_are_zero(Pointer le);

    public static native int ppl_Linear_Expression_add_to_coefficient(Pointer le, Dimension var, Pointer n);

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

    public static native int ppl_Constraint_space_dimension(Pointer c, DimensionByReference m);

    public static native int ppl_Constraint_type(Pointer c);

    public static native int ppl_Constraint_coefficient(Pointer c, Dimension var, Pointer n);

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

    public static native int ppl_Constraint_System_space_dimension(Pointer cs, DimensionByReference m);

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

    // ConstraintSystem Iterator

    public static native int ppl_new_Constraint_System_const_iterator(PointerByReference pcit);

    public static native int ppl_new_Constraint_System_const_iterator_from_Constraint_System_const_iterator(
            PointerByReference pcit, Pointer cit);

    public static native int ppl_assign_Constraint_System_const_iterator_from_Constraint_System_const_iterator(
            Pointer dst, Pointer src);

    public static native int ppl_delete_Constraint_System_const_iterator(Pointer cit);

    public static native int ppl_Constraint_System_const_iterator_dereference(Pointer cit, PointerByReference pc);

    public static native int ppl_Constraint_System_const_iterator_increment(Pointer cit);

    public static native int ppl_Constraint_System_const_iterator_equal_test(Pointer x, Pointer y);

    // Polyhedron

    public static native int ppl_new_C_Polyhedron_from_space_dimension(PointerByReference pph, Dimension d, int empty);

    public static native int ppl_new_C_Polyhedron_from_C_Polyhedron(PointerByReference pph, Pointer ph);

    public static native int ppl_new_C_Polyhedron_from_C_Polyhedron_with_complexity(PointerByReference pph, Pointer ph,
            int complexity);

    public static native int ppl_new_C_Polyhedron_from_Constraint_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_C_Polyhedron_recycle_Constraint_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_C_Polyhedron_from_Congruence_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_C_Polyhedron_recycle_Congruence_System(PointerByReference pph, Pointer cs);

    public static native int ppl_assign_C_Polyhedron_from_C_Polyhedron(Pointer dst, Pointer src);

    public static native int ppl_new_C_Polyhedron_from_NNC_Polyhedron(PointerByReference pph, Pointer ph);

    public static native int ppl_new_C_Polyhedron_from_NNC_Polyhedron_with_complexity(PointerByReference pph,
            Pointer ph, int complexity);

    public static native int ppl_new_NNC_Polyhedron_from_space_dimension(PointerByReference pph, Dimension d,
            int empty);

    public static native int ppl_new_NNC_Polyhedron_from_NNC_Polyhedron(PointerByReference pph, Pointer ph);

    public static native int ppl_new_NNC_Polyhedron_from_NNC_Polyhedron_with_complexity(PointerByReference pph,
            Pointer ph, int complexity);

    public static native int ppl_new_NNC_Polyhedron_from_Constraint_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_NNC_Polyhedron_recycle_Constraint_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_NNC_Polyhedron_from_Congruence_System(PointerByReference pph, Pointer cs);

    public static native int ppl_new_NNC_Polyhedron_recycle_Congruence_System(PointerByReference pph, Pointer cs);

    public static native int ppl_assign_NNC_Polyhedron_from_NNC_Polyhedron(Pointer dst, Pointer src);

    public static native int ppl_new_NNC_Polyhedron_from_C_Polyhedron(PointerByReference pph, Pointer ph);

    public static native int ppl_new_NNC_Polyhedron_from_C_Polyhedron_with_complexity(PointerByReference pph,
            Pointer ph, int complexity);

    public static native int ppl_delete_Polyhedron(Pointer ph);

    public static native int ppl_Polyhedron_space_dimension(Pointer ph, DimensionByReference m);

    public static native int ppl_Polyhedron_affine_dimension(Pointer ph, DimensionByReference m);

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

    public static native int ppl_Polyhedron_constrains(Pointer ph, Dimension var);

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

    public static native int ppl_Polyhedron_external_memory_in_bytes(Pointer ph, DimensionByReference sz);

    public static native int ppl_Polyhedron_total_memory_in_bytes(Pointer ph, DimensionByReference sz);

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

    public static native int ppl_Polyhedron_unconstrain_space_dimension(Pointer ph, Dimension var);

    public static native int ppl_Polyhedron_unconstrain_space_dimensions(Pointer ph, Pointer ds, Dimension n);

    public static native int ppl_Polyhedron_affine_image(Pointer ph, Dimension var, Pointer le, Pointer d);

    public static native int ppl_Polyhedron_affine_preimage(Pointer ph, Dimension var, Pointer le, Pointer d);

    public static native int ppl_Polyhedron_bounded_affine_image(Pointer ph, Dimension var, Pointer lb, Pointer ub,
            Pointer d);

    public static native int ppl_Polyhedron_bounded_affine_preimage(Pointer ph, Dimension var, Pointer lb, Pointer ub,
            Pointer d);

    public static native int ppl_Polyhedron_generalized_affine_image(Pointer ph, Dimension var, int relsym, Pointer le,
            Pointer d);

    public static native int ppl_Polyhedron_generalized_affine_preimage(Pointer ph, Dimension var, int relsym,
            Pointer le, Pointer d);

    public static native int ppl_Polyhedron_generalized_affine_image_lhs_rhs(Pointer ph, Pointer lhs, int relsym,
            Pointer rhs);

    public static native int ppl_Polyhedron_generalized_affine_preimage_lhs_rhs(Pointer ph, Pointer lhs, int relsym,
            Pointer rhs);

    public static native int ppl_Polyhedron_concatenate_assign(Pointer x, Pointer y);

    public static native int ppl_Polyhedron_add_space_dimensions_and_embed(Pointer ph, Dimension d);

    public static native int ppl_Polyhedron_add_space_dimensions_and_project(Pointer ph, Dimension d);

    public static native int ppl_Polyhedron_remove_space_dimensions(Pointer ph, Pointer ds, Dimension n);

    public static native int ppl_Polyhedron_remove_higher_space_dimensions(Pointer ph, Dimension d);

    public static native int ppl_Polyhedron_map_space_dimensions(Pointer ph, Pointer maps, Dimension n);

    public static native int ppl_Polyhedron_expand_space_dimension(Pointer ph, Dimension d, Dimension m);

    public static native int ppl_Polyhedron_fold_space_dimensions(Pointer ph, Pointer ds, Dimension n, Dimension d);

    public static native int ppl_io_print_Polyhedron(Pointer x);

    public static native int ppl_io_fprint_Polyhedron(Pointer stream, Pointer x);

    public static native int ppl_io_asprint_Polyhedron(PointerByReference strp, Pointer x);

    public static native int ppl_Polyhedron_ascii_dump(Pointer x, Pointer stream);

    public static native int ppl_Polyhedron_ascii_load(Pointer x, Pointer stream);

    public static native int ppl_new_C_Polyhedron_from_Generator_System(PointerByReference pph, Pointer gs);

    public static native int ppl_new_C_Polyhedron_recycle_Generator_System(PointerByReference pph, Pointer gs);

    public static native int ppl_new_NNC_Polyhedron_from_Generator_System(PointerByReference pph, Pointer gs);

    public static native int ppl_new_NNC_Polyhedron_recycle_Generator_System(PointerByReference pph, Pointer gs);

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
}
