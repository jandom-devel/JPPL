package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PPLTest {

    @Test
    void irrationalPrecisionTest() {
        int p = PPL.getIrrationalPrecision();
        PPL.setIrrationalPrecision(64);
        assertEquals(64, PPL.getIrrationalPrecision());
        PPL.setIrrationalPrecision(p);
    }

    @Test
    void versionTest() {
        int major = PPL.getVersionMajor();
        int minor = PPL.getVersionMinor();
        int revision = PPL.getVersionRevision();
        int beta = PPL.getVersionBeta();
        String version = PPL.getVersion();
        String expectedVersion = major + "." + minor +
            (revision != 0 ? "." + revision : "") + (beta != 0 ? "b" + beta  : "");
        assertEquals(expectedVersion, version);
    }

    @Test
    void dimensionsTest() {
        long d = PPL.getMaxSpaceDimension();
        long nd = PPL.getNotADimension();
        assertTrue(nd > d || nd < 0);
    }

    @Test
    void variablePrintTest() {
        long var = 2;
        assertEquals("C", PPL.IOASPrintVariable(var));
        VariableOutputFunction f = PPL.IOGetVariableOutputFunction();
        PPL.IOSetVariableOutputFunction( (x) -> "v" + x );
        assertEquals("v2", PPL.IOASPrintVariable(var));
        PPL.IOSetVariableOutputFunction(f);
        assertEquals("C", PPL.IOASPrintVariable(var));
    }

    @Test
    void wrapStringTest() {
        String s = "This  is a very long text";
        String newLine = System.getProperty("line.separator");
        String expected = String.join(newLine,
            "This  is a",
            "  very long",
            "  text");
        String actual = PPL.IOWrapString(s, 2, 10, 10);
        assertEquals(expected, actual);
    }
}
