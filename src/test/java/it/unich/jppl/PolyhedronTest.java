package it.unich.jppl;

import static it.unich.jppl.Constraint.RelationWithConstraint.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.Domain.DegenerateElement;
import it.unich.jppl.Generator.GeneratorType;
import it.unich.jppl.Property.WideningToken;

import org.junit.jupiter.api.Test;

public class PolyhedronTest {

    @Test
    void testCConstructors() {
        var le = new LinearExpression().add(new Coefficient(3)).add(new Coefficient(1), 0);
        var c1 = new Constraint(le, ConstraintType.GREATER_THAN);

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
        var le = new LinearExpression().add(new Coefficient(3)).add(new Coefficient(1), 0);
        var c1 = new Constraint(le, ConstraintType.GREATER_THAN);

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
        var le = new LinearExpression(2).add(new Coefficient(3)).add(new Coefficient(1), 0);
        var c1 = new Constraint(le, ConstraintType.GREATER_THAN);
        le.add(new Coefficient(-1), 1);
        var c2 = new Constraint(le, ConstraintType.EQUAL);

        var ph = new NNCPolyhedron(2, DegenerateElement.UNIVERSE);
        ph.addConstraint(c1).addConstraint(c2);
        var constraints = ph.getConstraints();
        for (var c : constraints)
            assertTrue(c.equals(c1) || c.equals(c2));
    }

    @Test
    void testUnconstraints() {
        var c0 = new Constraint(new LinearExpression().add(new Coefficient(-3)).add(new Coefficient(1), 0),
                ConstraintType.GREATER_OR_EQUAL);
        var c1 = new Constraint(new LinearExpression().add(new Coefficient(-2)).add(new Coefficient(1), 1),
                ConstraintType.GREATER_OR_EQUAL);
        var c2 = new Constraint(new LinearExpression().add(new Coefficient(-1)).add(new Coefficient(1), 2),
                ConstraintType.GREATER_OR_EQUAL);
        var ph = new CPolyhedron(3, DegenerateElement.UNIVERSE).addConstraint(c0).addConstraint(c1).addConstraint(c2);
        ph.unconstrainSpaceDimension(0);
        assertEquals(new CPolyhedron(3, DegenerateElement.UNIVERSE).addConstraint(c1).addConstraint(c2), ph);
        ph = new CPolyhedron(3, DegenerateElement.UNIVERSE).addConstraint(c0).addConstraint(c1).addConstraint(c2);
        long[] ds = { 0, 2 };
        ph.unconstrainSpaceDimensions(ds);
        assertEquals(new CPolyhedron(3, DegenerateElement.UNIVERSE).addConstraint(c1), ph);
    }

    @Test
    void testWidenings() {
        var le = new LinearExpression().add(new Coefficient(1), 0);
        var c0 = new Constraint(new LinearExpression(le).add(new Coefficient(-3)), ConstraintType.GREATER_OR_EQUAL);
        var c1 = new Constraint(new LinearExpression(le).add(new Coefficient(-2)), ConstraintType.GREATER_OR_EQUAL);
        var c2 = new Constraint(new LinearExpression(le).add(new Coefficient(-1)), ConstraintType.GREATER_OR_EQUAL);
        var ph = new CPolyhedron(3, DegenerateElement.UNIVERSE).addConstraint(c0);
        var ph1 = new CPolyhedron(3, DegenerateElement.UNIVERSE).addConstraint(c1);
        var w = new WideningToken(1);
        ph1.H79WideningAssign(ph, w);
        assertEquals(0, w.tokens);
        var optMin = ph1.minimize(le);
        assertTrue(optMin.isPresent());
        var min = optMin.get();
        assertEquals(new Coefficient(2), min.supN);
        var ph2 = new CPolyhedron(3, DegenerateElement.UNIVERSE).addConstraint(c2);
        ph2.H79WideningAssign(ph1, w);
        assertTrue(ph2.minimize(le).isEmpty());
    }

    @Test
    void test1() {
        var lec = new LinearExpression().add(new Coefficient(3)).add(new Coefficient(1), 0);
        var c1 = new Constraint(lec, ConstraintType.GREATER_THAN);
        lec.add(new Coefficient(-1), 1);
        var c2 = new Constraint(lec, ConstraintType.EQUAL);

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
        var le = new LinearExpression().add(new Coefficient(1), 0);
        assertFalse(ph1.boundsFromAbove(le));
        assertTrue(ph1.boundsFromBelow(le));

        // minimize / maximize
        var res = ph1.minimizeWithPoint(le);
        assertTrue(res.isPresent());
        var extremals = res.get();
        assertEquals(new Coefficient(-3), extremals.supN);
        assertEquals(new Coefficient(1), extremals.supD);
        assertTrue(extremals.isMaximum);
        Generator g = new Generator(new LinearExpression(3).add(new Coefficient(-3), 0), GeneratorType.POINT,
                new Coefficient(1));
        assertEquals(g, extremals.point);

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
        var lec = new LinearExpression().add(new Coefficient(3)).add(new Coefficient(1), 0);
        var c1 = new Constraint(lec, ConstraintType.GREATER_THAN);
        lec.add(new Coefficient(-1), 1);
        var c2 = new Constraint(lec, ConstraintType.EQUAL);

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
