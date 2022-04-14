package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.*;

import it.unich.jppl.Constraint.ConstraintType;
import it.unich.jppl.LibPPL.VariableOutputFunction;

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
        String expectedVersion = major + "." + minor + (revision != 0 ? "." + revision : "")
                + (beta != 0 ? "b" + beta : "");
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
        assertEquals("C", PPL.ioASPrintVariable(var));
        VariableOutputFunction f = PPL.ioGetVariableOutputFunction();
        PPL.ioSetVariableOutputFunction((x) -> "v" + x);
        assertEquals("v2", PPL.ioASPrintVariable(var));
        PPL.ioSetVariableOutputFunction(f);
        assertEquals("C", PPL.ioASPrintVariable(var));
    }

    @Test
    void wrapStringTest() {
        String s = "This  is a very long text";
        String newLine = System.getProperty("line.separator");
        String expected = String.join(newLine, "This  is a", "  very long", "  text");
        String actual = PPL.ioWrapString(s, 2, 10, 10);
        assertEquals(expected, actual);
    }

    @Test
    void errorHandlerTest() {
        System.out.println(PPL.getMaxSpaceDimension());
        var le = LinearExpression.zero();
        le.add(Coefficient.ONE, 1);
        var c = Constraint.of(le, ConstraintType.EQUAL);
        var cs = ConstraintSystem.of(c);
        var p = DoubleBox.from(cs);

        var le2 = LinearExpression.zero();
        le2.add(Coefficient.ONE, 2);
        var c2 = Constraint.of(le2, ConstraintType.EQUAL);
        var cs2 = ConstraintSystem.of(c2);
        var p2 = DoubleBox.from(cs2);

        try {
            p.upperBoundAssign(p2);

        } catch (PPLError e) {
            System.out.println(e);
        }
        System.out.println(p);
    }
}
