package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CoefficientTest {

    @BeforeAll
    static void init() {
        PPL.pplInitialize();
    }

    @AfterAll
    static void finish() {
        PPL.pplFinalize();
    }

    @Test
    void testConstructor() {
        var c = new Coefficient();
        assertEquals("0", c.toString());
        c = new Coefficient(2);
        assertEquals("2", c.toString());
        c = new Coefficient("29383734");
        assertEquals("29383734", c.toString());
        c = new Coefficient(new BigInteger("29383735"));
        assertEquals("29383735", c.toString());
        assertEquals(new BigInteger("29383735"), c.bigIntegerValue());
    }

    @Test
    void testAssignment() {
        var c = new Coefficient();
        assertEquals("0", c.toString());
        c.assign(2);
        assertEquals("2", c.toString());
        c.assign("29383734");
        assertEquals("29383734", c.toString());
        c.assign(new BigInteger("29383735"));
        assertEquals("29383735", c.toString());
        assertEquals(new BigInteger("29383735"), c.bigIntegerValue());
    }

    @Test
    void testIsOK() {
        var c = new Coefficient(35);
        assertTrue(c.isOK());
    }

    @Test
    void testBounds() {
        var c = new Coefficient(1);
        assertTrue(! Coefficient.isBounded() || (c.minValue() < c.maxValue()));
    }

    @Test
    void testEquality() {
        var c1 = new Coefficient(1);
        var c2 = new Coefficient(2);
        var c3 = new Coefficient(1);
        assertEquals(c1, c1);
        assertEquals(c1, c3);
        assertNotEquals(c1, c2);
        assertNotEquals(c1, "1");
    }
}
