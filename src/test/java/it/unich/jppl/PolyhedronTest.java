package it.unich.jppl;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Domain.DegenerateElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class PolyhedronTest {

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
    void testCConstructors() {
        var ph = new CPolyhedron(3, DegenerateElement.UNIVERSE);
        assertTrue(ph.isOK());
        assertTrue(ph.isUniverse());
        assertFalse(ph.isEmpty());
        assertFalse(ph.isDiscrete());
        assertTrue(ph.isTopologicallyClosed());
        assertEquals(3, ph.getSpaceDimension());
        assertFalse(ph.constraints(0));

        ph = new CPolyhedron(3, DegenerateElement.EMPTY);
        assertTrue(ph.isOK());
        assertFalse(ph.isUniverse());
        assertTrue(ph.isEmpty());
        assertTrue(ph.isDiscrete());
        assertTrue(ph.isTopologicallyClosed());
        assertEquals(3, ph.getSpaceDimension());
        assertTrue(ph.constraints(0));
    }

    @Test
    void test1() {
        var ph = new CPolyhedron(3, DegenerateElement.UNIVERSE);
        ph.refineWithConstraint(c1);
        assertTrue(ph.constraints(0));
        assertFalse(ph.constraints(2));
        var le = new LinearExpression();
        le.add(new Coefficient(1), 0);
        assertFalse(ph.boundsFromAbove(le));
        assertTrue(ph.boundsFromBelow(le));
        assertFalse(ph.isDiscrete());

        var res = ph.minimize(le);
        assertTrue(res.isPresent());
        var extremals = res.get();
        assertEquals(new Coefficient(-3), extremals.supN);
        assertEquals(new Coefficient(1), extremals.supD);
        assertTrue(extremals.isMaximum);

        res = ph.maximize(le);
        assertTrue(res.isEmpty());

        var memex = ph.getExternalMemoryInBytes();
        var memtot = ph.getTotalMemoryInBytes();
        assertTrue(memex > 0);
        assertTrue(memtot >= memex);

        var ph2 = new CPolyhedron(ph);
        assertEquals(ph, ph2);

        ph2.refineWithConstraint(c2);

        assertNotEquals(ph, ph2);
        assertTrue(ph.contains(ph2));
        assertTrue(ph.strictlyContains(ph2));
        assertTrue(ph.containsIntegerPoint());
    }

}

