package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

public class CoefficientTest {

    @Test
    void testConstructor() {
        var c = Coefficient.zero();
        assertEquals("0", c.toString());
        c = Coefficient.valueOf(2);
        assertEquals("2", c.toString());
        c = Coefficient.valueOf("29383734");
        assertEquals("29383734", c.toString());
        c = Coefficient.valueOf(new BigInteger("29383735"));
        assertEquals("29383735", c.toString());
        assertEquals(new BigInteger("29383735"), c.bigIntegerValue());
    }

    @Test
    void testIsOK() {
        var c = Coefficient.valueOf(35);
        assertTrue(c.isOK());
    }

    @Test
    void testBounds() {
        var c = Coefficient.valueOf(1);
        assertTrue(!Coefficient.isBounded() || (c.minAllowed() < c.maxAllowed()));
    }

    @Test
    void testEquality() {
        var c1 = Coefficient.valueOf(1);
        var c2 = Coefficient.valueOf(2);
        var c3 = Coefficient.valueOf(1);
        assertEquals(c1, c1);
        assertEquals(c1, c3);
        assertNotEquals(c1, c2);
        assertNotEquals(c1, "1");
    }
}
