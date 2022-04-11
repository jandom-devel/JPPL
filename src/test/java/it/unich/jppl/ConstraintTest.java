package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Constraint.ZeroDimConstraint;

import org.junit.jupiter.api.Test;

public class ConstraintTest {

    @Test
    void testZeroDimConstructors() {
        var c1 = new Constraint(ZeroDimConstraint.FALSITY);
        assertEquals(0, c1.getSpaceDimension());
        assertEquals(ConstraintType.EQUAL, c1.getType());
        assertNotEquals(new Coefficient(70), c1.getInhomogeneousTerm());
        var c2 = new Constraint(ZeroDimConstraint.POSITIVITY);
        assertEquals(0, c2.getSpaceDimension());
        assertEquals(ConstraintType.GREATER_OR_EQUAL, c2.getType());
        assertEquals(new Coefficient(1), c2.getInhomogeneousTerm());
    }

    @Test
    void testLinearExpressionConstructor() {
        LinearExpression le = new LinearExpression();
        le.add(new Coefficient(3));
        le.add(new Coefficient(1), 0);
        Constraint c = new Constraint(le, ConstraintType.GREATER_THAN);
        assertTrue(c.isOK());
        assertEquals(ConstraintType.GREATER_THAN, c.getType());
        assertEquals(new Coefficient(3), c.getInhomogeneousTerm());
        assertEquals(1, c.getSpaceDimension());
    }

    @Test
    void testEquality() {
        var le = new LinearExpression();
        le.add(new Coefficient(3));
        le.add(new Coefficient(1), 0);
        var c1 = new Constraint(le, ConstraintType.GREATER_THAN);
        le.add(new Coefficient(-1), 1);
        var c2 = new Constraint(le, ConstraintType.EQUAL);

        assertEquals(c1, c1);
        assertNotEquals(c1, c2);
        var c3 = new Constraint(c1);
        assertFalse(c1 == c3);
        assertEquals(c1, c3);
    }

    @Test
    void testGetIllegalCoefficient() {
        var c = new Constraint(ZeroDimConstraint.POSITIVITY);
        var exception = assertThrows(PPLError.class, () -> c.getCoefficient(0));
        assertEquals(PPLError.PPL_ERROR_INVALID_ARGUMENT, exception.getPPLError());
    }

}
