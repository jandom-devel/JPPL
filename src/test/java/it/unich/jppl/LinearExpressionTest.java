package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LinearExpressionTest {

    @Test
    void testDefaultConstructors() {
        var le = LinearExpression.zero();
        assertTrue(le.isZero());
        assertTrue(le.isOK());
        assertTrue(le.isConstant());
        assertEquals(0, le.getSpaceDimension());
        le = LinearExpression.zero(5);
        assertTrue(le.isZero());
        assertTrue(le.isOK());
        assertTrue(le.isConstant());
        assertEquals(5, le.getSpaceDimension());
    }

    @Test
    void testAdd() {
        var le = LinearExpression.zero();
        le.add(Coefficient.valueOf(2), 3);
        le.add(Coefficient.valueOf(5));
        assertEquals(4, le.getSpaceDimension());
        var le2 = LinearExpression.zero(2);
        le2.add(Coefficient.valueOf(-1), 0);
        le2.add(Coefficient.valueOf(1), 1);
        le2.add(Coefficient.valueOf(3), 3);
        le.add(le2);
        le.add(Coefficient.valueOf(4), 3);
        le.add(Coefficient.valueOf(1));
        assertEquals(4, le.getSpaceDimension());
        assertEquals(Coefficient.valueOf(-1), le.getCoefficient(0));
        assertEquals(Coefficient.valueOf(1), le.getCoefficient(1));
        assertEquals(Coefficient.zero(), le.getCoefficient(2));
        assertEquals(Coefficient.valueOf(9), le.getCoefficient(3));
        assertEquals(Coefficient.valueOf(6), le.getInhomogeneousTerm());
        assertTrue(le.isOK());
        assertFalse(le.isZero());
        assertFalse(le.isConstant());
    }

    @Test
    void testLinearExpression() {
        var le = LinearExpression.zero();
        le.add(Coefficient.valueOf(-1), 0);
        le.add(Coefficient.valueOf(1), 1);
        var le2 = le.clone();
        le.add(Coefficient.valueOf(-1), 0);
        assertEquals(Coefficient.valueOf(-2), le.getCoefficient(0));
        assertEquals(Coefficient.valueOf(-1), le2.getCoefficient(0));
        assertEquals(Coefficient.valueOf(1), le2.getCoefficient(1));
        le.add(le2);
        assertEquals(Coefficient.valueOf(-3), le.getCoefficient(0));
        assertEquals(Coefficient.valueOf(2), le.getCoefficient(1));
    }

    @Test
    void testToString() {
        // I am not sure this should be tested
        var le = LinearExpression.zero();
        le.add(Coefficient.valueOf(1), 0);
        le.add(Coefficient.valueOf(2), 1);
        le.add(Coefficient.valueOf(4));
        assertEquals("A + 2*B + 4", le.toString());
    }

    @Test
    void testEquality() {
        // I am not sure this should be tested
        var le = LinearExpression.zero();
        le.add(Coefficient.valueOf(1), 0);
        le.add(Coefficient.valueOf(2), 1);
        le.add(Coefficient.valueOf(4));
        var le2 = le.clone();
        assertFalse(le == le2);
        assertEquals(le, le2);
        assertNotEquals(le, LinearExpression.zero());
    }

    @Test
    void testGetIllegalCoefficient() {
        var le = LinearExpression.zero();
        assertEquals(Coefficient.zero(), le.getCoefficient(0));
    }

    @Test
    void testAuxiliaryConstructors() {
        var le = LinearExpression.of(0, 1);
        assertEquals(Coefficient.zero(), le.getInhomogeneousTerm());
        assertEquals(Coefficient.ONE, le.getCoefficient(0));

    }
}
