package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import it.unich.jppl.Constraint.ConstraintType;

import org.junit.jupiter.api.Test;

public class ConstraintTest {

    @Test
    void testZeroDimConstructors() {
        var c1 = Constraint.zeroDimFalse();
        assertEquals(0, c1.getSpaceDimension());
        assertEquals(ConstraintType.EQUAL, c1.getType());
        assertNotEquals(Coefficient.valueOf(70), c1.getInhomogeneousTerm());
        var c2 = Constraint.zeroDimPositivity();
        assertEquals(0, c2.getSpaceDimension());
        assertEquals(ConstraintType.GREATER_OR_EQUAL, c2.getType());
        assertEquals(Coefficient.valueOf(1), c2.getInhomogeneousTerm());
    }

    @Test
    void testLinearExpressionConstructor() {
        LinearExpression le = LinearExpression.zero();
        le.add(Coefficient.valueOf(3));
        le.add(Coefficient.valueOf(1), 0);
        Constraint c = Constraint.of(le, ConstraintType.GREATER_THAN);
        assertTrue(c.isOK());
        assertEquals(ConstraintType.GREATER_THAN, c.getType());
        assertEquals(Coefficient.valueOf(3), c.getInhomogeneousTerm());
        assertEquals(1, c.getSpaceDimension());
    }

    @Test
    void testEquality() {
        var le = LinearExpression.zero();
        le.add(Coefficient.valueOf(3));
        le.add(Coefficient.valueOf(1), 0);
        var c1 = Constraint.of(le, ConstraintType.GREATER_THAN);
        le.add(Coefficient.valueOf(-1), 1);
        var c2 = Constraint.of(le, ConstraintType.EQUAL);

        assertEquals(c1, c1);
        assertNotEquals(c1, c2);
        var c3 = c1.clone();
        assertFalse(c1 == c3);
        assertEquals(c1, c3);
    }

    @Test
    void testGetIllegalCoefficient() {
        var c = Constraint.zeroDimPositivity();
        var exception = assertThrows(PPLRuntimeException.class, () -> c.getCoefficient(0));
        assertEquals(PPLRuntimeException.INVALID_ARGUMENT, exception.getCode());
    }

}
