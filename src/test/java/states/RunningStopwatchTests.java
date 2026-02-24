package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.stopwatch.*;
import states.timer.*;

class RunningStopwatchTests {

    Context c;

    @BeforeEach
    void setup() {
        c = new Context();
        AbstractTimer.resetInitialValues();
        AbstractStopwatch.resetInitialValues();
    }

    /** Helper: navigate to RunningStopwatch */
    private void startStopwatch() {
        c.left(); // IdleTimer -> ResetStopwatch
        c.up(); // ResetStopwatch -> RunningStopwatch
    }

    @Test
    @DisplayName("RunningStopwatch: doIt increments totalTime")
    void testDoIt() {
        startStopwatch();
        assertSame(RunningStopwatch.Instance(), c.currentState);
        assertEquals(0, AbstractStopwatch.getTotalTime());
        c.tick();
        assertEquals(1, AbstractStopwatch.getTotalTime());
        c.tick();
        assertEquals(2, AbstractStopwatch.getTotalTime());
    }

    @Test
    @DisplayName("RunningStopwatch: right transitions to ResetStopwatch")
    void testRightReset() {
        startStopwatch();
        c.tick();
        c.tick();
        assertEquals(2, AbstractStopwatch.getTotalTime());
        c.right(); // reset
        assertSame(ResetStopwatch.Instance(), c.currentState);
    }

    @Test
    @DisplayName("RunningStopwatch: up transitions to LaptimeStopwatch (split)")
    void testUpSplit() {
        startStopwatch();
        c.tick();
        c.up(); // split
        assertSame(LaptimeStopwatch.Instance(), c.currentState);
    }

    @Test
    @DisplayName("RunningStopwatch: displayString shows totalTime")
    void testDisplayString() {
        startStopwatch();
        assertEquals("totalTime = 0", c.currentState.getDisplayString());
        c.tick();
        assertEquals("totalTime = 1", c.currentState.getDisplayString());
    }

    @Test
    @DisplayName("RunningStopwatch: button labels are change mode/split/reset")
    void testButtonLabels() {
        startStopwatch();
        assertEquals("change mode", c.currentState.getLeftText());
        assertEquals("split", c.currentState.getUpText());
        assertEquals("reset", c.currentState.getRightText());
    }
}
