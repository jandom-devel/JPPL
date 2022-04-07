package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import it.unich.jppl.Generator.GeneratorType;
import it.unich.jppl.Generator.ZeroDimGenerator;

public class GeneratorTest {

    @Test
    void testZeroDimConstructors() {
        var g1 = new Generator(ZeroDimGenerator.POINT);
        assertEquals(0, g1.getSpaceDimension());
        assertEquals(GeneratorType.POINT, g1.getType());
        assertEquals(new Coefficient(1), g1.getDivisor());
        var g2 = new Generator(ZeroDimGenerator.CLOSURE_POINT);
        assertEquals(0, g2.getSpaceDimension());
        assertEquals(GeneratorType.CLOSURE_POINT, g2.getType());
        assertEquals(new Coefficient(1), g2.getDivisor());
    }

    @Test
    void testLinearExpressionConstructor() {
        LinearExpression le = new LinearExpression();
        le.add(new Coefficient(3), 0);
        le.add(new Coefficient(6), 1);
        Generator g = new Generator(le, GeneratorType.LINE, new Coefficient(1));
        assertTrue(g.isOK());
        assertEquals(GeneratorType.LINE, g.getType());
        assertEquals(new Coefficient(1), g.getCoefficient(0));
        assertEquals(2, g.getSpaceDimension());
    }

    @Test
    void testEquality() {
        var le = new LinearExpression();
        le.add(new Coefficient(1), 0);
        var g1 = new Generator(le, GeneratorType.LINE, new Coefficient(0));
        le.add(new Coefficient(-1), 1);
        var g2 = new Generator(le, GeneratorType.POINT, new Coefficient(1));

        assertEquals(g1, g1);
        assertNotEquals(g1, g2);
        var g3 = new Generator(g1);
        assertFalse(g1 == g3);
        assertEquals(g1, g3);
    }

    @Test
    void testGetIllegalCoefficient() {
        var g = new Generator(ZeroDimGenerator.POINT);
        var exception = assertThrows(PPLError.class, () -> g.getCoefficient(0));
        assertEquals(PPLError.PPL_ERROR_INVALID_ARGUMENT, exception.getPPLError());
    }

}
