package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Domain.DegenerateElement;

import org.junit.jupiter.api.Test;

public class DoubleBoxTest {

    @Test
    void testAddConstraints() {
        var le = new LinearExpression().add(new Coefficient(3)).add(new Coefficient(1), 0);
        var c1 = new Constraint(le, ConstraintType.GREATER_THAN);
        le.add(new Coefficient(-1), 1);
        var c2 = new Constraint(le, ConstraintType.EQUAL);

        var box = new DoubleBox(2, DegenerateElement.UNIVERSE);
        assertFalse(box.constraints(0));
        box.addConstraint(c1);

        assertTrue(box.constraints(0));
        var exception = assertThrows(PPLError.class, () -> box.addConstraint(c2));
        assertEquals(exception.getPPLError(), PPLError.PPL_ERROR_INVALID_ARGUMENT);
    }

}
