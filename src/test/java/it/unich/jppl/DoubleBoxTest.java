package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Domain.DegenerateElement;

import org.junit.jupiter.api.Test;

public class DoubleBoxTest {

    @Test
    void testAddConstraints() {
        var le = LinearExpression.zero().add(Coefficient.valueOf(3)).add(Coefficient.valueOf(1), 0);
        var c1 = Constraint.of(le, ConstraintType.GREATER_THAN);
        le.add(Coefficient.valueOf(-1), 1);
        var c2 = Constraint.of(le, ConstraintType.EQUAL);

        var box = new DoubleBox(2, DegenerateElement.UNIVERSE);
        assertFalse(box.constraints(0));
        box.addConstraint(c1);

        assertTrue(box.constraints(0));
        var exception = assertThrows(PPLError.class, () -> box.addConstraint(c2));
        assertEquals(exception.getPPLError(), PPLError.PPL_ERROR_INVALID_ARGUMENT);
    }

}
