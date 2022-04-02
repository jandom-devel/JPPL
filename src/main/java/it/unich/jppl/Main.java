/**
 * Created by amato on 17/03/16.
 */

package it.unich.jppl;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.IntByReference;
import it.unich.jppl.nativeppl.LibPPL;

public  class Main {

    public static void one() {
        PointerByReference ph = new PointerByReference();
        LibPPL.ppl_new_C_Polyhedron_from_space_dimension(ph,2,0);
        Pointer h = ph.getValue();

        PointerByReference pc = new PointerByReference();
        Memory mpz = new Memory(LibGMP.MPZ_SIZE);
        LibGMP.__gmpz_init_set_si(mpz, 2L);
        LibPPL.ppl_new_Coefficient_from_mpz_t(pc, mpz);
        Pointer c = pc.getValue();

        PointerByReference ple = new PointerByReference();
        LibPPL.ppl_new_Linear_Expression(ple);
        Pointer le = ple.getValue();
        LibPPL.ppl_Linear_Expression_add_to_coefficient(le, 0, c);

        PointerByReference pc2 = new PointerByReference();
        Memory mpz2 = new Memory(LibGMP.MPZ_SIZE);
        LibGMP.__gmpz_init_set_si(mpz2, 3L);
        LibPPL.ppl_new_Coefficient_from_mpz_t(pc2, mpz2);
        Pointer c2 = pc2.getValue();

        PointerByReference pconstr = new PointerByReference();
        LibPPL.ppl_Linear_Expression_add_to_inhomogeneous(le,c2);
        LibPPL.ppl_new_Constraint(pconstr, le, 0);
        Pointer constr = pconstr.getValue();

        LibPPL.ppl_Polyhedron_refine_with_constraint(h,constr);
        LibPPL.ppl_io_print_Polyhedron(h);
    }

    public static void two() {
        LinearExpression le = new LinearExpression();
        le.addToCoefficient(0, new Coefficient(2));
        le.addToInhomogeneous(new Coefficient(3));
        Constraint c = new Constraint(le, 0);

        CPolyhedron h = new CPolyhedron(2, 0);
        h.refineWithConstraint(c);
        System.out.print(h);
    }

    public static void main(String[] args) {
        LibPPL.ppl_initialize();
        IntByReference x = new IntByReference(183);
        LibPPL.ppl_irrational_precision(x);
        System.out.println(x.getValue());
        System.out.println(LibPPL.ppl_version_major()+"."+LibPPL.ppl_version_minor());
        one();
        two();
    }
}
