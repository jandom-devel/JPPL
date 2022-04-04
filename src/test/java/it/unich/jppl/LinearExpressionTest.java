package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LinearExpressionTest {

    @BeforeAll
    static void init() {
        PPL.pplInitialize();
    }

    @AfterAll
    static void finish() {
        PPL.pplFinalize();
    }

    @Test
    void testDefaultConstructors() {
        var le = new LinearExpression();
        assertTrue(le.isZero());
        assertTrue(le.isOK());
        assertTrue(le.allHomogeneousTermsAreZero());
        assertEquals(0, le.getSpaceDimension());
        le = new LinearExpression(5);
        assertTrue(le.isZero());
        assertTrue(le.isOK());
        assertTrue(le.allHomogeneousTermsAreZero());
        assertEquals(5, le.getSpaceDimension());
    }

    @Test
    void testAdd() {
        var le = new LinearExpression();
        le.add(new Coefficient(2), 3);
        le.add(new Coefficient(5));
        assertEquals(4, le.getSpaceDimension());
        var le2 = new LinearExpression(2);
        le2.add(new Coefficient(-1), 0);
        le2.add(new Coefficient(1), 1);
        le2.add(new Coefficient(3), 3);
        le.add(le2);
        le.add(new Coefficient(4), 3);
        le.add(new Coefficient(1));
        assertEquals(4, le.getSpaceDimension());
        assertEquals(new Coefficient(-1), le.getCoefficient(0));
        assertEquals(new Coefficient(1), le.getCoefficient(1));
        assertEquals(new Coefficient(0), le.getCoefficient(2));
        assertEquals(new Coefficient(9), le.getCoefficient(3));
        assertEquals(new Coefficient(6), le.getCoefficient());
        assertTrue(le.isOK());
        assertFalse(le.isZero());
        assertFalse(le.allHomogeneousTermsAreZero());
    }

    @Test
    void testLinearExpression() {
        var le = new LinearExpression();
        le.add(new Coefficient(-1), 0);
        le.add(new Coefficient(1), 1);
        var le2 = new LinearExpression(le);
        le.add(new Coefficient(-1), 0);
        assertEquals(new Coefficient(-2), le.getCoefficient(0));
        assertEquals(new Coefficient(-1), le2.getCoefficient(0));
        assertEquals(new Coefficient(1), le2.getCoefficient(1));
        le.add(le2);
        assertEquals(new Coefficient(-3), le.getCoefficient(0));
        assertEquals(new Coefficient(2), le.getCoefficient(1));
    }

    @Test
    void testToString() {
        // I am not sure this should be tested
        var le = new LinearExpression();
        le.add(new Coefficient(1), 0);
        le.add(new Coefficient(2), 1);
        le.add(new Coefficient(4));
        assertEquals("A + 2*B + 4", le.toString());
    }

    @Test
    void testEquality() {
        // I am not sure this should be tested
        var le = new LinearExpression();
        le.add(new Coefficient(1), 0);
        le.add(new Coefficient(2), 1);
        le.add(new Coefficient(4));
        var le2 = new LinearExpression(le);
        assertFalse(le == le2);
        assertEquals(le, le2);
        assertNotEquals(le, new LinearExpression());
    }
}

