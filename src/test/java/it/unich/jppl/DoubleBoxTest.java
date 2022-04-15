package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Generator.GeneratorType;

import org.junit.jupiter.api.Test;

public class DoubleBoxTest {

    @Test
    void testAddConstraints() {
        var le = LinearExpression.zero().add(Coefficient.valueOf(3)).add(Coefficient.valueOf(1), 0);
        var c1 = Constraint.of(le, ConstraintType.GREATER_THAN);
        le.add(Coefficient.valueOf(-1), 1);
        var c2 = Constraint.of(le, ConstraintType.EQUAL);

        var box = DoubleBox.universe(2);
        assertFalse(box.constraints(0));
        box.add(c1);

        assertTrue(box.constraints(0));
        var exception = assertThrows(PPLRuntimeException.class, () -> box.add(c2));
        assertEquals(exception.getCode(), PPLRuntimeException.INVALID_ARGUMENT);
    }

    @Test
    void testAddCongruences() {
        var le = LinearExpression.zero().add(Coefficient.ONE, 0);
        var c = Congruence.of(le, Coefficient.valueOf(2));
        var cs = CongruenceSystem.of(c);
        var exception = assertThrows(PPLRuntimeException.class, () -> DoubleBox.from(cs));
        assertEquals(exception.getCode(), PPLRuntimeException.INVALID_ARGUMENT);
    }

    @Test
    void testAddGenerator() {
        var p = Generator.zeroDimPoint();
        var le = LinearExpression.zero().add(Coefficient.ONE, 0);
        var g1 = Generator.of(le, GeneratorType.RAY);
        le.add(Coefficient.ONE, 1);
        var g2 = Generator.of(le, GeneratorType.RAY);
        var gs = GeneratorSystem.of(p).add(g1).add(g2);
        var b = DoubleBox.from(gs);
        assertFalse(b.isBounded());
    }

    @Test
    void testBig() {
        var exception = assertThrows(PPLRuntimeException.class, () -> DoubleBox.universe(-3));
        assertEquals(exception.getCode(), PPLRuntimeException.LENGTH_ERROR);
    }

    @Test
    void testWidenings() {
        var dom = new DoubleBoxDomain();
        var ws = dom.getWidenings().get(0);
        assertEquals("CC76", ws.getName());
        var p1 = dom.createUniverse(1);
        p1.refineWith(Constraint.of(LinearExpression.of(-3, 1), ConstraintType.LESS_OR_EQUAL));
        var p2 = dom.createUniverse(1);
        p2.refineWith(Constraint.of(LinearExpression.of(-10, 1), ConstraintType.LESS_OR_EQUAL));
        ws.getWidening().apply(p2, p1);
        assertTrue(p2.isUniverse());
    }

}
