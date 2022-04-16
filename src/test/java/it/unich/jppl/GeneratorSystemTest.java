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
        var le = LinearExpression.zero();
        le.add(Coefficient.valueOf(1), 0);
        g1 = Generator.of(le, GeneratorType.RAY);
        le.add(Coefficient.valueOf(-1), 1);
        g2 = Generator.of(le, GeneratorType.POINT);
    }

    @Test
    void testEmptyConstructor() {
        var gs = GeneratorSystem.empty();
        assertTrue(gs.isOK());
        assertTrue(gs.isEmpty());
        assertEquals(0, gs.getSpaceDimension());
    }

    @Test
    void testInsert() {
        var gs = GeneratorSystem.singleton(g1);
        assertTrue(gs.isOK());
        assertFalse(gs.isEmpty());
        assertEquals(1, gs.getSpaceDimension());
        gs.clear();
        assertTrue(gs.isEmpty());
        assertEquals(0, gs.getSpaceDimension());
    }

    @Test
    void testToString() {
        var gs = GeneratorSystem.empty();
        gs.add(g1);
        gs.add(g2);
        assertEquals("r(A), p(A - B)", gs.toString());
    }

    @Test
    void testIterator() {
        var gs = GeneratorSystem.of(g1, g2);
        var le = LinearExpression.zero(2);
        le.add(Coefficient.valueOf(1), 0);
        var c1bis = Generator.of(le, GeneratorType.RAY);
        var gsi = gs.iterator();
        assertEquals(c1bis, gsi.next());
        assertEquals(g2, gsi.next());
        assertThrows(NoSuchElementException.class, () -> gsi.next());
    }
}
