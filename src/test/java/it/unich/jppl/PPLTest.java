package it.unich.jppl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PPLTest {

    @BeforeAll
    static void init() {
        PPL.pplInitialize();
    }

    @AfterAll
    static void finish() {
        PPL.pplFinalize();
    }

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
}
