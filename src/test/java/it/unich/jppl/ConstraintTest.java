package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Constraint.ZeroDimConstraint;

public class ConstraintTest {

    static Constraint c1, c2;

    @BeforeAll
    static void init() {
        var le = new LinearExpression();
        le.add(new Coefficient(3));
        le.add(new Coefficient(1), 0);
        c1  = new Constraint(le, ConstraintType.GREATER_THAN);
        le.add(new Coefficient(-1), 1);
        c2  = new Constraint(le, ConstraintType.EQUAL);
    }

    @Test
    void testZeroDimConstructors() {
        var c1 = new Constraint(ZeroDimConstraint.FALSITY);
        assertEquals(0, c1.getSpaceDimension());
        assertEquals(ConstraintType.EQUAL, c1.getType());
        assertNotEquals(new Coefficient(70), c1.getCoefficient());
        var c2 = new Constraint(ZeroDimConstraint.POSITIVITY);
        assertEquals(0, c2.getSpaceDimension());
        assertEquals(ConstraintType.GREATER_OR_EQUAL, c2.getType());
        assertEquals(new Coefficient(1), c2.getCoefficient());
    }

    @Test
    void testLinearExpressionConstructor() {
        LinearExpression le = new LinearExpression();
        le.add(new Coefficient(3));
        le.add(new Coefficient(1), 0);
        Constraint c = new Constraint(le, ConstraintType.GREATER_THAN);
        assertTrue(c.isOK());
        assertEquals(ConstraintType.GREATER_THAN, c.getType());
        assertEquals(new Coefficient(3), c.getCoefficient());
        assertEquals(1, c.getSpaceDimension());
    }

    @Test
    void testEquality() {
        assertEquals(c1, c1);
        assertNotEquals(c1, c2);
        var c3 = new Constraint(c1);
        assertFalse(c1 == c3);
        assertEquals(c1, c3);
    }

}
