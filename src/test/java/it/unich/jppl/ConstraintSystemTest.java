package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.ConstraintSystem.ZeroDimConstraintSystem;

public class ConstraintSystemTest {

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
    void testEmptyConstructor() {
        var cs = new ConstraintSystem();
        assertTrue(cs.isOK());
        assertTrue(cs.isEmpty());
        assertEquals(0, cs.getSpaceDimension());
        assertFalse(cs.hasStrictInequalities());
    }

    @Test
    void testZeroDimConstructor() {
        var cs = new ConstraintSystem(ZeroDimConstraintSystem.EMPTY);
        assertTrue(cs.isOK());
        assertTrue(cs.isEmpty());
        assertEquals(0, cs.getSpaceDimension());
        assertFalse(cs.hasStrictInequalities());
        cs = new ConstraintSystem(ZeroDimConstraintSystem.UNSATISFIABLE);
        assertTrue(cs.isOK());
        assertFalse(cs.isEmpty());
        assertEquals(0, cs.getSpaceDimension());
        assertFalse(cs.hasStrictInequalities());
    }

    @Test
    void testInsert() {
        var cs = new ConstraintSystem();
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
        var cs = new ConstraintSystem();
        cs.add(c1);
        cs.add(c2);
        assertEquals("A > -3, A - B = -3", cs.toString());
    }

    @Test
    void testIterator() {
        var cs = new ConstraintSystem();
        cs.add(c1);
        cs.add(c2);
        var le = new LinearExpression(2);
        le.add(new Coefficient(3));
        le.add(new Coefficient(1), 0);
        var c1bis  = new Constraint(le, ConstraintType.GREATER_THAN);
        var csi = cs.iterator();
        assertEquals(c1bis, csi.next());
        assertEquals(c2, csi.next());
        assertThrows(NoSuchElementException.class, () -> csi.next());
    }
}
