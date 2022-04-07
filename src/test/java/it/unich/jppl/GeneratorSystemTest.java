package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import it.unich.jppl.Generator.GeneratorType;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GeneratorSystemTest {

    static Generator g1, g2;

    @BeforeAll
    static void init() {
        var le = new LinearExpression();
        le.add(new Coefficient(1), 0);
        g1 = new Generator(le, GeneratorType.RAY, new Coefficient(0));
        le.add(new Coefficient(-1), 1);
        g2 = new Generator(le, GeneratorType.POINT, new Coefficient(1));
    }

    @Test
    void testEmptyConstructor() {
        var gs = new GeneratorSystem();
        assertTrue(gs.isOK());
        assertTrue(gs.isEmpty());
        assertEquals(0, gs.getSpaceDimension());
    }

    @Test
    void testInsert() {
        var gs = new GeneratorSystem();
        gs.add(g1);
        assertTrue(gs.isOK());
        assertFalse(gs.isEmpty());
        assertEquals(1, gs.getSpaceDimension());
        gs.clear();
        assertTrue(gs.isEmpty());
        assertEquals(0, gs.getSpaceDimension());
    }

    @Test
    void testToString() {
        var gs = new GeneratorSystem();
        gs.add(g1);
        gs.add(g2);
        assertEquals("r(A), p(A - B)", gs.toString());
    }

    @Test
    void testIterator() {
        var gs = new GeneratorSystem();
        gs.add(g1);
        gs.add(g2);
        var le = new LinearExpression(2);
        le.add(new Coefficient(1), 0);
        var c1bis = new Generator(le, GeneratorType.RAY, new Coefficient(1));
        var gsi = gs.iterator();
        assertEquals(c1bis, gsi.next());
        assertEquals(g2, gsi.next());
        assertThrows(NoSuchElementException.class, () -> gsi.next());
    }
}
