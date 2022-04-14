package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import it.unich.jppl.Generator.GeneratorType;

public class GeneratorTest {

    @Test
    void testZeroDimConstructors() {
        var g1 = Generator.zeroDimPoint();
        assertEquals(0, g1.getSpaceDimension());
        assertEquals(GeneratorType.POINT, g1.getType());
        assertEquals(Coefficient.valueOf(1), g1.getDivisor());
        var g2 = Generator.zeroDimClosurePoint();
        assertEquals(0, g2.getSpaceDimension());
        assertEquals(GeneratorType.CLOSURE_POINT, g2.getType());
        assertEquals(Coefficient.valueOf(1), g2.getDivisor());
    }

    @Test
    void testLinearExpressionConstructor() {
        LinearExpression le = LinearExpression.zero();
        le.add(Coefficient.valueOf(3), 0);
        le.add(Coefficient.valueOf(6), 1);
        Generator g = Generator.of(le, GeneratorType.LINE, Coefficient.valueOf(1));
        assertTrue(g.isOK());
        assertEquals(GeneratorType.LINE, g.getType());
        assertEquals(Coefficient.valueOf(1), g.getCoefficient(0));
        assertEquals(2, g.getSpaceDimension());
    }

    @Test
    void testEquality() {
        var le = LinearExpression.zero();
        le.add(Coefficient.valueOf(1), 0);
        var g1 = Generator.of(le, GeneratorType.LINE, Coefficient.zero());
        le.add(Coefficient.valueOf(-1), 1);
        var g2 = Generator.of(le, GeneratorType.POINT, Coefficient.valueOf(1));

        assertEquals(g1, g1);
        assertNotEquals(g1, g2);
        var g3 = g1.clone();
        assertFalse(g1 == g3);
        assertEquals(g1, g3);
    }

    @Test
    void testGetIllegalCoefficient() {
        var g = Generator.zeroDimPoint();
        var exception = assertThrows(PPLError.class, () -> g.getCoefficient(0));
        assertEquals(PPLError.INVALID_ARGUMENT, exception.getCode());
    }

}
