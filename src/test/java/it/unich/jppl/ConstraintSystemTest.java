package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import it.unich.jppl.Constraint.ConstraintType;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ConstraintSystemTest {

    static Constraint c1, c2;

    @BeforeAll
    static void init() {
        var le = LinearExpression.zero();
        le.add(Coefficient.valueOf(3));
        le.add(Coefficient.valueOf(1), 0);
        c1 = Constraint.of(le, ConstraintType.GREATER_THAN);
        le.add(Coefficient.valueOf(-1), 1);
        c2 = Constraint.of(le, ConstraintType.EQUAL);
    }

    @Test
    void testEmptyConstructor() {
        var cs = ConstraintSystem.empty();
        assertTrue(cs.isOK());
        assertTrue(cs.isEmpty());
        assertEquals(0, cs.getSpaceDimension());
        assertFalse(cs.hasStrictInequalities());
    }

    @Test
    void testZeroDimConstructor() {
        var cs = ConstraintSystem.empty();
        assertTrue(cs.isOK());
        assertTrue(cs.isEmpty());
        assertEquals(0, cs.getSpaceDimension());
        assertFalse(cs.hasStrictInequalities());
        cs = ConstraintSystem.zeroDimFalse();
        assertTrue(cs.isOK());
        assertFalse(cs.isEmpty());
        assertEquals(0, cs.getSpaceDimension());
        assertFalse(cs.hasStrictInequalities());
    }

    @Test
    void testInsert() {
        var cs = ConstraintSystem.empty();
        cs.add(c1);
        assertTrue(cs.isOK());
        assertFalse(cs.isEmpty());
        assertTrue(cs.hasStrictInequalities());
        assertEquals(1, cs.getSpaceDimension());
        cs.clear();
        assertTrue(cs.isEmpty());
        assertEquals(0, cs.getSpaceDimension());
    }

    @Test
    void testToString() {
        var cs = ConstraintSystem.empty();
        cs.add(c1);
        cs.add(c2);
        assertEquals("A > -3, A - B = -3", cs.toString());
    }

    @Test
    void testIterator() {
        var cs = ConstraintSystem.empty();
        cs.add(c1);
        cs.add(c2);
        var le = LinearExpression.zero(2);
        le.add(Coefficient.valueOf(3));
        le.add(Coefficient.valueOf(1), 0);
        var c1bis = Constraint.of(le, ConstraintType.GREATER_THAN);
        var csi = cs.iterator();
        assertEquals(c1bis, csi.next());
        assertEquals(c2, csi.next());
        assertThrows(NoSuchElementException.class, () -> csi.next());
    }
}
