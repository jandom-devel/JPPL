package it.unich.jppl;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Domain.DegenerateElement;
import static it.unich.jppl.Constraint.RelationWithConstraint.*;

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
        var le = new LinearExpression(2);
        le.add(new Coefficient(3));
        le.add(new Coefficient(1), 0);
        c1  = new Constraint(le, ConstraintType.GREATER_THAN);
        le.add(new Coefficient(-1), 1);
        c2  = new Constraint(le, ConstraintType.EQUAL);
    }

    @Test
    void testCConstructors() {
        var ph1 = new CPolyhedron(3, DegenerateElement.UNIVERSE);
        assertTrue(ph1.isOK());
        assertTrue(ph1.isUniverse());
        assertFalse(ph1.isEmpty());
        assertFalse(ph1.isDiscrete());
        assertTrue(ph1.containsIntegerPoint());
        assertTrue(ph1.isTopologicallyClosed());
        assertEquals(3, ph1.getSpaceDimension());
        assertEquals(3, ph1.getAffineDimension());
        assertFalse(ph1.constraints(0));
        assertEquals(STRICTLY_INTERSECTS, ph1.getRelationWithConstraint(c1));

        var ph2 = new CPolyhedron(3, DegenerateElement.EMPTY);
        assertTrue(ph2.isOK());
        assertFalse(ph2.isUniverse());
        assertTrue(ph2.isEmpty());
        assertTrue(ph2.isDiscrete());
        assertFalse(ph2.containsIntegerPoint());
        assertTrue(ph2.isTopologicallyClosed());
        assertEquals(3, ph2.getSpaceDimension());
        assertEquals(0, ph2.getAffineDimension());
        assertTrue(ph2.constraints(0));
        assertEquals(IS_DISJOINT | IS_INCLUDED | SATURATES, ph2.getRelationWithConstraint(c1));
    }

    @Test
    void testNNCConstructors() {
        var ph1 = new NNCPolyhedron(3, DegenerateElement.UNIVERSE);
        assertTrue(ph1.isOK());
        assertTrue(ph1.isUniverse());
        assertFalse(ph1.isEmpty());
        assertFalse(ph1.isDiscrete());
        assertTrue(ph1.containsIntegerPoint());
        assertTrue(ph1.isTopologicallyClosed());
        assertEquals(3, ph1.getSpaceDimension());
        assertEquals(3, ph1.getAffineDimension());
        assertFalse(ph1.constraints(0));
        assertEquals(STRICTLY_INTERSECTS, ph1.getRelationWithConstraint(c1));

        var ph2 = new NNCPolyhedron(3, DegenerateElement.EMPTY);
        assertTrue(ph2.isOK());
        assertFalse(ph2.isUniverse());
        assertTrue(ph2.isEmpty());
        assertTrue(ph2.isDiscrete());
        assertFalse(ph2.containsIntegerPoint());
        assertTrue(ph2.isTopologicallyClosed());
        assertEquals(3, ph2.getSpaceDimension());
        assertEquals(0, ph2.getAffineDimension());
        assertTrue(ph2.constraints(0));
        assertEquals(IS_DISJOINT | IS_INCLUDED | SATURATES, ph2.getRelationWithConstraint(c1));
    }

    @Test
    void testGetConstraints() {
        var ph = new NNCPolyhedron(2, DegenerateElement.UNIVERSE);
        ph.addConstraint(c1).addConstraint(c2);
        var constraints = ph.getConstraints();
        for (var c: constraints)
            assertTrue(c.equals(c1) || c.equals(c2));
    }

    @Test
    void test1() {
        var ph1 = new CPolyhedron(3, DegenerateElement.UNIVERSE);
        ph1.refineWithConstraint(c1);
        assertEquals(3, ph1.getSpaceDimension());
        assertEquals(3, ph1.getAffineDimension());

        // is???
        assertFalse(ph1.isDiscrete());
        assertEquals(ph1, ph1);

        // constrains and getRelationWithConstraint
        assertTrue(ph1.constraints(0));
        assertFalse(ph1.constraints(2));
        assertEquals(STRICTLY_INTERSECTS, ph1.getRelationWithConstraint(c1));

        // boundsFromAbove / boundsFromBelow
        var le = new LinearExpression();
        le.add(new Coefficient(1), 0);
        assertFalse(ph1.boundsFromAbove(le));
        assertTrue(ph1.boundsFromBelow(le));

        // minimize / maximize
        var res = ph1.minimize(le);
        assertTrue(res.isPresent());
        var extremals = res.get();
        assertEquals(new Coefficient(-3), extremals.supN);
        assertEquals(new Coefficient(1), extremals.supD);
        assertTrue(extremals.isMaximum);
        res = ph1.maximize(le);
        assertTrue(res.isEmpty());

        // get???MemoryInBytes
        var memex = ph1.getExternalMemoryInBytes();
        var memtot = ph1.getTotalMemoryInBytes();
        assertTrue(memex > 0);
        assertTrue(memtot >= memex);

        var ph2 = new CPolyhedron(ph1);
        assertEquals(ph1, ph2);

        // contains / strictlyContains
        ph2.refineWithConstraint(c2);
        assertNotEquals(ph1, ph2);
        assertTrue(ph1.contains(ph2));
        assertTrue(ph1.strictlyContains(ph2));
        assertEquals(3, ph2.getSpaceDimension());
        assertEquals(2, ph2.getAffineDimension());
        assertEquals(STRICTLY_INTERSECTS, ph1.getRelationWithConstraint(c1));
    }

    @Test
    void test2() {
        var ph1 = new NNCPolyhedron(3, DegenerateElement.UNIVERSE);
        ph1.refineWithConstraint(c1);
        assertTrue(ph1.constraints(0));
        assertFalse(ph1.constraints(2));
        assertEquals(IS_INCLUDED, ph1.getRelationWithConstraint(c1));

        var le = new LinearExpression();
        le.add(new Coefficient(1), 0);
        assertFalse(ph1.boundsFromAbove(le));
        assertTrue(ph1.boundsFromBelow(le));
        assertFalse(ph1.isDiscrete());

        var res = ph1.minimize(le);
        assertTrue(res.isPresent());
        var extremals = res.get();
        assertEquals(new Coefficient(-3), extremals.supN);
        assertEquals(new Coefficient(1), extremals.supD);
        assertFalse(extremals.isMaximum);

        res = ph1.maximize(le);
        assertTrue(res.isEmpty());

        var ph2 = new CPolyhedron(ph1);
        assertNotEquals(ph1, ph2);

        ph2.refineWithConstraint(c2);
        assertNotEquals(ph1, ph2);
        assertTrue(ph2.containsIntegerPoint());
        assertEquals(STRICTLY_INTERSECTS, ph2.getRelationWithConstraint(c1));
    }



}

