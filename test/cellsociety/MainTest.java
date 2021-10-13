package cellsociety;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Feel free to completely change this code or delete it entirely. 
 */
class MainTest {
    // how close do real valued numbers need to be to count as the same
    static final double TOLERANCE = 0.0005;
    
    /**
     * Test a method from Main.
     */
    @Test
    void testVersionIsReady () {
	Main m = new Main();
	// different ways to test double results
        assertEquals(1, Math.round(m.getVersion() * 1000));
        assertTrue(m.getVersion() < 1);
        assertEquals(0.001, m.getVersion(), TOLERANCE);
    }
}
